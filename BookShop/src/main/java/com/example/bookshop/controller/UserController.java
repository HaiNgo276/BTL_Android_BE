package com.example.bookshop.controller;

import com.example.bookshop.config.jwt.JwtUtil;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.customer.CustomerResponse;
import com.example.bookshop.entity.*;
import com.example.bookshop.service.*;
import com.example.bookshop.util.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/all")
    public ResponseEntity<?> getUserNumber() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PutMapping("update/status")
    public ResponseEntity<?> updateStatus(@RequestParam("idUser") int idUser, @RequestParam("status") String status) throws MessagingException, UnsupportedEncodingException {
        Customer customer = customerService.findById(idUser);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "USR_04", "Không tìm thấy tài khoản", ""));
        } else {
            customer.setStatus(status);
            if (status.equals("inactive")) {
                emailService.sendMailLockUser(customer);
                return ResponseEntity.ok(new Message("Đã khóa tài khoản khách hàng!"));
            } else {
                emailService.sendMailUnLockUser(customer);
                return ResponseEntity.ok(new Message("Đã mở khóa tài khoản khách hàng!"));
            }
        }
    }

    @PostMapping("/update/avatar")
    public ResponseEntity<?> changeAvatar(@RequestHeader("user-key") String userKey, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            String imageURL = customerService.uploadFile(multipartFile, "customer");
            int id = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer", "")));
            Customer customer = customerService.getCustomer(id);
            customer.setAvatar(imageURL.replace("http", "https"));
            customerService.save(customer);
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PostMapping("/changePass")
    public ResponseEntity<?> changePass(@RequestParam("email") String email, @RequestParam("old_password") String old_password, @RequestParam("new_password") String new_password) {
        Customer customer = customerService.findByEmail(email);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(404, "USR_04", "Không tìm thấy tài khoản của bạn", "email"));
        } else {
            if (!bCryptPasswordEncoder.matches(old_password, customer.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                Error error = new Error(400, "USR_01", "Mật khẩu cũ không chính xác!", "password");
                response.put("error", error);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            } else {
                customer.setPassword(bCryptPasswordEncoder.encode(new_password));
                customerService.save(customer);
                return ResponseEntity.ok(customer);
            }
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getCustomer(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int id = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            return ResponseEntity.ok(customerService.getCustomer(id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateCustomer(@RequestHeader("user-key") String userKey, @RequestParam String name, @RequestParam String address, @RequestParam String date_of_birth, @RequestParam String gender, @RequestParam String mob_phone) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int id = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Customer customer = customerService.getCustomer(id);
            String[] date = date_of_birth.split("[-/]");
            try {
                customer.setName(name);
                customer.setAddress(address);
                LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                customer.setDateOfBirth(dateOfBirth);
                customer.setGender(gender);
                customer.setMobPhone(mob_phone);
                if (new PhoneNumberValidator().isValidPhoneNumber(mob_phone)) {
                    customerService.save(customer);
                    return ResponseEntity.ok(customer);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "PHONE_01", "Số điện thoại không đúng định dạng", "receiver_phone"));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new Error(400, "USR_02", "Nhập đúng định dạng ngày sinh!", "date_of_birth"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }
}
