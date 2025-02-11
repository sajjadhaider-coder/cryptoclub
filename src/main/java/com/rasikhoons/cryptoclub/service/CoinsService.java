package com.rasikhoons.cryptoclub.service;

import com.rasikhoons.cryptoclub.Repository.CoinsRepo;
import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.CoinsDTO;
import com.rasikhoons.cryptoclub.model.Coins;
import com.rasikhoons.cryptoclub.response.CoinsResponse;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.response.PaginationResponse.PaginationDetail;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            Coins existingCoin = coinsRepo.findByCoinsName(coinsDTO.getCoinsName());
            if (existingCoin == null) {
                Coins coins = mapper.map(coinsDTO, Coins.class);
                coins = coinsRepo.save(coins);

                CoinsResponse coinsResponse = mapper.map(coins, CoinsResponse.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins create Successfully!", coinsResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Coins already Exist!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
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
                coins = Optional.of(coinsRepo.save(coins.get()));

                CoinsResponse coinsResponse = mapper.map(coins.get(), CoinsResponse.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins update Successfully!", coinsResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Coins not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
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
                CoinsResponse coinsResponse = mapper.map(coins.get(), CoinsResponse.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "Coins found Successfully!", coinsResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Coins not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return apiResponse;
        }
    }

    public PaginationResponse getPaginatedList(Integer pageNo, Integer pageSize) {
        PaginationResponse paginationResponse = null;
        List<CoinsResponse> coinPageDTO = new ArrayList<>();
        PageRequest coinPageRequest = PageRequest.of(pageNo, pageSize);
        Page<Coins> coinsPage = coinsRepo.findAll(coinPageRequest);
        List<Coins> coinsList = coinsPage.getContent();
        try {
            if (!coinsList.isEmpty()) {
                List<Coins> getCoinList = new ArrayList<>(coinsList);
                getCoinList.forEach(coin -> {
                    CoinsResponse coinsResponse = mapper.map(coin, CoinsResponse.class);
                    coinPageDTO.add(coinsResponse);
                });
                Long totalElements = coinsPage.getTotalElements();
                long totalPages = totalElements / pageSize;

                PaginationDetail paginationDetail = new PaginationDetail(pageNo, totalPages, pageSize,
                        totalElements);
                paginationResponse = new PaginationResponse(HttpStatus.OK.value(), "coin found Successfully!", coinPageDTO, paginationDetail);

            } else {
                paginationResponse = new PaginationResponse(HttpStatus.BAD_REQUEST.value(), "coin not found Successfully!", null, null);
            }
        } catch (Exception e) {
            paginationResponse = new PaginationResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null, null);
            return paginationResponse;
        }
        return paginationResponse;
    }
}
