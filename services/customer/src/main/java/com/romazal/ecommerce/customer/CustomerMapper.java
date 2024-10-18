package com.romazal.ecommerce.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequest customerRequest) {
        return new Customer().builder()
                .id(customerRequest.id())
                .username(customerRequest.username())
                .password(customerRequest.password())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .phoneNumber(customerRequest.phoneNumber())
                .email(customerRequest.email())
                .shippingAddress(customerRequest.shippingAddress())
                .billingAddress(customerRequest.billingAddress())
                .build();
    }
}
