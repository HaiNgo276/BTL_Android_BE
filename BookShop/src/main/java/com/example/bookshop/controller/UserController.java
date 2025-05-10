package com.example.bookshop.controller;

import com.example.bookshop.config.jwt.JwtUtil;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.customer.CustomerResponse;
import com.example.bookshop.entity.*;
import com.example.bookshop.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(path = "/customers")
@AllArgsConstructor
@Slf4j
@ControllerAdvice
public class UserController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("")
    public ResponseEntity<?> register(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password) {
        if (customerService.isEmailExists(email)) {
            Map<String, Object> response = new HashMap<>();
            Error error = new Error(409, "USR_04", "Email này đã tồn tại trong hệ thống!", "email");
            response.put("error", error);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setName(name);
            customer.setPassword(bCryptPasswordEncoder.encode(password));
            customer.setRole("user");
            customer.setStatus("active");
            String accessToken = jwtUtil.generateToken(customer);
            customerService.save(customer);
            CustomerResponse response = new CustomerResponse("Bearer " + accessToken, customer, "15 days");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        Customer customer = customerService.findByEmail(email);
        if (customer == null) {
            Map<String, Object> response = new HashMap<>();
            Error error = new Error(404, "USR_05", "Email không tồn tài trong hệ thống!", "email");
            response.put("error", error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                String accessToken = jwtUtil.generateToken(customer);
                LocalDateTime expiredTime = jwtUtil.extractExpiration(accessToken);
                long expiresIn = ChronoUnit.HOURS.between(LocalDateTime.now().with(ChronoField.MILLI_OF_SECOND, 0), expiredTime);
                CustomerResponse response = new CustomerResponse("Bearer " + accessToken, customer, expiresIn + " hours");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                Error error = new Error(400, "USR_05", "Sai tên tài khoản hoặc mật khẩu", "");
                response.put("error", error);
                return ResponseEntity.badRequest().body(response);
            }
        }
    }


    @PostMapping("/forgotPass")
    public ResponseEntity<?> forgotPass(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        Customer customer = customerService.findByEmail(email);
        Map<String, Object> response = new HashMap<>();
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder newPass = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(charset.length());
            char randomChar = charset.charAt(index);
            newPass.append(randomChar);
        }
        if (customer == null) {
            Error error = new Error(404, "USR_05", "Email không tồn tại trong hệ thống!", "email");
            response.put("error", error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            emailService.sendMailForgotPass(customer, email, bCryptPasswordEncoder, newPass + "");
            response.put("message", "Đã gửi mật khẩu mới thông qua email của bạn!");
            return ResponseEntity.ok(response);
        }
    }

}
