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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final VendorClient vendorClient;
    private final NotificationKafkaTemplate notificationKafkaTemplate;


    public UUID addProduct(ProductRequest productRequest) {
        Product product = mapper.toProduct(productRequest);
        return repository.save(product).getProductId();
    }


    public ProductResponse getProductById(UUID productId) {
        return repository.findById(productId).map(mapper::toProductResponse)
                .orElseThrow(() -> new ProductNotFoundException(
                        format("No product found with the provided ID:: %s", productId)
                ));
    }

    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public void updateProduct(ProductRequest productRequest) {
        var product = repository.findById(productRequest.productId())
                .orElseThrow(() -> new ProductNotFoundException(
                        format("No product found with the provided ID:: %s", productRequest.productId())
                ));

        mergeProduct(product, productRequest);
        repository.save(product);
    }

    private void mergeProduct(Product product, ProductRequest productRequest) {

        if(productRequest.vendorId() != null){
            product.setVendorId(productRequest.vendorId());
        }

        if(isNotBlank(productRequest.description())){
            product.setDescription(productRequest.description());
        }

        if(productRequest.price() != null){
            product.setPrice(productRequest.price());
        }

        if(productRequest.categoryId() != null){
            product.setCategory(
                    new Category().builder()
                            .categoryId(productRequest.categoryId())
                            .build()
            );
        }

        if(productRequest.stockQuantity() != null){
            product.setStockQuantity(productRequest.stockQuantity());
        }

        if(productRequest.thresholdQuantity() != null){
            product.setThresholdQuantity(productRequest.thresholdQuantity());
        }

        if(isNotBlank(productRequest.sku())){
            product.setSku(productRequest.sku());
        }

    }

    public void deleteProduct(UUID productId) {
        if(!repository.existsById(productId)){
            throw new ProductNotFoundException(
                    format("No product found with the provided ID:: %s", productId)
            );
        }

        repository.deleteById(productId);
    }

    public List<ProductResponse> getAllProductsByVendorId(Long vendorId) {
        return repository.findAllProductsByVendorId(vendorId)
                .stream()
                .map(mapper::toProductResponse)
                .toList();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        log.info("Starting product purchase process for {} items", request.size());

        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        log.debug("Collected product IDs from purchase requests: {}", productIds);

        var storedProducts = repository.findAllByProductIdInOrderByProductId(productIds);
        log.debug("Fetched {} products from repository matching IDs", storedProducts.size());

        if (productIds.size() != storedProducts.size()) {
            log.error("Mismatch in requested product count and stored products; some products do not exist.");
            throw new ProductPurchaseException("One or more products do not exist");
        }

        var storedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        log.debug("Sorted purchase requests by product ID");

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);
            log.info("Processing purchase for Product ID: {}, Requested Quantity: {}", product.getProductId(), productRequest.quantity());

            if (product.getStockQuantity() < productRequest.quantity()) {
                log.error("Insufficient stock for Product ID: {}; Available: {}, Requested: {}",
                        product.getProductId(), product.getStockQuantity(), productRequest.quantity());
                throw new ProductPurchaseException(
                        "Insufficient stock for product with ID:: " + productRequest.productId()
                );
            }

            var newAvailableQuantity = product.getStockQuantity() - productRequest.quantity();
            product.setStockQuantity(newAvailableQuantity);
            repository.save(product);
            log.info("Updated stock for Product ID: {}. New quantity: {}", product.getProductId(), newAvailableQuantity);

            var response = mapper.toProductPurchaseResponse(product, productRequest.quantity());
            purchasedProducts.add(response);
            log.debug("Added ProductPurchaseResponse for Product ID: {} with quantity: {}", product.getProductId(), productRequest.quantity());

            if (product.getStockQuantity() <= product.getThresholdQuantity()) {
                log.warn("Stock for Product ID: {} has reached threshold. Initiating vendor notification.", product.getProductId());

                VendorContactResponse vendorContacts = vendorClient.getVendorContactsByVendorId(product.getVendorId());
                log.info("Fetched vendor contact for Vendor ID: {}", product.getVendorId());

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

        log.info("Completed product purchase process with {} purchased products", purchasedProducts.size());
        return purchasedProducts;
    }

}
