package com.rasikhoons.cryptoclub.controller;

import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.TransactionDTO;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TransactionDTO transactionDTO) {
        ApiResponse apiResponse = transactionService.save(transactionDTO);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        ApiResponse apiResponse = transactionService.archive(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ApiResponse apiResponse = transactionService.getById(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<PaginationResponse> getAllPagination(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        PaginationResponse paginationResponse = transactionService.getPaginatedList(pageNo, pageSize);
        if (paginationResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(paginationResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(paginationResponse, HttpStatusCode.valueOf(paginationResponse.getCode()));
        }
    }
}
