package com.example.bookshop.util;

import com.example.bookshop.dto.objectdto.cartdto.CartDto;
import com.example.bookshop.dto.objectdto.cartdto.CartItemDto;
import com.example.bookshop.entity.CartItem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class CartUtil {
    public String generateCartId() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);

        // Chuyển mảng byte thành một số BigInteger dương
        BigInteger bigInteger = new BigInteger(1, randomBytes);

        // Chuyển số BigInteger thành chuỗi hex
        String hexString = bigInteger.toString(16);
        return hexString;
    }

    public List<CartItemDto> addToCartItemDto(List<CartItem> cartItems) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setItem_id(cartItem.getId());
            cartItemDto.setName(cartItem.getBook().getName());
            cartItemDto.setPrice(cartItem.getBook().getPrice() + "");
            cartItemDto.setQuantity(cartItem.getQuantity());
            cartItemDto.setProduct_id(cartItem.getBook().getId());
            BigDecimal quantity = cartItem.getBook().getDiscounted_price().multiply(new BigDecimal(cartItemDto.getQuantity()));
            cartItemDto.setSub_total(quantity + "");
            cartItemDto.setAdded_on(cartItem.getAddOn() + "");
            cartItemDto.setDiscounted_price(cartItem.getBook().getDiscounted_price() + "");
            cartItemDtos.add(cartItemDto);
        }
        return cartItemDtos;
    }

}
