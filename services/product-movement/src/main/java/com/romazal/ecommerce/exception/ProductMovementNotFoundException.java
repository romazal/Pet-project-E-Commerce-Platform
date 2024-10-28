package com.romazal.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductMovementNotFoundException extends RuntimeException{
    private final String message;
}
