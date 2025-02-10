package com.rasikhoons.cryptoclub.service;

import com.rasikhoons.cryptoclub.Repository.TransactionRepo;
import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.TransactionDTO;
import com.rasikhoons.cryptoclub.model.Transactions;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.response.PaginationResponse.PaginationDetail;
import com.rasikhoons.cryptoclub.response.TransactionResponse;
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
public class TransactionService {

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private TransactionRepo transactionRepo;

    @Transactional
    public ApiResponse save(TransactionDTO transactionDTO) {
        ApiResponse apiResponse;
        try {
            Transactions transaction = mapper.map(transactionDTO, Transactions.class);
            transactionRepo.save(transaction);

            apiResponse = new ApiResponse(HttpStatus.OK.value(), "transaction create Successfully!", null);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return apiResponse;
        }
    }

    @Transactional
    public ApiResponse archive(long id) {
        ApiResponse apiResponse;
        try {
            Optional<Transactions> transaction = transactionRepo.findById(id);
            if (transaction.isPresent()) {
                transactionRepo.delete(transaction.get());
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "transaction archive Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "transaction not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return apiResponse;
        }
    }

    public ApiResponse getById(long id) {
        ApiResponse apiResponse;
        try {
            Optional<Transactions> transaction = transactionRepo.findById(id);
            if (transaction.isPresent()) {
                TransactionResponse transactionResponse = mapper.map(transaction.get(), TransactionResponse.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "transaction found Successfully!", transactionResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "transaction not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return apiResponse;
        }
    }

    public PaginationResponse getPaginatedList(Integer pageNo, Integer pageSize) {
        PaginationResponse paginationResponse = null;
        List<TransactionResponse> transactionPageDTO = new ArrayList<>();
        PageRequest transactionPageRequest = PageRequest.of(pageNo, pageSize);
        Page<Transactions> transactionPage = transactionRepo.findAll(transactionPageRequest);
        List<Transactions> transactionsList = transactionPage.getContent();
        try {
            if (!transactionsList.isEmpty()) {
                List<Transactions> gettransactionList = new ArrayList<>(transactionsList);
                gettransactionList.forEach(transaction -> {
                    TransactionResponse transactionRes = mapper.map(transaction, TransactionResponse.class);
                    transactionPageDTO.add(transactionRes);
                });
                Long totalElements = transactionPage.getTotalElements();
                long totalPages = totalElements / pageSize;

                PaginationDetail paginationDetail = new PaginationDetail(pageNo, totalPages, pageSize,
                        totalElements);
                paginationResponse = new PaginationResponse(HttpStatus.OK.value(), "transaction found Successfully!", transactionPageDTO, paginationDetail);

            } else {
                paginationResponse = new PaginationResponse(HttpStatus.BAD_REQUEST.value(), "transaction not found Successfully!", null, null);
            }
        } catch (Exception e) {
            paginationResponse = new PaginationResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null, null);
            return paginationResponse;
        }
        return paginationResponse;
    }
}
