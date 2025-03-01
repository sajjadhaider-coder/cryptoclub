package com.rasikhoons.cryptoclub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @NotNull
    @Column(name = "USER_NAME", unique = true, nullable = false, length = 20)
    @JsonIgnore // Hide sensitive data if necessary
    private String userName;

    @NotNull
    @Column(name = "VERIFICATION_CODE", length = 10)
    private String verificationCode;

    @JsonIgnore // Prevent password from being serialized
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "DEVICE_TYPE")
    private String deviceType;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "LOCATION")
    private String userLocation;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "user_Type")
    private String userType;

    @Column(name = "refId")
    private Long refId;

    @Column(name = "parentRefId")
    private Long parentRefId;

    @Column(name = "balance", columnDefinition = "Decimal (10, 2)")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "isAgent", columnDefinition = "BigInt default '0'")
    private Boolean isAgent = false;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();  // Set the current timestamp when the entity is persisted
        }

        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();  // Set the current timestamp when the entity is persisted
        }
    }
}
