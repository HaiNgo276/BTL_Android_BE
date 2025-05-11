package com.example.bookshop.controller;

import com.example.bookshop.config.jwt.JwtUtil;
import com.example.bookshop.dto.objectdto.orderdto.OrderDto;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.order.OrderDetailResponse;
import com.example.bookshop.dto.response.order.OrderResponse;
import com.example.bookshop.entity.*;
import com.example.bookshop.service.*;
import com.example.bookshop.util.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/orders")
@AllArgsConstructor
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private WishListItemService wishListItemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<?> getOrder(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            List<OrderDetail> orderDetails = orderDetailService.getAllByCustomerId(customerId);
            List<Order> orders = orderService.getAllByCustomerId(customerId);
            List<OrderDto> orderDtos = new OrderUtil().addToOrderDtoByCustomer(orders, customerId, orderDetails);
            return ResponseEntity.ok(new OrderResponse(orderDtos.size(), orderDtos));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrderInfoByCustomer(@RequestHeader("user-key") String userKey,
                                                    @PathVariable("order_id") int orderId) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            List<OrderDetail> orderDetails = orderDetailService.getAllByOrderId(orderId);
            Order order = orderService.getOrderById(orderId);
            List<Book> books = wishListItemService.getAllBooksInWishlist(customerId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "ORDER_01", "Không có đơn hàng chi tiết cho đơn hàng này!", "orderId"));
            } else {
                OrderDetailResponse response = new OrderUtil().addToOrderDetail(order, orderDetails, customerId, books);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }



}
