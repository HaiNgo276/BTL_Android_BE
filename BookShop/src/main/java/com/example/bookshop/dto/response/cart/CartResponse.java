package com.example.bookshop.dto.response.cart;

import com.example.bookshop.dto.objectdto.bookdto.BookInCartDto;
import lombok.*;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponse {
    private String cart_id;
    private List<BookInCartDto> products;
}
