package com.example.Biblioteca.literatura.universal.repository;

import com.example.Biblioteca.literatura.universal.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);
}
