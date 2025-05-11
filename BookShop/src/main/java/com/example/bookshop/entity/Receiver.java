package com.example.bookshop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "receiver")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receiver {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone", columnDefinition = "VARCHAR(10)")
    private String receiverPhone;
    @Column(name = "address")
    private String address;
    @Column(name = "is_default")
    private int isDefault;
    @Column(name = "is_selected")
    private int isSelected;
    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
}
