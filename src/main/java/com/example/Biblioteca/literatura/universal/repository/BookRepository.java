package com.example.Biblioteca.literatura.universal.repository;

import com.example.Biblioteca.literatura.universal.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByTitle(String title);

    boolean existsByTitle(String bookTitle);

    boolean existsByTitleAndAuthor(String title, String authorName);

    List<Book> findByAuthor(String authorName);

    long countByLanguage(String language);
}
