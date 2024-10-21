package com.romazal.ecommerce.vendor;

import com.romazal.ecommerce.exception.VendorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository repository;
    private final VendorMapper mapper;


    public Long registerVendor(VendorRequest vendorRequest) {
        Vendor vendor = mapper.toVendor(vendorRequest);
        return repository.save(vendor).getId();
    }

    public VendorResponse getVendorById(Long vendorId) {
        return repository.findById(vendorId).map(mapper::toVendorResponse)
                .orElseThrow(() -> new VendorNotFoundException(
                        format("No vendor found with the provided ID:: %s", vendorId)
                ));
    }


    public List<VendorResponse> getAllVendors() {
        return repository.findAll().stream().map(mapper::toVendorResponse).collect(Collectors.toList());
    }

    public void updateVendorById(VendorRequest vendorRequest) {
        var vendor = repository.findById(vendorRequest.id())
                .orElseThrow(() -> new VendorNotFoundException(
                        format("No vendor found with the provided ID:: %s", vendorRequest.id())
                ));

        mergeVendor(vendor, vendorRequest);
        repository.save(vendor);
    }

    private void mergeVendor(Vendor vendor, VendorRequest vendorRequest) {

        if(isNotBlank(vendorRequest.username())){
            vendor.setUsername(vendorRequest.username());
        }

        if(isNotBlank(vendorRequest.password())){
            vendor.setPassword(vendorRequest.password());
        }

        if(isNotBlank(vendorRequest.storeName())){
            vendor.setStoreName(vendorRequest.storeName());
        }

        if(isNotBlank(vendorRequest.businessLicenseNumber())){
            vendor.setBusinessLicenseNumber(vendorRequest.businessLicenseNumber());
        }

        if(isNotBlank(vendorRequest.storeAddress())){
            vendor.setStoreAddress(vendorRequest.storeAddress());
        }

        if(isNotBlank(vendorRequest.storePhone())){
            vendor.setStorePhone(vendorRequest.storePhone());
        }

        if(isNotBlank(vendorRequest.storeEmail())){
            vendor.setStoreEmail(vendorRequest.storeEmail());
        }

        if(isNotBlank(vendorRequest.description())){
            vendor.setDescription(vendorRequest.description());
        }

    }

    public void deleteVendorById(Long vendorId) {
        repository.deleteById(vendorId);
    }

    public List getAllProductsByVendorId(Long vendorId) {
        //todo
        return null;
    }
}
