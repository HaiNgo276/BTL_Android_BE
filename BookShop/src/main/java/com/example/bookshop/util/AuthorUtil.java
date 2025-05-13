package com.example.bookshop.util;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.example.bookshop.dto.request.AuthorRequest;
import com.example.bookshop.entity.Author;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorUtil {

    public Author addAuthor(AuthorRequest authorRequest){
        Author author = new Author();
        author.setName(authorRequest.getName());
        author.setDescription(authorRequest.getDescription());
        return author;
    }

}
