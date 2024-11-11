package com.example.biblioteca.literatura.universal.repository;

import com.example.biblioteca.literatura.universal.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);

    boolean existsByName(String name);


}
