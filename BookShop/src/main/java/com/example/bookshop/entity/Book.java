package com.example.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "book")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "name", columnDefinition = "VARCHAR(100)")
    private String name;

    @NotNull
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "discounted_price", precision = 10, scale = 2)
    private BigDecimal discounted_price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "quantity_sold")
    private int quantitySold;

    @Column(name = "image", columnDefinition = "VARCHAR(255)")
    private String image;

    @Column(name = "thumbnail", columnDefinition = "VARCHAR(255)")
    private String thumbnail;

    @Column(name = "banner_url")
    private String banner;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CartItem> cartItems;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wishlistitem> wishlistitems;
}
