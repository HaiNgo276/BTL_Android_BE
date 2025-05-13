package com.example.bookshop.util;

import com.cloudinary.Cloudinary;
import com.example.bookshop.dto.objectdto.bookdto.*;
import com.example.bookshop.dto.request.BookRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.CartItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;


public class BookUtil {
    private final Cloudinary cloudinary = new Cloudinary();

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

    public List<BookInCartDto> addToBookInCartDto(List<CartItem> cartItems, List<Book> booksInWishlist) {
        List<BookInCartDto> bookList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            BookInCartDto bookInCartDto = new BookInCartDto();
            bookInCartDto.setItem_id(cartItem.getId());
            bookInCartDto.setName(cartItem.getBook().getName());
            bookInCartDto.setPrice(cartItem.getBook().getPrice() + "");
            bookInCartDto.setDiscounted_price(cartItem.getBook().getDiscounted_price() + "");
            bookInCartDto.setQuantity(cartItem.getQuantity());
            bookInCartDto.setQuantityBook(cartItem.getBook().getQuantity());
            bookInCartDto.setQuantitySold(cartItem.getBook().getQuantitySold());
            bookInCartDto.setProduct_id(cartItem.getBook().getId());
            bookInCartDto.setAdded_on(cartItem.getAddOn());
            bookInCartDto.setSub_total(cartItem.getBook().getDiscounted_price().multiply(new BigDecimal(cartItem.getQuantity())) + "");
            bookInCartDto.setImage(cartItem.getBook().getImage());
            int wishList = 0;
            for (Book book : booksInWishlist) {
                if (cartItem.getBook().getId() == book.getId()) {
                    wishList = 1;
                }
            }
            bookInCartDto.setWishlist(wishList);
            bookList.add(bookInCartDto);
        }
        return bookList;
    }

    public List<BookDto> addBook(List<Book> books) {
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto(book.getId(), book.getName(), book.getDescription(), book.getPrice() + "", book.getDiscounted_price() + "", book.getQuantity(), book.getQuantitySold(),
                    book.getImage(), book.getThumbnail(), book.getAuthor().getId(), book.getSupplier().getId(), book.getCategory().getId());
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }

    public List<BookBannerDto> addBookBanner(List<Book> books) {
        List<BookBannerDto> bookBannerDtos = new ArrayList<>();
        for (Book book : books) {
            BookBannerDto bookBanner = new BookBannerDto();
            bookBanner.setProduct_id(book.getId());
            bookBanner.setName(book.getName());
            bookBanner.setDescription(book.getDescription());
            bookBanner.setPrice(book.getPrice() + "");
            bookBanner.setDiscounted_price(book.getDiscounted_price() + "");
            bookBanner.setQuantity(book.getQuantity());
            bookBanner.setQuantitySold(book.getQuantitySold());
            bookBanner.setThumbnail(book.getThumbnail());
            bookBanner.setBanner_url(book.getBanner());
            bookBannerDtos.add(bookBanner);
        }
        return bookBannerDtos;
    }

    public BookDetailDto addBookDetailDto(Book book, List<Book> booksInWishlist, double ratingLevel) {
        BookDetailDto bookDetailDto = new BookDetailDto();
        bookDetailDto.setProduct_id(book.getId());
        bookDetailDto.setName(book.getName());
        bookDetailDto.setDescription(book.getDescription());
        bookDetailDto.setPrice(book.getPrice() + "");
        bookDetailDto.setDiscounted_price(book.getDiscounted_price() + "");
        bookDetailDto.setQuantity(book.getQuantity());
        bookDetailDto.setQuantitySold(book.getQuantitySold());
        bookDetailDto.setThumbnail(book.getThumbnail());
        bookDetailDto.setRatingLevel(ratingLevel);
        int wishlist = 0;
        if (booksInWishlist != null) {
            for (Book bookWishlist : booksInWishlist) {
                if (book.getId() == bookWishlist.getId()) {
                    wishlist = 1;
                }
            }
        }
        bookDetailDto.setWishlist(wishlist);
        return bookDetailDto;
    }

    public String uploadFile(MultipartFile multipartFile, String folderName) throws IOException {
        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", UUID.randomUUID().toString());
        uploadParams.put("folder", folderName);

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), uploadParams)  //chuyển đổi tệp đa phương tiện thành mảng byte sau đó upload
                .get("url")                                     //truy xuất URL của tệp
                .toString();
    }

    public Book setBookFromRequest(BookRequest bookRequest) {
        Book book = new Book();
        book.setName(bookRequest.getName());
        book.setDescription(bookRequest.getDescription());
        book.setQuantity(bookRequest.getQuantity());
        book.setPrice(bookRequest.getPrice());
        book.setDiscounted_price(bookRequest.getDiscounted_price());
        book.setAuthor(bookRequest.getAuthor());
        book.setCategory(bookRequest.getCategory());
        book.setSupplier(bookRequest.getSupplier());
        return book;
    }
}
