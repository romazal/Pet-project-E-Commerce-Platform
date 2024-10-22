package com.romazal.ecommerce.product;

import com.romazal.ecommerce.category.Category;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest productRequest) {
        if (productRequest == null) return null;

        return new Product().builder()
                .productId(productRequest.productId())
                .vendorId(productRequest.vendorId())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .category(productRequest.categoryId())
                .stockQuantity(productRequest.stockQuantity())
                .thresholdQuantity(productRequest.thresholdQuantity())
                .sku(productRequest.sku())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        if (product == null) return null;

        return new ProductResponse(
                product.getProductId(),
                product.getVendorId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getThresholdQuantity(),
                product.getSku(),
                product.getCreatedDate(),
                product.getCategory(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }
}
