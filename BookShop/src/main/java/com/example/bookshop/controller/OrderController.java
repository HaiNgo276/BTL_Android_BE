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
    private ReceiverService receiverService;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private PaymentService paymentService;
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

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestHeader("user-key") String userKey,
                                         @RequestParam("cart_id") String cartId,
                                         @RequestParam("shipping_id") int shippingId,
                                         @RequestParam("payment_id") int paymentId,
                                         @RequestParam("receiver_id") int receiverId) throws MessagingException, UnsupportedEncodingException {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            List<CartItem> cartItems = cartItemService.getAllByCustomerId(customerId);
            if (cartItems.size() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "CART_02", "Giỏ hàng của bạn không có sản phẩm để thanh toán!", ""));
            } else {
                BigDecimal subTotal = new BigDecimal("0");
                for (CartItem cartItem : cartItems) {
                    subTotal = subTotal.add(cartItem.getBook().getDiscounted_price().multiply(new BigDecimal(cartItem.getQuantity())));
                }
                Customer customer = customerService.findById(customerId);
                Receiver receiver = receiverService.findById(receiverId);
                if (receiver == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(404, "RECEIVER_02", "Không tìm thấy người nhận!", "RECEIVER"));
                } else {
                    Order order = new Order();
                    OrderStatus orderStatus = new OrderStatus();
                    orderStatus.setId(1);
                    orderStatus.setStatus("Đang được chuẩn bị");
                    order.setCreateOn(new Date());
                    order.setAddress(receiver.getAddress());
                    order.setReceiverName(receiver.getReceiverName());
                    order.setReceiverPhone(receiver.getReceiverPhone());
                    order.setShipping(shippingService.findById(shippingId));
                    order.setTotalAmount(subTotal);
                    order.setOrderStatus(orderStatus);
                    order.setCustomer(customer);
                    order.setPayment(paymentService.getPaymentById(paymentId));
                    emailService.sendMailOrder(receiver, customer, order, cartItems);
                    orderService.save(order);
                    orderDetailService.save(order, cartItems);
                    cartItemService.emptyCart(cartId);
                    return ResponseEntity.ok(new Message("Đặt hàng thành công"));
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @GetMapping("/status/{orderStatusId}")
    public ResponseEntity<?> getAllOrderByOrderStatus(@PathVariable("orderStatusId") int orderStatusId) {
        List<OrderDto> orders = orderService.getOrderByOrderStatus(orderStatusId);
        return ResponseEntity.ok(new OrderResponse(orders.size(), orders));
    }

    @GetMapping("/detail/{order_id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("order_id") int orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getAllByOrderId(orderId);
        Order order = orderService.getOrderById(orderId);
        OrderDetailResponse response = new OrderUtil().addToOrderDetail(order, orderDetails, 0, null);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/status")
    public ResponseEntity<?> updateOrderStatus(@RequestParam("orderId") int orderId,
                                               @RequestParam("orderStatusId") int orderStatusId) {
        Order order = orderService.getOrderById(orderId);
        OrderStatus orderStatus = orderStatusService.findById(orderStatusId);
        if(orderStatusId==4){
            List<OrderDetail> orderDetails=orderDetailService.getAllByOrderId(orderId);
            for(OrderDetail orderDetail:orderDetails){
                Book book=productService.findById(orderDetail.getBook().getId());
                book.setQuantitySold(book.getQuantitySold()-orderDetail.getQuantity());
                productService.addBook(book);
            }
        }
        order.setOrderStatus(orderStatus);
        order.setShippedOn(new Date());
        orderService.save(order);
        return ResponseEntity.ok(new Message("Đã cập nhật trạng thái đơn hàng thành công"));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<?> getAllOrderByYear(@PathVariable("year") int year) throws ParseException {
        SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date start = smf.parse("01/01/" + year + " 00:00:00");
        Date end = smf.parse("31/12/" + year + " 23:59:59");
        List<OrderDto> orders = orderService.getOrderByTime(start, end);
        return ResponseEntity.ok(new OrderResponse(orders.size(), orders));
    }

    @GetMapping("/monthOfYear")
    public ResponseEntity<?> getAllOrderByMonthOfYear(@RequestParam("year") int year,
                                                      @RequestParam("month") int month) throws ParseException {
        SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date start = smf.parse("01/" + month + "/" + year + " 00:00:00");
        Date end = smf.parse("31/" + month + "/" + year + " 23:59:59");
        List<OrderDto> orders = orderService.getOrderByTime(start, end);
        return ResponseEntity.ok(new OrderResponse(orders.size(), orders));
    }

    @GetMapping("/today")
    public ResponseEntity<?> getAllOrderByToday(@RequestParam("today") String today) throws ParseException {
        SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date start = smf.parse(today + " 00:00:00");
        Date end = smf.parse(today + " 23:59:59");
        List<OrderDto> orders = orderService.getOrderByTime(start, end);
        return ResponseEntity.ok(new OrderResponse(orders.size(), orders));
    }
}
