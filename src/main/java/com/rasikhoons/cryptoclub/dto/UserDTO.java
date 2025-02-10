package com.rasikhoons.cryptoclub.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {

    private long id;
    private String verificationCode;
    private String userName;
    private String password;
    private String deviceType;
    private String status;
    private String ipAddress;
    private String userLocation;
    private int userId;
    private String userType;
    private long refId;
    private long parentRefId;
    private BigDecimal balance;
    private boolean isAgent;
}
