package com.rasikhoons.cryptoclub.controller;

import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.service.WalletLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/walletlog")
public class WalletLogController {

    @Autowired
    WalletLogService walletLogService;

    @GetMapping("/bywallet/{id}")
    public ResponseEntity<?> getByWalletId(@PathVariable Long id) {
        ApiResponse apiResponse = walletLogService.getByWalletId(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<?> paged(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        PaginationResponse apiResponse = walletLogService.getPaginatedList(pageNo, pageSize);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }
}
