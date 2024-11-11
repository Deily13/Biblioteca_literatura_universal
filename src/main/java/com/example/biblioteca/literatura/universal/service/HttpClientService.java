package com.example.biblioteca.literatura.universal.service;

import com.example.biblioteca.literatura.universal.model.Book;

public interface HttpClientService {

    Book getBookByTitle(String title);
}
