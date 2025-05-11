package com.example.bookshop.controller;

import com.example.bookshop.config.jwt.JwtUtil;
import com.example.bookshop.dto.objectdto.receiver.ReceiverDto;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.receiver.ReceiverResponse;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Receiver;
import com.example.bookshop.service.CustomerService;
import com.example.bookshop.service.ReceiverService;
import com.example.bookshop.util.PhoneNumberValidator;
import com.example.bookshop.util.ReceiverUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/receiver")
public class ReceiverController {
    @Autowired
    private ReceiverService receiverService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<?> getAllReceivers(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            List<Receiver> receivers = receiverService.getAllReceivers(customerId);
            List<ReceiverDto> response = new ReceiverUtil().addToListReceiverDto(receivers, customerId);
            return ResponseEntity.ok(new ReceiverResponse(response.size(), response));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addReceiver(@RequestHeader("user-key") String userKey,
                                         @RequestParam("receiver_name") String receiverName,
                                         @RequestParam("receiver_phone") String receiverPhone,
                                         @RequestParam("receiver_address") String receiverAddress,
                                         @RequestParam("isDefault") int isDefalut) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Customer customer = customerService.getCustomer(customerId);
            List<Receiver> receivers = receiverService.getAllReceivers(customerId);
            Receiver receiver = new Receiver();
            receiver.setCustomer(customer);
            receiver.setReceiverName(receiverName);
            receiver.setReceiverPhone(receiverPhone);
            receiver.setAddress(receiverAddress);
            if (receivers.size() > 0) {
                receiver.setIsDefault(isDefalut);
            } else {
                receiver.setIsDefault(1);
            }
            if (new PhoneNumberValidator().isValidPhoneNumber(receiverPhone)) {
                receiverService.save(receiver);
                return ResponseEntity.ok(new Message("Thêm thông tin nhận hàng thành công!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "PHONE_01", "Số điện thoại không đúng định dạng", "receiver_phone"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> removeReceiver(@RequestHeader("user-key") String userKey,
                                            @RequestParam("receiverId") int receiverId) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Receiver receiver = receiverService.findById(receiverId);
            if (receiver.getIsDefault() == 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "REC_01", "Bạn không thể xóa địa chỉ mặc định!", "receiver_phone"));
            } else {
                receiverService.deleteReceiverByIdAndCustomerId(receiverId, customerId);
                return ResponseEntity.ok(new Message("Xóa thông tin thành công"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateReceiver(@RequestHeader("user-key") String userKey,
                                            @RequestParam("receiverId") int receiverId,
                                            @RequestParam("receiver_name") String receiverName,
                                            @RequestParam("receiver_phone") String receiverPhone,
                                            @RequestParam("receiver_address") String receiverAddress,
                                            @RequestParam("isDefault") int isDefault,
                                            @RequestParam("isSelected") int isSelected) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            Receiver receiver = receiverService.findById(receiverId);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "RECE_02", "Không tìm thấy địa chỉ người nhận!", ""));
            } else {
                receiver.setReceiverName(receiverName);
                receiver.setReceiverPhone(receiverPhone);
                receiver.setAddress(receiverAddress);
                receiver.setIsDefault(isDefault);
                receiver.setIsSelected(isSelected);
                if (new PhoneNumberValidator().isValidPhoneNumber(receiverPhone)) {
                    receiverService.update(receiver);
                    return ResponseEntity.ok(new Message("Cập nhật thông tin nhận hàng thành công!"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "PHONE_01", "Số điện thoại không đúng định dạng", "receiver_phone"));
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PutMapping("defaultIsSelected")
    public ResponseEntity<?> updateReceiverDefalutIsSelected(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int idCustomer = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Receiver receiver = receiverService.getReceiverDefault(idCustomer);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "RECE_02", "Không tìm thấy địa chỉ người nhận!", ""));
            } else {
                receiver.setIsSelected(1);
                receiverService.update(receiver);
                return ResponseEntity.ok(new Message("Cập nhật thông tin nhận hàng thành công!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @GetMapping("{receiver_id}")
    public ResponseEntity<?> getReceiverInfo(@RequestHeader("user-key") String userKey,
                                             @PathVariable("receiver_id") int receiverId) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            Receiver receiver = receiverService.findById(receiverId);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "REC_02", "Không tìm thấy thông tin người nhận", "receiver_id"));
            } else {
                ReceiverDto response = new ReceiverUtil().addToReceiverDto(receiver);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @GetMapping("/default")
    public ResponseEntity<?> getReceiverDefault(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Receiver receiver = receiverService.getReceiverDefault(customerId);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "REC_02", "Không tìm thấy thông tin người nhận", "receiver_id"));
            } else {
                ReceiverDto response = new ReceiverUtil().addToReceiverDto(receiver);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @GetMapping("/selected")
    public ResponseEntity<?> getReceiverSelected(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Receiver receiver = receiverService.getReceiverSelected(customerId);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "REC_02", "Không tìm thấy thông tin người nhận", "receiver_id"));
            } else {
                ReceiverDto response = new ReceiverUtil().addToReceiverDto(receiver);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }
}
