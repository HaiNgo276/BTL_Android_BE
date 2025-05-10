package com.example.bookshop.service;

import com.example.bookshop.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CustomerService customerService;

    public void sendEmail(String to, String subject, String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // Đặt tham số thứ hai là true để cho phép nội dung là HTML

        // Đặt tên người gửi
        String senderName = "BookShop";
        String senderEmail = "bookshop@example.com";
        helper.setFrom(senderEmail, senderName);
        mailSender.send(message);
        System.out.println("Send email successful");
    }


    public void sendMailForgotPass(Customer customer, String email, PasswordEncoder bCryptPasswordEncoder, String newPass) throws MessagingException, UnsupportedEncodingException {
        String subject = "Mật khẩu mới trên hệ thống BookShop";
        String text = "<strong>Xin chào, <em>" + customer.getName() + "</em></strong>" +
                "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu BookShop của bạn.</p>" +
                "<p>Mật khẩu mới của bạn là: <strong>" + newPass + "</strong></p>" +
                "<p>Trân trọng,</p>" +
                "<p>Đội ngũ BookShop.</p>";
        sendEmail(email, subject, text);
        customer.setPassword(bCryptPasswordEncoder.encode(newPass));
        customerService.save(customer);
    }

    public void sendMailLockUser(Customer customer) throws MessagingException, UnsupportedEncodingException {
        String subject = "Thông báo từ hệ thống BookShop";
        String text = "<strong>Xin chào, <em>" + customer.getName() + "</em></strong>" +
                "<p>Chúng tôi lấy làm tiếc khi phải thông báo điều này tới bạn:</p>" +
                "<p>Do bạn đã vi phạm chính sách của chúng tôi, vì thế chúng tôi sẽ tạm khóa tài khoản của bạn.</p>" +
                "<p>Trân trọng,</p>" +
                "<p>Đội ngũ BookShop.</p>";
        sendEmail(customer.getEmail(), subject, text);
        customerService.save(customer);
    }

    public void sendMailUnLockUser(Customer customer) throws MessagingException, UnsupportedEncodingException {
        String subject = "Thông báo từ hệ thống BookShop";
        String text = "<strong>Xin chào, <em>" + customer.getName() + "</em></strong>" +
                "<p>Tài khoản BookShop của bạn đã được mở khóa. Bạn có thể vào hệ thống để mua sắm</p>" +
                "<p>Trân trọng,</p>" +
                "<p>Đội ngũ BookShop.</p>";
        sendEmail(customer.getEmail(), subject, text);
        customerService.save(customer);
    }

}
