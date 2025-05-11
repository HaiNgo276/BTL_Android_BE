package com.example.bookshop.util;

import com.example.bookshop.dto.objectdto.receiver.ReceiverDto;
import com.example.bookshop.entity.Receiver;

import java.util.ArrayList;
import java.util.List;

public class ReceiverUtil {
    public List<ReceiverDto> addToListReceiverDto(List<Receiver> receivers, int customerId){
        List<ReceiverDto> lists=new ArrayList<>();
        for(Receiver receiver:receivers){
            ReceiverDto receiverDto=new ReceiverDto();
            receiverDto.setReceiver_id(receiver.getId());
            receiverDto.setReceiver_name(receiver.getReceiverName());
            receiverDto.setReceiver_phone(receiver.getReceiverPhone());
            receiverDto.setReceiver_address(receiver.getAddress());
            receiverDto.setIsSelected(receiver.getIsSelected());
            receiverDto.setIsDefault(receiver.getIsDefault());
            receiverDto.setCustomer_id(customerId);
            lists.add(receiverDto);
        }
        return lists;
    }

    public ReceiverDto addToReceiverDto(Receiver receiver){
        ReceiverDto receiverDto=new ReceiverDto();
        receiverDto.setReceiver_id(receiver.getId());
        receiverDto.setReceiver_name(receiver.getReceiverName());
        receiverDto.setReceiver_phone(receiver.getReceiverPhone());
        receiverDto.setReceiver_address(receiver.getAddress());
        receiverDto.setIsDefault(receiver.getIsDefault());
        receiverDto.setIsSelected(receiver.getIsSelected());
        receiverDto.setCustomer_id(receiver.getCustomer().getId());
        return receiverDto;
    }
}
