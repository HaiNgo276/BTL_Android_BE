package com.example.bookshop.service;

import com.example.bookshop.entity.CartItem;
import com.example.bookshop.entity.Order;
import com.example.bookshop.entity.OrderDetail;
import com.example.bookshop.repository.OrderDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepo orderDetailRepo;
    @Autowired
    private ProductService productService;

    public void save(Order order, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setBook(cartItem.getBook());
            orderDetail.setBookName(cartItem.getBook().getName());
            orderDetail.setUniCost(cartItem.getBook().getDiscounted_price());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetailRepo.save(orderDetail);
        }
    }

    public List<OrderDetail> getAllByCustomerId(int customerId){
        return orderDetailRepo.getAllByOrderCustomerId(customerId);
    }
    public List<OrderDetail> getAllByOrderId(int orderId){
        return orderDetailRepo.getAllByOrderId(orderId);
    }
}
