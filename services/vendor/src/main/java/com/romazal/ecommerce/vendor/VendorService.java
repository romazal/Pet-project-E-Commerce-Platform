package com.romazal.ecommerce.vendor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository repository;
    private final VendorMapper mapper;


    public Long register(VendorRequest vendorRequest) {
        Vendor vendor = mapper.toVendor(vendorRequest);
        return repository.save(vendor).getId();
    }

}
