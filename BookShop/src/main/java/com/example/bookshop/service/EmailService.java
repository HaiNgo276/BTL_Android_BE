package com.example.bookshop.service;

import com.example.bookshop.entity.CartItem;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Order;
import com.example.bookshop.entity.Receiver;
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

    public void sendMailOrder(Receiver receiver, Customer customer, Order order, List<CartItem> cartItems) throws MessagingException, UnsupportedEncodingException {
        String subject = "Đơn hàng của bạn đã được đặt thành công!";
        String text = "<strong>Xin chào: <em>" + customer.getName() + ",</em> cảm ơn bạn vì đã đặt hàng. </strong>" +
                "<p>Thời gian đặt hàng: " + order.getCreateOn() + "</p>" +
                "<p>Tên người nhận hàng: <em>" + receiver.getReceiverName() + "</em></p>" +
                "<p>Số điện thoại người nhận: <em>" + receiver.getReceiverPhone() + "</em></p>" +
                "<p>Địa chỉ người nhận: <em>" + receiver.getAddress() + "</em></p>" +
                "<p style='font-weight: bold;'>SAU ĐÂY LÀ THÔNG TIN CHI TIẾT ĐƠN HÀNG CỦA BẠN:</p>" +
                "<table>" +
                "<tr>" +
                "<th>STT</th>" +
                "<th>Ảnh sản phẩm</th>" +
                "<th>Tên sản phẩm</th>" +
                "<th>Đơn giá</th>" +
                "<th>Số lượng</th>" +
                "</tr>";
        int index = 0;
        BigDecimal subTotal = new BigDecimal("0");
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        for (CartItem cartItem : cartItems) {
            index++;
            subTotal = subTotal.add(cartItem.getBook().getDiscounted_price().multiply(new BigDecimal(cartItem.getQuantity())));
            text += "<tr>" +
                    "<td width='50' height='175' style='text-align: center;'>" + index + "</td>" +
                    "<td width='200' height='175' style='text-align: center;'><img src='" + cartItem.getBook().getImage() + "' alt='" + cartItem.getBook().getName() + "' width='140' height='140'></td>" +
                    "<td width='500' height='175' style='text-align: center;'>" + cartItem.getBook().getName() + "</td>" +
                    "<td width='125' height='175' style='text-align: center;'>" + formatter.format(cartItem.getBook().getDiscounted_price()) + "</td>" +
                    "<td width='100' height='175' style='text-align: center;'>" + cartItem.getQuantity() + "</td>" +
                    "</tr>";
        }
        text += "</table>" +
                "<span style='font-weight: bold;'>Tổng tiền: " + formatter.format(subTotal) + "</span><br>" +
                "<span style='font-weight: bold;'>Phí vận chuyển: " + formatter.format(order.getShipping().getShippingCost()) + "</span><br>" +
                "<span style='font-weight: bold;'>Tổng thanh toán: " + formatter.format(subTotal.add(order.getShipping().getShippingCost())) + "</span></br>" +
                "<hr>" +
                "<span>Trân trọng,</span><br>" +
                "<span>Đội ngũ BookShop.</span>";
        String email = customer.getEmail();
        sendEmail(email, subject, text);
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
