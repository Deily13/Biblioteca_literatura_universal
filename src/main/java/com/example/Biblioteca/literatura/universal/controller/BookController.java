package com.example.Biblioteca.literatura.universal.controller;

import com.example.Biblioteca.literatura.universal.service.BookService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


}
