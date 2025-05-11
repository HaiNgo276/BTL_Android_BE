package com.example.bookshop.util;

import com.example.bookshop.dto.objectdto.orderdto.BookOrderDetailDto;
import com.example.bookshop.dto.objectdto.orderdto.OrderDto;
import com.example.bookshop.dto.response.order.OrderDetailResponse;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Order;
import com.example.bookshop.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderUtil {
    public List<OrderDto> addToOrderDtoByCustomer(List<Order> orders, int customerId, List<OrderDetail> orderDetails) {
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setOrder_id(order.getId());
            orderDto.setMerchandise_subtotal(order.getTotalAmount() + "");
            int totalQuantity = 0;
            for (OrderDetail orderDetail : orderDetails) {
                if (order.getId() == orderDetail.getOrder().getId()) {
                    totalQuantity++;
                }
            }
            orderDto.setTotal_quantity(totalQuantity + "");
            orderDto.setCreated_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getCreateOn()));
            if(order.getShippedOn()!=null){
                orderDto.setShipped_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getShippedOn()));
            }
            orderDto.setCustomer_id(customerId);
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
    }

    public OrderDetailResponse addToOrderDetail(Order order, List<OrderDetail> orderDetails, int customerId, List<Book> books) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrder_id(order.getId());
        response.setMerchandise_subtotal(order.getTotalAmount() + "");
        response.setCreated_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getCreateOn()));
        response.setCustomer_id(customerId);
        if (order.getShippedOn() != null) {
            response.setShipped_on(new ConvetDateTimeUTC().convertDateTimeUTC(order.getShippedOn()));
        }
        response.setAddress(order.getAddress());
        response.setReceiver_name(order.getReceiverName());
        response.setReceiver_phone(order.getReceiverPhone());
        response.setShipping_id(order.getShipping().getId());
        response.setShipping_type(order.getShipping().getShippingType());
        response.setShipping_cost(order.getShipping().getShippingCost() + "");
        response.setOrder_status(order.getOrderStatus().getStatus());
        response.setPayment_method(order.getPayment().getPaymentMethod());
        response.setOrder_total(order.getTotalAmount().add(order.getShipping().getShippingCost()) + "");
        List<BookOrderDetailDto> bookOrderDetailDtos = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            BookOrderDetailDto bookOrderDetailDto = new BookOrderDetailDto();
            bookOrderDetailDto.setOrder_id(order.getId());
            bookOrderDetailDto.setProduct_id(orderDetail.getBook().getId());
            bookOrderDetailDto.setProduct_name(orderDetail.getBookName());
            bookOrderDetailDto.setProduct_description(orderDetail.getBook().getDescription());
            bookOrderDetailDto.setImage(orderDetail.getBook().getImage());
            bookOrderDetailDto.setQuantity(orderDetail.getQuantity());
            bookOrderDetailDto.setUnit_cost(orderDetail.getUniCost() + "");
            bookOrderDetailDto.setSubtotal(orderDetail.getUniCost().multiply(new BigDecimal(orderDetail.getQuantity())) + "");
            int wishList = 0;
            if (books != null) {
                for (Book book : books) {
                    if (orderDetail.getBook().getId() == book.getId()) {
                        wishList = 1;
                    }
                }
                bookOrderDetailDto.setWishlist(wishList);
            }
            bookOrderDetailDtos.add(bookOrderDetailDto);
        }
        response.setProducts(bookOrderDetailDtos);
        return response;
    }
}
