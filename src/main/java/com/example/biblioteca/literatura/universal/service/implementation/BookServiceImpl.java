package com.example.biblioteca.literatura.universal.service.implementation;

import com.example.biblioteca.literatura.universal.model.Book;
import com.example.biblioteca.literatura.universal.repository.BookRepository;
import com.example.biblioteca.literatura.universal.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class BookServiceImpl implements BookService {

    private final HttpClientServiceImpl httpClientService;
    private final BookRepository bookRepository;


    public BookServiceImpl(HttpClientServiceImpl httpClientService, BookRepository bookRepository) {
        this.httpClientService = httpClientService;
        this.bookRepository = bookRepository;
    }

    // Método para buscar y guardar un libro por título
    @Override

    public void searchAndSaveBookByTitle(String title) {
        // Llamar al servicio que obtiene el libro desde la API
        Book book = httpClientService.getBookByTitle(title);

        // 2. Verifica si el libro fue encontrado (es decir, no es nulo)
        if (book != null) {
            // Asegúrate de que todos los campos de book estén completos antes de guardarlo
            book.setTitle(book.getTitle());         // Asignar título
            book.setAuthor(book.getAuthor());       // Asignar autor
            book.setLanguage(book.getLanguage());   // Asignar idioma
            book.setDownloadCount(book.getDownloadCount()); // Asignar cantidad de descargas

            // Guarda el libro en la base de datos
            bookRepository.save(book);
        }
    }


    // Método para verificar si un libro está completo (evitando la repetición de código)
    private boolean isBookComplete(Book book) {
        return book != null &&
                book.getTitle() != null &&
                book.getAuthor() != null &&
                book.getLanguage() != null &&
                book.getDownloadCount() != null;
    }

    // Método para obtener todos los libros guardados en la base de datos
    @Override
    public List<Book> showAllBooks() {
        // Obtener la lista de libros desde la base de datos
        List<Book> books = bookRepository.findAll();

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

        return books; // Devolver la lista de libros
    }


    @Override
    public List<Book> showBooksByAuthorName() {
        return List.of();
    }

    // Método para obtener los libros de un autor específico
    @Override
    public List<Book> showBooksByAuthorName(String authorName) {
        return bookRepository.findByAuthor(authorName);
    }

    @Override
    public long countBooksByLanguage() {
        // Solicitar el idioma si no se pasa como parámetro
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
            Ingresa el idioma para contar los libros:
            
            en (inglés)
            es (español)
            fr (francés)
            de (alemán)
            it (italiano)
            pt (portugués)
            
            """);
        String language = scanner.nextLine().trim();  // Solicitar al usuario el idioma

        // Pasar el idioma al repositorio
        long bookCount = bookRepository.countBooksByLanguage(language);

        // Mostrar el resultado
        System.out.println("Cantidad de libros en el idioma '" + language + "': " + bookCount);
        return bookCount;
    }

    @Override
    public void showTop5MostDownloadedBooks() {
        // Obtener los 5 libros más descargados
        List<Book> topBooks = bookRepository.findTop5ByOrderByDownloadCountDesc();

        // Verificar si la lista de libros está vacía
        if (topBooks.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            // Mostrar los libros encontrados (pueden ser menos de 5 si no hay suficientes)
            System.out.println("\n _____________________________________________________________________________________________________");
            System.out.println("Los libros más descargados son:");
            System.out.println("-------------------------------------------------------------------------------------------------------");

            // Mostrar solo los libros encontrados
            for (Book book : topBooks) {
                System.out.println("  Título: " + book.getTitle() + " | Autor: " + book.getAuthor() + " | Idioma: " + book.getLanguage());
            }

            // Si hay menos de 5 libros, indicarlo
            if (topBooks.size() < 5) {
                System.out.println("\nSe encontraron " + topBooks.size() + " libros en la base de datos.");
            }

            System.out.println("-------------------------------------------------------------------------------------------------------\n ");
        }
    }

}