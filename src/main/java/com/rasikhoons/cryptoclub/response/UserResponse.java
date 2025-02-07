package com.rasikhoons.cryptoclub.response;

import lombok.Data;

@Data
public class UserResponse {

    private long id;
    private String username;
    private String deviceType;
    private String status;
    private String ipAddress;
    private String userLocation;
    private int userId;
}
