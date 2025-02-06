package com.rasikhoons.cryptoclub.controller;

import com.rasikhoons.cryptoclub.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class HelloAPI {

    @GetMapping("/sayhello")
    public ResponseEntity<ApiResponse> sayHello() {
        ApiResponse apiResponse = null;
        int status = 0;
        try {
            apiResponse = new ApiResponse(HttpStatus.OK.value(), "Success", null);
            status = HttpStatus.OK.value();
        } catch (Exception e) {
            status = HttpStatus.UNAUTHORIZED.value();
        }
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(status));
    }
}
