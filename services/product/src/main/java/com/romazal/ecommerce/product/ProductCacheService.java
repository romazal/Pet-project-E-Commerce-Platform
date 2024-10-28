package com.romazal.ecommerce.product;

import com.romazal.ecommerce.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCacheService {

    private final ProductRepository repository;

    @Cacheable(value = "products", key = "#productId")
    public Product getProductById(UUID productId) {
        log.info("Fetching product with ID:: {} from PostgreSQL repository", productId);
        return repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        format("No product found with the provided ID:: %s", productId)
                ));
    }

    @CachePut(value = "products", key = "#product.getProductId()")
    public void updateProduct(Product product) {
        repository.save(product);
    }

    public void updateAllProducts(List<Product> products) {
        for (Product product : products) {
            updateProduct(product);
        }
    }

    @CacheEvict(value = "products", key = "#productId")
    public void deleteProduct(UUID productId) {
        repository.deleteById(productId);
    }

    @Cacheable(value = "productsByVendor", key = "#vendorId")
    public List<Product> getAllProductsByVendorId(Long vendorId) {
        return repository.findAllProductsByVendorId(vendorId);
    }
}

