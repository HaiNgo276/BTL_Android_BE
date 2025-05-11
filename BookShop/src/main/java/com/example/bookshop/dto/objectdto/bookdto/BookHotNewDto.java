package com.example.bookshop.dto.objectdto.bookdto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookHotNewDto {
    private int product_id;
    private String name;
    private String description;
    private String price;
    private String discounted_price;
    private int quantity;
    private int quantitySold;
    private String thumbnail;
    private String supplierName;
}
