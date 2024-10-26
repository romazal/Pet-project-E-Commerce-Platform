package com.romazal.ecommerce.product;

import com.romazal.ecommerce.category.Category;
import com.romazal.ecommerce.exception.ProductNotFoundException;
import com.romazal.ecommerce.exception.ProductPurchaseException;
import lombok.RequiredArgsConstructor;
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
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;


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
        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        var storedProducts = repository.findAllByProductIdInOrderByProductId(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products do not exist");
        }

        var storedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);
            if(product.getStockQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException(
                        "Insufficient stock remaining quantity of product with ID:: " + productRequest.productId()
                );
            }
            var newAvailableQuantity = product.getStockQuantity() - productRequest.quantity();
            product.setStockQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }

        //todo threshold quantity notification

        return purchasedProducts;
    }
}
