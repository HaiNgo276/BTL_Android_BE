package com.example.bookshop.controller;

import com.example.bookshop.entity.Shipping;
import com.example.bookshop.service.ShippingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/shipping")
@AllArgsConstructor
@Slf4j
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @GetMapping("")
    public ResponseEntity<?> getAllShipping() {
        List<Shipping> shippings = shippingService.getAll();
        return ResponseEntity.ok(shippings);
    }
}
