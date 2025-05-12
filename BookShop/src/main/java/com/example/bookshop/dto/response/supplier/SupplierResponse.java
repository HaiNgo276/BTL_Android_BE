package com.example.bookshop.dto.response.supplier;

import com.example.bookshop.entity.Supplier;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierResponse {
    private int count;
    private List<Supplier> rows;
}
