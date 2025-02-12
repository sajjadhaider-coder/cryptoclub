package com.rasikhoons.cryptoclub.service;

import com.rasikhoons.cryptoclub.Repository.WalletRepo;
import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.WalletDTO;
import com.rasikhoons.cryptoclub.model.Wallet;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.response.WalletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private WalletRepo walletRepo;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public ApiResponse save(WalletDTO walletDTO) {
        ApiResponse apiResponse;
        try {
            Wallet wallet = mapper.map(walletDTO, Wallet.class);
            if (walletDTO.getFile() != null) {
                wallet.setTransferAmount(uploadFile(walletDTO.getFile()));
            }
            wallet = walletRepo.save(wallet);

            WalletResponse walletResponse = mapper.map(wallet, WalletResponse.class);
            apiResponse = new ApiResponse(HttpStatus.OK.value(), "wallet save Successfully!", walletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
        return apiResponse;
    }

    @Transactional
    public ApiResponse update(WalletDTO walletDTO) {
        ApiResponse apiResponse;
        try {
            Optional<Wallet> wallet = walletRepo.findById(walletDTO.getId());
            if (wallet.isPresent()) {
                mapper.map(walletDTO, wallet.get());
                wallet.get().setLastedUpdated(LocalDateTime.now());
                walletRepo.save(wallet.get());
                WalletResponse walletResponse = mapper.map(wallet.get(), WalletResponse.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "wallet update Successfully!", walletResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "wallet not found!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
        return apiResponse;
    }

    public ApiResponse archive(Long id) {
        ApiResponse apiResponse;
        try {
            Optional<Wallet> wallet = walletRepo.findById(id);
            if (wallet.isPresent()) {
                walletRepo.delete(wallet.get());

                apiResponse = new ApiResponse(HttpStatus.OK.value(), "wallet delete Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "wallet not found!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
        return apiResponse;
    }

    public ApiResponse getById(Long id) {
        ApiResponse apiResponse;
        try {
            Optional<Wallet> wallet = walletRepo.findById(id);
            if (wallet.isPresent()) {
                WalletResponse walletResponse = mapper.map(wallet.get(), WalletResponse.class);

                apiResponse = new ApiResponse(HttpStatus.OK.value(), "wallet found!", walletResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "wallet not found!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
        return apiResponse;
    }

    public PaginationResponse getPaginatedList(Integer pageNo, Integer pageSize) {
        PaginationResponse paginationResponse = null;
        List<WalletResponse> walletPageDTO = new ArrayList<>();
        PageRequest walletPageRequest = PageRequest.of(pageNo, pageSize);
        Page<Wallet> walletPage = walletRepo.findAll(walletPageRequest);
        List<Wallet> walletList = walletPage.getContent();
        try {
            if (!walletList.isEmpty()) {
                List<Wallet> getWalletList = new ArrayList<>(walletList);
                getWalletList.forEach(wallet -> {
                    WalletResponse walletResponse = mapper.map(wallet, WalletResponse.class);
                    walletPageDTO.add(walletResponse);
                });
                Long totalElements = walletPage.getTotalElements();
                long totalPages = totalElements / pageSize;

                PaginationResponse.PaginationDetail paginationDetail = new PaginationResponse.PaginationDetail(pageNo, totalPages, pageSize,
                        totalElements);
                paginationResponse = new PaginationResponse(HttpStatus.OK.value(), "wallet found Successfully!", walletPageDTO, paginationDetail);

            } else {
                paginationResponse = new PaginationResponse(HttpStatus.BAD_REQUEST.value(), "wallet not found Successfully!", null, null);
            }
        } catch (Exception e) {
            paginationResponse = new PaginationResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null, null);
            return paginationResponse;
        }
        return paginationResponse;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique filename to avoid overwriting
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = System.currentTimeMillis() + extension;

        // Create upload directory if it doesn't exist
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Save the file to the directory
        Path path = Paths.get(uploadDir, uniqueFilename);
        Files.write(path, file.getBytes());

        // Store the file URL in the database (relative to the server)
        String fileUrl = uploadDir + uniqueFilename;

        return fileUrl;
    }
}
