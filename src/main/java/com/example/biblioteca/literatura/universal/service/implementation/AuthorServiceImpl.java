package com.example.biblioteca.literatura.universal.service.implementation;

import com.example.biblioteca.literatura.universal.model.Author;
import com.example.biblioteca.literatura.universal.repository.AuthorRepository;
import com.example.biblioteca.literatura.universal.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author saveAuthor(Author author) {
        if (!authorRepository.existsByName(author.getName())) {
            authorRepository.save(author);
        } else {
            System.out.println("El autor ya existe en la base de datos.");
        }
        return author;
    }

    public void listAllAuthors() {
        // Obtener la lista de autores desde la base de datos
        List<Author> authors = authorRepository.findAll();

        // Verificar si la lista de autores está vacía
        if (authors.isEmpty()) {
            System.out.println("No hay autores disponibles en la base de datos.");
        } else {
            // Imprimir los autores encontrados
            System.out.println("Autores disponibles en la base de datos:");
            for (Author author : authors) {
                System.out.println("ID: " + author.getId() + " - Nombre: " + author.getName());
            }
        }
    }
}
