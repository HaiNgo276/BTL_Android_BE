package com.example.bookshop.dto.objectdto.cartdto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto {
    private int item_id;
    private String name;
    private String price;
    private int quantity;
    private int product_id;
    private String sub_total;
    private String added_on;
    private String discounted_price;
    private int wishlist;
    private String image;
}
