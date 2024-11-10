package com.example.Biblioteca.literatura.universal.service.implementation;

import com.example.Biblioteca.literatura.universal.model.Book;
import com.example.Biblioteca.literatura.universal.repository.AuthorRepository;
import com.example.Biblioteca.literatura.universal.repository.BookRepository;
import com.example.Biblioteca.literatura.universal.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final HttpClientServiceImpl httpClientService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    // Constructor de la clase donde se inyectan las dependencias necesarias
    public BookServiceImpl(HttpClientServiceImpl httpClientService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.httpClientService = httpClientService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void searchAndSaveBookByTitle(String title) {
        // 1. Llamada al método que obtiene el libro desde la API
        Book book = httpClientService.getBookByTitle(title);

        // 2. Verifica si el libro fue encontrado (es decir, no es nulo)
        if (book != null) {
            // Asegúrate de que todos los campos de book estén completos antes de guardarlo
            book.setTitle(book.getTitle());         // Asignar título
            book.setAuthor(book.getAuthor());       // Asignar autor
            book.setLanguage(book.getLanguage());   // Asignar idioma
            book.setDownload_count(book.getDownload_count()); // Asignar cantidad de descargas

            // Guarda el libro en la base de datos
            bookRepository.save(book);
        }
    }

    @Override
    public List<Book> showAllBooks() {
        List<Book> books = bookRepository.findAll(); // Obtener todos los libros de la base de datos

        if (books.isEmpty()) {
            System.out.println("No hay libros en la base de datos.");
        } else {
            // Iterar sobre la lista de libros y mostrar el título y el autor
            System.out.println("\n _____________________________________________________________________________________________________");
            for (Book book : books) {
                System.out.println("  Título: " + book.getTitle() + " | Autor: " + book.getAuthor());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------\n ");
        }
        return books;
    }

    @Override
    public List<Book> showBooksByAuthorName() {
        return List.of();
    }


}
