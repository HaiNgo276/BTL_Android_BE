package com.example.bookshop.service;

import com.example.bookshop.entity.Receiver;
import com.example.bookshop.repository.ReceiverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiverService {
    @Autowired
    private ReceiverRepo receiverRepo;

    public List<Receiver> getAllReceivers(int customerId) {
        return receiverRepo.findByCustomerIdOrderByIsDefaultDesc(customerId);
    }

    public Receiver findById(int receiverId) {
        return receiverRepo.findById(receiverId);
    }

    public Receiver getReceiverDefault(int customerId) {
        return receiverRepo.getReceiverDefault(customerId);
    }

    public Receiver getReceiverSelected(int customerId) {
        return receiverRepo.getReceiverSelected(customerId);
    }

    public Receiver save(Receiver receiver) {
        Receiver receiverIsDefault = getReceiverDefault(receiver.getCustomer().getId());
        if (receiverIsDefault != null && receiver.getIsDefault() == 1)
            receiverIsDefault.setIsDefault(0);
        return receiverRepo.save(receiver);
    }

    public void update(Receiver receiver) {
        Receiver receiverIsDefault = getReceiverDefault(receiver.getCustomer().getId());
        if (receiverIsDefault != null && !receiverIsDefault.equals(receiver) && receiver.getIsDefault() == 1) {
            receiverIsDefault.setIsDefault(0);
        }
        Receiver receiverIsSelected = getReceiverSelected(receiver.getCustomer().getId());
        if (receiverIsSelected != null && !receiverIsSelected.equals(receiver) && receiver.getIsSelected() == 1) {
            receiverIsSelected.setIsSelected(0);
        }
        receiverRepo.save(receiver);
    }

    public void deleteReceiverByIdAndCustomerId(int receiverId, int customerId) {
        receiverRepo.deleteByIdAndCustomerId(receiverId, customerId);
    }
}
