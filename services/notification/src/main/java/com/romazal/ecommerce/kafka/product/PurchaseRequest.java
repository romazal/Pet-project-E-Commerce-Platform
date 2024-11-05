package com.romazal.ecommerce.kafka.product;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseRequest(

        UUID productId,

        Double quantity,

        BigDecimal unitPrice
){
}
