package com.example.bookshop.dto.response.order;

import com.example.bookshop.dto.objectdto.orderdto.OrderDto;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private int count;
    private List<OrderDto> orders;
}
