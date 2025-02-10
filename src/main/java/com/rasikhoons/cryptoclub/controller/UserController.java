package com.rasikhoons.cryptoclub.controller;

import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.UserDTO;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO) {
        ApiResponse apiResponse = userService.save(userDTO);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        ApiResponse apiResponse = userService.update(userDTO);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        ApiResponse apiResponse = userService.archive(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ApiResponse apiResponse = userService.getById(id);
        if (apiResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode()));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<PaginationResponse> getAllPagination(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        PaginationResponse paginationResponse = userService.getPaginatedList(pageNo, pageSize);
        if (paginationResponse.getCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(paginationResponse, HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            return new ResponseEntity<>(paginationResponse, HttpStatusCode.valueOf(paginationResponse.getCode()));
        }
    }
}
