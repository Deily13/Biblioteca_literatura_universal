package com.example.Biblioteca.literatura.universal.service.implementation;

import com.example.Biblioteca.literatura.universal.model.Author;
import com.example.Biblioteca.literatura.universal.repository.AuthorRepository;
import com.example.Biblioteca.literatura.universal.service.AuthorService;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author saveAuthor(Author author) {
        // Verificar si el autor ya existe en la base de datos
        if (authorRepository.existsById(author.getId())) {
            System.out.println("El autor ya existe en la base de datos.");
        } else {
            // Guardar el autor en la base de datos
            authorRepository.save(author);
            System.out.println("El autor ha sido guardado en la base de datos.");
        }
        return author;
    }
}
