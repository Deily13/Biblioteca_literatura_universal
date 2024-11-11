package com.example.biblioteca.literatura.universal.repository;

import com.example.biblioteca.literatura.universal.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {



    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM Book b WHERE LOWER(b.title) = LOWER(:title)")
    boolean existsByTitleIgnoreCase(@Param("title") String title);


    List<Book> findByAuthor(String authorName);



    Book findByTitle(String title);

    public long countBooksByLanguage(String language);

}
