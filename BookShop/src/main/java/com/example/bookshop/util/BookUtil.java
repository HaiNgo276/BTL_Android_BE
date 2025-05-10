package com.example.bookshop.util;

import com.example.bookshop.dto.objectdto.bookdto.*;
import com.example.bookshop.entity.Book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookUtil {
    public List<BookHotNewDto> addBookNewHot(List<Book> books) {
        List<BookHotNewDto> bookHotNews = new ArrayList<>();
        for (Book book : books) {
            BookHotNewDto bookHotNew = new BookHotNewDto();
            bookHotNew.setProduct_id(book.getId());
            bookHotNew.setName(book.getName());
            bookHotNew.setDescription(book.getDescription());
            bookHotNew.setPrice(book.getPrice() + "");
            bookHotNew.setDiscounted_price(book.getDiscounted_price() + "");
            bookHotNew.setQuantity(book.getQuantity());
            bookHotNew.setQuantitySold(book.getQuantitySold());
            bookHotNew.setThumbnail(book.getThumbnail());
            bookHotNew.setSupplierName(book.getSupplier().getName());
            bookHotNews.add(bookHotNew);
        }
        return bookHotNews;
    }


}
