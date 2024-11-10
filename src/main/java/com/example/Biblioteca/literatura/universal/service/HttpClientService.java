package com.example.Biblioteca.literatura.universal.service;

import com.example.Biblioteca.literatura.universal.model.Book;

import java.util.List;

public interface HttpClientService {

    Book getBookByTitle(String title);
}
