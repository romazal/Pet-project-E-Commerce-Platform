package com.romazal.ecommerce.product;

import com.romazal.ecommerce.category.Category;
import org.springframework.stereotype.Service;

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
                .category(
                        new Category().builder()
                                .categoryId(productRequest.categoryId())
                                .build()
                )
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
                product.getCategory().getCategoryId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
