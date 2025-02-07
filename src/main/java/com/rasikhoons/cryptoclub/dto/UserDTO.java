package com.rasikhoons.cryptoclub.dto;

import lombok.Data;

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
}
