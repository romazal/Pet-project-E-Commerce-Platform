package com.romazal.ecommerce.vendor;

import org.springframework.stereotype.Service;

import static com.romazal.ecommerce.vendor.Status.*;

@Service
public class VendorMapper {
    public Vendor toVendor(VendorRequest vendorRequest) {
        if(vendorRequest == null) return null;

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

    public VendorResponse toVendorResponse(Vendor vendor) {
        if(vendor == null) return null;

        return new VendorResponse(
                vendor.getUsername(),
                vendor.getStoreName(),
                vendor.getStoreAddress(),
                vendor.getStorePhone(),
                vendor.getStoreEmail(),
                vendor.getDescription(),
                vendor.getStatus()
        );

    }

    public VendorContactResponse toVendorContactResponse(Vendor vendor) {
        if(vendor == null) return null;

        return new VendorContactResponse(
            vendor.getStoreName(),
            vendor.getStoreAddress(),
            vendor.getStorePhone(),
            vendor.getStoreEmail()
        );
    }
}
