package com.example.biblioteca.literatura.universal.service;

import com.example.biblioteca.literatura.universal.model.Book;

import java.util.List;

public interface BookService {


    void searchAndSaveBookByTitle(String title);

    List<Book> showAllBooks();

    List<Book> showBooksByAuthorName();

    List<Book> showBooksByAuthorName(String authorName);

    long countBooksByLanguage();
}
