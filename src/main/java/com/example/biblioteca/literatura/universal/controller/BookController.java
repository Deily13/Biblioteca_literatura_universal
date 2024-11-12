package com.example.biblioteca.literatura.universal.controller;

import com.example.biblioteca.literatura.universal.model.Book;
import com.example.biblioteca.literatura.universal.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Endpoint para buscar un libro por título y guardarlo en la base de datos
    @PostMapping("/search")
    public ResponseEntity<String> searchAndSaveBookByTitle(@RequestParam String title) {
        bookService.searchAndSaveBookByTitle(title);
        String message = "busqueda finalizada del libro: " + title;
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> showAllBooks() {
        List<Book> books = bookService.showAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(books);
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }


    // Endpoint para mostrar los libros de un autor específico
    @GetMapping("/author")
    public ResponseEntity<List<Book>> showBooksByAuthorName(@RequestParam String authorName) {
        List<Book> books = bookService.showBooksByAuthorName(authorName);
        if (books.isEmpty()) {
            String message = "No books found for the specified author: " + authorName;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    // Endpoint para contar los libros por idioma
    @GetMapping("/count/language")
    public ResponseEntity<String> countBooksByLanguage(@RequestBody String language) {
        long count = bookService.countBooksByLanguage();
        String message = "Cantidad de libros en el idioma '" + language + "': " + count;
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/top5Books")
    public void getTop5MostDownloadedBooks() {
        // Llamamos al servicio para mostrar los 5 libros más descargados
        bookService.showTop5MostDownloadedBooks();
    }
}
