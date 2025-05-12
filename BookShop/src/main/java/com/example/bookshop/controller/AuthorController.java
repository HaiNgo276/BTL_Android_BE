package com.example.bookshop.controller;

import com.example.bookshop.dto.request.AuthorRequest;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.author.AuthorInfoResponse;
import com.example.bookshop.dto.response.author.AuthorResponse;
import com.example.bookshop.entity.Author;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.util.AuthorUtil;
import com.example.bookshop.util.MultilPartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/author")
@AllArgsConstructor
@Slf4j
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorUtil authorUtil;

    @GetMapping("/hot")
    public ResponseEntity<?> getFamousAuthor() {
        List<Author> authors = authorService.getFamousAuthor();
        AuthorResponse response = new AuthorResponse(authors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{author_id}")
    public ResponseEntity<?> getAuthorInfo(@PathVariable int author_id) {
        Author author = authorService.getAuthorInfo(author_id);
        AuthorInfoResponse response = new AuthorInfoResponse(author);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAuthor(@RequestParam("limit") int limit,
                                          @RequestParam("page") int page) {
        Page<Author> authorPage = authorService.getAllAuthor(limit, page);
        List<Author> authors = authorPage.getContent();
        return ResponseEntity.ok(new AuthorResponse(authors));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAuthor(@RequestBody AuthorRequest authorRequest) throws IOException {
        List<Author> authors = authorService.getAll();
        List<String> authorNames = new ArrayList<>();
        for (Author author : authors) {
            authorNames.add(author.getName());
        }
        if (authorNames.contains(authorRequest.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Tác giả này đã tồn tại trong hệ thống"));
        } else {
            MultipartFile multipartFile = new MultilPartFile().createMultipartFileFromUrl(authorRequest.getAvatar(), authorRequest.getFileName());
            String imageURL = authorUtil.uploadFile(multipartFile, "author");
            Author author = authorUtil.addAuthor(authorRequest);
            author.setAvatar(imageURL.replace("http", "https"));
            authorService.addAuthor(author);
            return ResponseEntity.ok(new Message("Đã thêm tác giả bản thành công!"));
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceededException() {
        return ResponseEntity.badRequest().body(new Error(400, "FILE_01", "Kích thước tệp tin vượt quá giới hạn cho phép(3MB).", "FILE"));
    }
}
