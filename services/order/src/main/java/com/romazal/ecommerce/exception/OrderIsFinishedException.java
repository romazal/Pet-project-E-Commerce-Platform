package com.romazal.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderIsFinishedException extends RuntimeException{
    private final String message;
}
