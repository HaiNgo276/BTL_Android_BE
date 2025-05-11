package com.example.bookshop.dto.response.order;

import com.example.bookshop.dto.objectdto.orderdto.BookOrderDetailDto;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailResponse {
    private int order_id;
    private String merchandise_subtotal;
    private String created_on;
    private String shipped_on;
    private int customer_id;
    private String address;
    private String receiver_name;
    private String receiver_phone;
    private int shipping_id;
    private String shipping_type;
    private String shipping_cost;
    private String payment_method;
    private String order_status;
    private String order_total;
    List<BookOrderDetailDto> products;
}
