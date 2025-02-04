package com.example.TwoFactorAuthentication.service;


import com.example.TwoFactorAuthentication.dao.RoleRepository;
import com.example.TwoFactorAuthentication.dao.UserRepository;
import com.example.TwoFactorAuthentication.dto.UserRegisteredDTO;
import com.example.TwoFactorAuthentication.models.Role;
import com.example.TwoFactorAuthentication.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultUserServiceImpl implements DefaultUserService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    JavaMailSender javaMailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public User save(UserRegisteredDTO userRegisteredDTO) {
        Role role = roleRepo.findByRole("USER");

        User user = new User();
        user.setEmail(userRegisteredDTO.getEmail_id());
        user.setName(userRegisteredDTO.getName());
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setRole(role);

        return userRepo.save(user);
    }

    @Override
    public String generateOtp(User user) {
        try {
            int randomPIN = (int) (Math.random() * 9000) + 1000;
            user.setOtp(randomPIN);
            user.setActive(false);
            userRepo.save(user);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("");// input the senders email ID
            msg.setTo(user.getEmail());

            msg.setSubject("Welcome To My Channel");
            msg.setText("Hello \n\n" +"Your Login OTP :" + randomPIN + ".Please Verify. \n\n"+"Regards \n"+"ABC");

            javaMailSender.send(msg);

            return "success";
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}