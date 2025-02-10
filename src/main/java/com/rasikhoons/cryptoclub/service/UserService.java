package com.rasikhoons.cryptoclub.service;

import com.rasikhoons.cryptoclub.Repository.UserRepo;
import com.rasikhoons.cryptoclub.dto.ApiResponse;
import com.rasikhoons.cryptoclub.dto.UserDTO;
import com.rasikhoons.cryptoclub.model.UserInfo;
import com.rasikhoons.cryptoclub.response.PaginationResponse;
import com.rasikhoons.cryptoclub.response.PaginationResponse.PaginationDetail;
import com.rasikhoons.cryptoclub.response.UserResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponse save(UserDTO userDTO) {
        ApiResponse apiResponse;
        try {
            UserInfo userInfo = userRepo.findByUserName(userDTO.getUserName());
            if (userInfo == null) {
                UserInfo user = mapper.map(userDTO, UserInfo.class);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                userRepo.save(user);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "user create Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "user name already exist! " + userDTO.getUserName(), null);
            }
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            //new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return apiResponse;
        }
    }

    @Transactional
    public ApiResponse update(UserDTO userDTO) {
        ApiResponse apiResponse;
        try {
            Optional<UserInfo> userInfo = userRepo.findById(userDTO.getId());
            if (userInfo.isPresent()) {
                UserInfo user = userInfo.get();
                mapper.map(userDTO, user);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                userRepo.save(user);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "user update Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "user not found!", null);
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
            Optional<UserInfo> userInfo = userRepo.findById(id);
            if (userInfo.isPresent()) {
                userRepo.delete(userInfo.get());
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "user archive Successfully!", null);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "user not found!", null);
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
            Optional<UserInfo> userInfo = userRepo.findById(id);
            if (userInfo.isPresent()) {
                UserResponse userResponse = mapper.map(userInfo.get(), UserResponse.class);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), "user found Successfully!", userResponse);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "user not found!", null);
            }
            return apiResponse;
        } catch (Exception e) {
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            //new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return apiResponse;
        }
    }

    public PaginationResponse getPaginatedList(Integer pageNo, Integer pageSize) {
        PaginationResponse paginationResponse = null;
        List<UserResponse> userPageDTO = new ArrayList<>();
        PageRequest userPageRequest = PageRequest.of(pageNo, pageSize);
        Page<UserInfo> userPage = userRepo.findAll(userPageRequest);
        List<UserInfo> userList = userPage.getContent();
        try {
            if (!userList.isEmpty()) {
                List<UserInfo> getUserList = new ArrayList<>(userList);
                getUserList.forEach(user -> {
                    UserResponse userResponse = mapper.map(user, UserResponse.class);
                    userPageDTO.add(userResponse);
                });
                Long totalElements = userPage.getTotalElements();
                long totalPages = totalElements / pageSize;

                PaginationDetail paginationDetail = new PaginationDetail(pageNo, totalPages, pageSize,
                        totalElements);
                paginationResponse = new PaginationResponse(HttpStatus.OK.value(), "user found Successfully!", userPageDTO, paginationDetail);

            } else {
                paginationResponse = new PaginationResponse(HttpStatus.BAD_REQUEST.value(), "user not found Successfully!", null, null);
            }
        } catch (Exception e) {
            paginationResponse = new PaginationResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null, null);
            return paginationResponse;
        }
        return paginationResponse;
    }
}
