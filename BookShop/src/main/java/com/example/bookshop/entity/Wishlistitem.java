package com.example.bookshop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "wishlist_item")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wishlistitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private WishList wishList;
}
