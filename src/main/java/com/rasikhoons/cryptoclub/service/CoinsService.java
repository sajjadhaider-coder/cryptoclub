package com.rasikhoons.cryptoclub.service;

import com.rasikhoons.cryptoclub.Repository.CoinsRepo;
import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.CoinsDTO;
import com.rasikhoons.cryptoclub.model.Coins;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoinsService {

    @Autowired
    private CoinsRepo coinsRepo;

    @Autowired
    private ModelMapper mapper;

    @Transactional
    public ApiResponse save(CoinsDTO coinsDTO) {
        ApiResponse apiResponse;
        try {
            Coins coins = mapper.map(coinsDTO, Coins.class);
            coinsRepo.save(coins);
            apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins create Successfully!", null);

            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            //new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return apiResponse;
        }
    }

    @Transactional
    public ApiResponse update(CoinsDTO coinsDTO) {
        ApiResponse apiResponse;
        try {
            Optional<Coins> coins = coinsRepo.findById(coinsDTO.getId());
            if (coins.isPresent()) {
                if (coinsDTO.getCoinsName() != null && !coinsDTO.getCoinsName().isBlank()) {
                    coins.get().setCoinsName(coinsDTO.getCoinsName());
                }
                if (coinsDTO.getCurrentPrice() != null) {
                    coins.get().setCurrentPrice(coinsDTO.getCurrentPrice());
                }
                coinsRepo.save(coins.get());
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins update Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Coins not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            //new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return apiResponse;
        }
    }

    @Transactional
    public ApiResponse archive(long id) {
        ApiResponse apiResponse;
        try {
            Optional<Coins> coins = coinsRepo.findById(id);
            if (coins.isPresent()) {
                coinsRepo.delete(coins.get());
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins archive Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Coins not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            //new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return apiResponse;
        }
    }

    public ApiResponse getById(long id) {
        ApiResponse apiResponse;
        try {
            Optional<Coins> coins = coinsRepo.findById(id);
            if (coins.isPresent()) {
                CoinsDTO coinsDTO = mapper.map(coins.get(), CoinsDTO.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins found Successfully!", coinsDTO);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Coins not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            //new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return apiResponse;
        }
    }
}
