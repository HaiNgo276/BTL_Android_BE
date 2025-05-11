package com.example.bookshop.repository;

import com.example.bookshop.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReceiverRepo extends JpaRepository<Receiver, Integer> {
    List<Receiver> findByCustomerIdOrderByIsDefaultDesc(int customerId);

    Receiver save(Receiver receiver);

    Receiver findById(int receiverId);

    @Transactional
    void deleteByIdAndCustomerId(int receiverId, int customerId);

    @Query("SELECT r from Receiver r where r.customer.id= :idCustomer and r.isDefault=1")
    Receiver getReceiverDefault(int idCustomer);

    @Query("SELECT r from Receiver r where r.customer.id= :idCustomer and r.isSelected=1")
    Receiver getReceiverSelected(int idCustomer);
}
