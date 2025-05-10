package com.example.bookshop.config.jwt;

import com.example.bookshop.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtUtil {

    private String secret = "your_secret_key";

    public String generateToken(Customer customer) {
        // Tạo JWT từ thông tin người dùng
        // Sử dụng thư viện JWT để tạo mã JWT và thêm thông tin người dùng
        return Jwts.builder()
                .setSubject(customer.getId() + "")
                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000)) // 10 giờ
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // Kiểm tra tính hợp lệ của token
        final String username = extractId(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();
            // Kiểm tra nếu thời gian hết hạn nhỏ hơn thời gian hiện tại
            if (expirationDate.before(currentDate)) {
                return false; // Token đã hết hạn
            }
            // Nếu thời gian hết hạn lớn hơn thời gian hiện tại, token còn hiệu lực
            return true;
        } catch (Exception e) {
            // Xử lý lỗi khi kiểm tra tính hợp lệ của access token
            return false; // Token không hợp lệ
        }
    }

    public UserDetails extractUserDetails(String token) {
        // Trích xuất thông tin người dùng từ JWT
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
    }

    public LocalDateTime extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();//Chuyển đổi đối tượng Instant thành LocalDateTime dựa trên múi giờ hệ thống mặc định của máy tính
    }

    public String extractId(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
