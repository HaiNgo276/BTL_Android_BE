package com.example.bookshop.dto.objectdto.wishlistdto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WishListItemDto {
    private int product_id;
    private String name;
    private String description;
    private String price;
    private String discounted_price;
    private int quantity;
    private String thumbnail;
}
