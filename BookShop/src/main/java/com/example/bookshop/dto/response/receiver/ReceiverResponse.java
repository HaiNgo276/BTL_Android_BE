package com.example.bookshop.dto.response.receiver;

import com.example.bookshop.dto.objectdto.receiver.ReceiverDto;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceiverResponse {
    private int count;
    private List<ReceiverDto> receivers;
}
