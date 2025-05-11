package com.example.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "shipping")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "shipping_type", columnDefinition = "VARCHAR(100)")
    private String shippingType;
    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;
    @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;
}
