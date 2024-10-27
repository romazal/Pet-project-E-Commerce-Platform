package com.romazal.ecommerce.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null) return null;

        return new Customer().builder()
                .id(customerRequest.id())
                .username(customerRequest.username())
                .password(customerRequest.password())
                .firstname(customerRequest.firstname())
                .lastname(customerRequest.lastname())
                .phoneNumber(customerRequest.phoneNumber())
                .email(customerRequest.email())
                .shippingAddress(customerRequest.shippingAddress())
                .billingAddress(customerRequest.billingAddress())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) return null;

        return new CustomerResponse(
            customer.getId(),
            customer.getUsername(),
            customer.getFirstname(),
            customer.getLastname(),
            customer.getPhoneNumber(),
            customer.getEmail(),
            customer.getShippingAddress(),
            customer.getBillingAddress()
        );
    }
}
