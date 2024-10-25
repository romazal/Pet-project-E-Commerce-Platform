package com.romazal.ecommerce.customer;

import com.romazal.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;


    public Long registerCustomer(CustomerRequest customerRequest) {
        Customer customer = mapper.toCustomer(customerRequest);
        return repository.save(customer).getId();
    }

    public CustomerResponse getCustomerById(Long customerId) {
        return repository.findById(customerId).map(mapper::toCustomerResponse)
                .orElseThrow(() -> new CustomerNotFoundException(
                    format("No customer found with the provided ID:: %s", customerId)
                ));
    }

    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    public void updateCustomerById(CustomerRequest customerRequest) {
        var customer = repository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                    format("No customer found with the provided ID:: %s", customerRequest.id())
                ));

        mergeCustomer(customer, customerRequest);
        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest customerRequest) {

        if(isNotBlank(customerRequest.username())){
            customer.setUsername(customerRequest.username());
        }

        if(isNotBlank(customerRequest.password())){
            customer.setPassword(customerRequest.password());
        }

        if(isNotBlank(customerRequest.firstname())){
            customer.setFirstname(customerRequest.firstname());
        }

        if(isNotBlank(customerRequest.lastname())){
            customer.setLastname(customerRequest.lastname());
        }

        if(isNotBlank(customerRequest.phoneNumber())){
            customer.setPhoneNumber(customerRequest.phoneNumber());
        }

        if(isNotBlank(customerRequest.email())){
            customer.setEmail(customerRequest.email());
        }

        if(isNotBlank(customerRequest.shippingAddress())){
            customer.setShippingAddress(customerRequest.shippingAddress());
        }

        if(isNotBlank(customerRequest.billingAddress())){
            customer.setBillingAddress(customerRequest.billingAddress());
        }

    }


    public void deleteCustomerById(Long customerId) {
        repository.deleteById(customerId);
    }

    public List getAllOrdersByCustomerId(Long customerId) {
        //todo
        return null;
    }
}
