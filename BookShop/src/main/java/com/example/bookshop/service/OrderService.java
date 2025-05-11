package com.example.bookshop.service;

import com.example.bookshop.dto.objectdto.orderdto.OrderDto;
import com.example.bookshop.entity.Order;
import com.example.bookshop.repository.OrderDetailRepo;
import com.example.bookshop.repository.OrderRepo;
import com.example.bookshop.util.ConvetDateTimeUTC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderDetailRepo orderDetailRepo;

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> getAllByCustomerId(int customerId) {
        return orderRepo.getAllByCustomerIdOrderByCreateOnDesc(customerId);
    }

    public Order getOrderById(int orderId) {
        return orderRepo.getOrderById(orderId);
    }

    public List<OrderDto> getOrderByOrderStatus(int orderStatus) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders;
        if(orderStatus==1) {
            orders = orderRepo.getAllByOrderStatusIdOrderByCreateOnDesc(orderStatus);
        }else{
            orders = orderRepo.getAllByOrderStatusIdOrderByShippedOnDesc(orderStatus);
        }
        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setOrder_id(order.getId());
            orderDto.setMerchandise_subtotal(order.getTotalAmount() + "");
            orderDto.setTotal_quantity(orderDetailRepo.getAllByOrderId(order.getId()).size() + "");
            orderDto.setCreated_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getCreateOn()));
            if(order.getShippedOn()!=null) {
                orderDto.setShipped_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getShippedOn()));
            }
            orderDto.setCustomer_id(order.getCustomer().getId());
            orderDto.setAddress(order.getAddress());
            orderDto.setReceiver_name(order.getReceiverName());
            orderDto.setReceiver_phone(order.getReceiverPhone());
            orderDto.setShipping_id(order.getShipping().getId());
            orderDto.setShipping_type(order.getShipping().getShippingType());
            orderDto.setShipping_cost(order.getShipping().getShippingCost() + "");
            orderDto.setOrder_status(order.getOrderStatus().getStatus());
            orderDto.setOrder_total(order.getTotalAmount().add(order.getShipping().getShippingCost()) + "");
            orderDtos.add(orderDto);
        }
        return orderDtos;
//        return orderRepo.getAllByOrderStatusId(orderStatus);
    }
    public List<OrderDto> getOrderByTime(Date startTime, Date endTime) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<Order> orders = orderRepo.getOrderByTime(startTime, endTime);
        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setOrder_id(order.getId());
            orderDto.setMerchandise_subtotal(order.getTotalAmount() + "");
            orderDto.setTotal_quantity(orderDetailRepo.getAllByOrderId(order.getId()).size() + "");
            orderDto.setCreated_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getCreateOn()));
            orderDto.setCustomer_id(order.getCustomer().getId());
            orderDto.setAddress(order.getAddress());
            orderDto.setReceiver_name(order.getReceiverName());
            orderDto.setReceiver_phone(order.getReceiverPhone());
            orderDto.setShipping_id(order.getShipping().getId());
            orderDto.setShipping_type(order.getShipping().getShippingType());
            orderDto.setShipping_cost(order.getShipping().getShippingCost() + "");
            orderDto.setOrder_status(order.getOrderStatus().getStatus());
            orderDto.setOrder_total(order.getTotalAmount().add(order.getShipping().getShippingCost()) + "");
            orderDtos.add(orderDto);
        }
        return orderDtos;
//        return orderRepo.getOrderByTime(startTime, endTime);
    }

}
