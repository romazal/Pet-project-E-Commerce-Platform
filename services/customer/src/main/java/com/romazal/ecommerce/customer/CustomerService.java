package com.romazal.ecommerce.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;


    public Long register(CustomerRequest customerRequest) {
        Customer customer = mapper.toCustomer(customerRequest);
        return repository.save(customer).getId();
    }

}
