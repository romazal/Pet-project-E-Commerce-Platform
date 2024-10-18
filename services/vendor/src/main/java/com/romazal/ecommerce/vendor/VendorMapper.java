package com.romazal.ecommerce.vendor;

import org.springframework.stereotype.Service;

import static com.romazal.ecommerce.vendor.Status.*;

@Service
public class VendorMapper {
    public Vendor toVendor(VendorRequest vendorRequest) {

        return new Vendor().builder()
                .id(vendorRequest.id())
                .username(vendorRequest.username())
                .password(vendorRequest.password())
                .storeName(vendorRequest.storeName())
                .businessLicenseNumber(vendorRequest.businessLicenseNumber())
                .storeAddress(vendorRequest.storeAddress())
                .storePhone(vendorRequest.storePhone())
                .storeEmail(vendorRequest.storeEmail())
                .description(vendorRequest.description())
                .status(ACTIVE)
                .build();
    }
}
