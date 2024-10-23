package com.romazal.ecommerce.product;

import com.romazal.ecommerce.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class ProductsService {

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
            product.setCategory(productRequest.categoryId());
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
        repository.deleteById(productId);
    }
}
