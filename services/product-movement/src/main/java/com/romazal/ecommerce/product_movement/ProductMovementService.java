package com.romazal.ecommerce.product_movement;

import com.romazal.ecommerce.exception.ProductMovementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductMovementService {

    private final ProductMovementRepository repository;
    private final ProductMovementMapper mapper;


    public List<ProductMovementResponse> getAllProductMovements() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductMovementResponse)
                .toList();
    }

    public ProductMovementResponse getProductMovementById(UUID productMovementId) {
        return repository.findById(productMovementId).map(mapper::toProductMovementResponse)
                .orElseThrow(() -> new ProductMovementNotFoundException(
                        format("No product movement found with the provided ID:: %s", productMovementId)
                ));
    }


    public List<ProductMovementResponse> getAllProductMovementsByProductId(UUID productId) {
        return repository.findAllByProductIdOrderByCreatedDateDesc(productId)
                .stream()
                .map(mapper::toProductMovementResponse)
                .toList();
    }
}
