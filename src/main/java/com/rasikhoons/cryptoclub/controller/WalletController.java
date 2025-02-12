package com.rasikhoons.cryptoclub.controller;

import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.WalletDTO;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<?> save(@RequestParam Long userId, @RequestParam BigDecimal balance, @RequestPart MultipartFile file) {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setUserId(userId);
        walletDTO.setBalance(balance);
        walletDTO.setFile(file);
        ApiResponse apiResponse = walletService.save(walletDTO);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestParam Long userId, @RequestParam BigDecimal balance, @RequestPart MultipartFile file) {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setId(id);
        walletDTO.setUserId(userId);
        walletDTO.setBalance(balance);
        walletDTO.setFile(file);
        ApiResponse apiResponse = walletService.update(walletDTO);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<?> archive(@PathVariable Long id) {
        ApiResponse apiResponse = walletService.archive(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ApiResponse apiResponse = walletService.getById(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<?> paged(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        PaginationResponse apiResponse = walletService.getPaginatedList(pageNo, pageSize);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }
}
