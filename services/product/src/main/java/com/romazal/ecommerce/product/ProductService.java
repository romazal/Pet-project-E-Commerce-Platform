package com.romazal.ecommerce.product;

import com.romazal.ecommerce.category.Category;
import com.romazal.ecommerce.exception.ProductNotFoundException;
import com.romazal.ecommerce.exception.ProductPurchaseException;
import com.romazal.ecommerce.kafka.NotificationKafkaTemplate;
import com.romazal.ecommerce.kafka.ProductThresholdNotification;
import com.romazal.ecommerce.vendor.VendorClient;
import com.romazal.ecommerce.vendor.VendorContactResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductCacheService productCacheService;
    private final VendorClient vendorClient;
    private final NotificationKafkaTemplate notificationKafkaTemplate;


    public UUID addProduct(ProductRequest productRequest) {
        if(productRequest.productId() != null && repository.existsById(productRequest.productId())) {
            log.error("Failed to add the product, a product with id {} already exists", productRequest.productId());
            throw new IllegalArgumentException(
                    format("Failed to add the product, a product with id %s already exists", productRequest.productId())
            );

        }

        if(!vendorClient.existsById(productRequest.vendorId())) {
            log.error("Failed to add the product, the vendor with id {} does not exist", productRequest.vendorId());
            throw new IllegalArgumentException(
                    format("Failed to add the product, the vendor does not exist with ID:: %s ", productRequest.vendorId())
            );
        }

        Product product = mapper.toProduct(productRequest);
        return repository.save(product).getProductId();
    }

    public void addAllProducts(List<ProductRequest> productRequests) {
        for (ProductRequest productRequest : productRequests) {
            addProduct(productRequest);
        }
    }

    public ProductResponse getProductById(UUID productId) {
        var product = productCacheService.getProductById(productId);
        return mapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public void updateProduct(ProductRequest productRequest) {
        if (productRequest.productId() == null) {
            log.error("Failed to update the product, no productId provided");
            throw new IllegalArgumentException(
                    "Failed to update the product, no productId provided"
            );
        }

        var product = repository.findById(productRequest.productId())
                .orElseThrow(() -> new ProductNotFoundException(
                        format("Failed to update the product, no product found with ID:: %s ", productRequest.productId())
                ));

        mergeProduct(product, productRequest);

        productCacheService.updateProduct(product);
    }

    private void mergeProduct(Product product, ProductRequest productRequest) {

        if (productRequest.vendorId() != null) {
            if(!vendorClient.existsById(productRequest.vendorId())) {
                log.error("Failed to update the product, the vendor with id {} does not exist", productRequest.vendorId());
                throw new IllegalArgumentException(
                        format("Failed to update the product, the vendor does not exist with ID:: %s ", productRequest.vendorId())
                );
            }

            product.setVendorId(productRequest.vendorId());
        }

        if (isNotBlank(productRequest.description())) {
            product.setDescription(productRequest.description());
        }

        if (productRequest.price() != null) {
            product.setPrice(productRequest.price());
        }

        if (productRequest.categoryId() != null) {
            product.setCategory(
                    new Category().builder()
                            .categoryId(productRequest.categoryId())
                            .build()
            );
        }

        if (productRequest.stockQuantity() != null) {
            product.setStockQuantity(productRequest.stockQuantity());
        }

        if (productRequest.thresholdQuantity() != null) {
            product.setThresholdQuantity(productRequest.thresholdQuantity());
        }

        if (isNotBlank(productRequest.sku())) {
            product.setSku(productRequest.sku());
        }

    }

    public void deleteProduct(UUID productId) {
        if (!repository.existsById(productId)) {
            throw new ProductNotFoundException(
                    format("No product found with the provided ID:: %s", productId)
            );
        }

        productCacheService.deleteProduct(productId);
    }

    public List<ProductResponse> getAllProductsByVendorId(Long vendorId) {
        return productCacheService.getAllProductsByVendorId(vendorId)
                .stream()
                .map(mapper::toProductResponse)
                .toList();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        log.info("Starting product purchase process for {} items", request.size());

        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        var storedProducts = repository.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(
                        Product::getProductId,
                        Function.identity()
                ));

        if (productIds.size() != storedProducts.size()) {
            log.error("Mismatch in requested product count and stored products; some products do not exist.");
            throw new ProductPurchaseException("One or more products do not exist");
        }

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        var productsToSave = new ArrayList<Product>();

        for (ProductPurchaseRequest productRequest : request) {
            var product = storedProducts.getOrDefault(productRequest.productId(), null);

            if (product == null) {
                log.error("Product with ID:: {} does not exist", productRequest.productId());
                throw new ProductPurchaseException(
                        format("Product with ID:: %s does not exist", productRequest.productId())
                );
            }

            if (product.getStockQuantity() < productRequest.quantity()) {
                log.error("Insufficient stock for Product ID: {}; Available: {}, Requested: {}",
                        product.getProductId(), product.getStockQuantity(), productRequest.quantity());
                throw new ProductPurchaseException(
                        "Insufficient stock for product with ID:: " + productRequest.productId()
                );
            }

            var newAvailableQuantity = product.getStockQuantity() - productRequest.quantity();
            product.setStockQuantity(newAvailableQuantity);
            productsToSave.add(product);

            var response = mapper.toProductPurchaseResponse(product, productRequest.quantity());
            purchasedProducts.add(response);

            if (product.getStockQuantity() <= product.getThresholdQuantity()) {
                log.warn("Stock for Product ID: {} has reached threshold. Initiating vendor notification.", product.getProductId());

                VendorContactResponse vendorContacts = vendorClient.getVendorContactsByVendorId(product.getVendorId());

                notificationKafkaTemplate.sendProductThresholdNotification(
                        new ProductThresholdNotification(
                                product.getProductId(),
                                product.getName(),
                                product.getSku(),
                                product.getStockQuantity(),
                                product.getThresholdQuantity(),
                                vendorContacts.storeName(),
                                vendorContacts.storeEmail()
                        )
                );
                log.info("Sent threshold notification for Product ID: {} to vendor store: {}", product.getProductId(), vendorContacts.storeName());
            }
        }

        productCacheService.updateAllProducts(productsToSave);

        log.info("Completed product purchase process with {} purchased products", purchasedProducts.size());
        return purchasedProducts;
    }

}
