package com.example.bookshop.service;

import com.example.bookshop.entity.Author;
import com.example.bookshop.repository.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepo authorRepo;

    public List<Author> getFamousAuthor() {
        return authorRepo.findFamousAuthor();
    }

    public Author getAuthorInfo(int idAuthor) {
        return authorRepo.findById(idAuthor);
    }

    public Author addAuthor(Author author) {
        return authorRepo.save(author);
    }

    public Page<Author> getAllAuthor(int limit, int page) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return authorRepo.getAllAuthor(pageable);
    }

    public List<Author> getAll() {
        return authorRepo.findAll();
    }
}
