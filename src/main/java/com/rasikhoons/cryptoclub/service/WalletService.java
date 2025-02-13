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
                wallet.setTransferAmount(uploadFile(walletDTO.getFile(), walletDTO.getUserId()));
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
                if (walletDTO.getFile() != null) {
                    fileDelete(wallet.get().getTransferAmount());
                    wallet.get().setTransferAmount(uploadFile(walletDTO.getFile(), walletDTO.getUserId()));
                }
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
                if (wallet.get().getTransferAmount() != null && !wallet.get().getTransferAmount().isBlank()) {
                    fileDelete(wallet.get().getTransferAmount());
                }
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

    public String uploadFile(MultipartFile file, Long userId) throws IOException {
        // Generate a unique filename to avoid overwriting
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = System.currentTimeMillis() + "–" + userId + extension;

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

//    public String fileUpdate(MultipartFile newFile, Long userId) throws IOException {
//        List<String> fileList = getAllFiles(uploadDir);
//        fileList.forEach(file -> {
//            fileDelete(uploadDir.concat(fileName));
//        });
//        return uploadFile(newFile, userId);
//    }

    public void fileDelete(String filePath) {
        // Specify the path of the file to be deleted
        File file = new File(filePath);
        // Check if the file exists and delete it
        if (file.exists()) {
            file.delete();
        }
    }

//    public List<String> getAllFiles(String directoryPath) {
//        // Specify the directory path
//        File directory = new File(directoryPath);
//        List<String> fileName = new ArrayList<>();
//        // Get all files and directories in the specified directory
//        if (directory.exists() && directory.isDirectory()) {
//            File[] files = directory.listFiles();
//
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isFile()) {
//                        fileName.add(file.getName());
//                        // Print file names
//                        System.out.println("File: " + file.getName());
//                    }
//                }
//            }
//        }
//        return fileName;
//    }

//    public void fileExistCheck(String fileName, Long userId) {
//        String[] getFileName = fileName.split("–");
//        System.out.println(getFileName);
//        // Check if the split was successful and extract the value
//        String value = getFileName[1];  // This should be the part after '-'
//        fileDelete(uploadDir.concat(fileName));
//        System.out.println("Value after '-' is: " + value);
//    }
}
