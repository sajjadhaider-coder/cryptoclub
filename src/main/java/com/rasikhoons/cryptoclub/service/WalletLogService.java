package com.rasikhoons.cryptoclub.service;

import com.rasikhoons.cryptoclub.Repository.WalletLogRepo;
import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.model.WalletLog;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.response.PaginationResponse.PaginationDetail;
import com.rasikhoons.cryptoclub.response.WalletLogResponse;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletLogService {

    @Autowired
    WalletLogRepo walletLogRepo;
    @Autowired
    private ModelMapper mapper;

    @Transactional
    public void  saveLogs(Long id, BigDecimal balance){
        WalletLog walletLog = new WalletLog();
        walletLog.setWalletId(id);
        walletLog.setBalance(balance);
        walletLog.setCreatedAt(LocalDateTime.now());

        walletLogRepo.save(walletLog);
    }

    public void deleteLog(Long id) {

        walletLogRepo.deleteAllByWalletId(id);
    }

    public ApiResponse getByWalletId(Long walletId) {
        ApiResponse apiResponse;
        try {
            List<WalletLog> walletLogList = walletLogRepo.findByWalletIdOrderByCreatedAtDesc(walletId);
            if (!walletLogList.isEmpty()) {
                List<WalletLogResponse> walletLogResponseList = walletLogList.stream().map(walletLog -> {
                    WalletLogResponse walletResponse = mapper.map(walletLog, WalletLogResponse.class);
                    return walletResponse;
                }).collect(Collectors.toList());

                apiResponse = new ApiResponse(HttpStatus.OK.value(), "wallet Logs found!", walletLogResponseList);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "wallet Logs not found!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
        return apiResponse;
    }

    public PaginationResponse getPaginatedList(Integer pageNo, Integer pageSize) {
        PaginationResponse paginationResponse = null;
        List<WalletLogResponse> walletLogPageList = new ArrayList<>();
        PageRequest walletLogPageRequest = PageRequest.of(pageNo, pageSize);
        Page<WalletLog> walletLogPage = walletLogRepo.findAll(walletLogPageRequest);
        List<WalletLog> walletLogList = walletLogPage.getContent();
        try {
            if (!walletLogList.isEmpty()) {
                List<WalletLog> getWalletLogList = new ArrayList<>(walletLogList);
                getWalletLogList.forEach(walletLog -> {
                    WalletLogResponse walletResponse = mapper.map(walletLog, WalletLogResponse.class);
                    walletLogPageList.add(walletResponse);
                });
                Long totalElements = walletLogPage.getTotalElements();
                long totalPages = totalElements / pageSize;

                PaginationDetail paginationDetail = new PaginationDetail(pageNo, totalPages, pageSize,
                        totalElements);
                paginationResponse = new PaginationResponse(HttpStatus.OK.value(), "wallet Logs found Successfully!", walletLogPageList, paginationDetail);

            } else {
                paginationResponse = new PaginationResponse(HttpStatus.BAD_REQUEST.value(), "wallet Logs not found Successfully!", null, null);
            }
        } catch (Exception e) {
            paginationResponse = new PaginationResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null, null);
            return paginationResponse;
        }
        return paginationResponse;
    }
}
