package com.example.bookshop.dto.objectdto.orderdto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookOrderDetailDto {
    private int order_id;
    private int product_id;
    private String product_name;
    private String product_description;
    private String image;
    private int quantity;
    private String unit_cost;
    private String subtotal;
    private int wishlist;
}
