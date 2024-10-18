package com.romazal.ecommerce.vendor;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "vendor_profiles")
public class Vendor {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String businessLicenseNumber;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private String storePhone;

    @Column(nullable = false, unique = true)
    private String storeEmail;

    @Column
    private String description;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
