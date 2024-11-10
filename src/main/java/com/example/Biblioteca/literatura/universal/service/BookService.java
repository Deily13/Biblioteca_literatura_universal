package com.example.Biblioteca.literatura.universal.service;

import com.example.Biblioteca.literatura.universal.model.Book;

import java.util.List;

public interface BookService {


    void searchAndSaveBookByTitle(String title);

    List<Book> showAllBooks();

    List<Book> showBooksByAuthorName();
}
