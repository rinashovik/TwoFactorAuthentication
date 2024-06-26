package com.example.TwoFactorAuthentication.service;


import com.example.TwoFactorAuthentication.dto.UserRegisteredDTO;
import com.example.TwoFactorAuthentication.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DefaultUserService extends UserDetailsService {

    User save(UserRegisteredDTO userRegisteredDTO);

    String generateOtp(User user);




}