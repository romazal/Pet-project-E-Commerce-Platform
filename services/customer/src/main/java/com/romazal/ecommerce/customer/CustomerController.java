package com.romazal.ecommerce.customer;

import com.romazal.ecommerce.order.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/register")
    public ResponseEntity<Long> registerCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity.ok(service.registerCustomer(customerRequest));
    }

    @GetMapping("/profiles/{customer-id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("customer-id") Long customerId) {
        return ResponseEntity.ok(service.getCustomerById(customerId));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @PutMapping("/profiles")
    public ResponseEntity<Void> updateCustomerById(
            @RequestBody @Valid CustomerRequest customerRequest
    ) {
        service.updateCustomerById(customerRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profiles/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(
        @PathVariable("customer-id") Long customerId
    ) {
        service.deleteCustomerById(customerId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/orders/{customer-id}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getAllOrdersByCustomerId(customerId));
    }



}
