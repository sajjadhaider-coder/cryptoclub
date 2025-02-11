package com.rasikhoons.cryptoclub.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserResponse {

    private long id;
    private String userName;
    private String deviceType;
    private String status;
    private String ipAddress;
    private String userLocation;
    private int userId;
    private String userType;
    private long refId;
    private long parentRefId;
    private BigDecimal balance;
    private Boolean isAgent;
}
