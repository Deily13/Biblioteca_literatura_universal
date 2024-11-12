package com.example.biblioteca.literatura.universal.service.implementation;

import com.example.biblioteca.literatura.universal.controller.BookController;
import com.example.biblioteca.literatura.universal.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class MenuService {

    private final BookService bookService;
    private final HttpClientServiceImpl httpClientServiceImpl;
    private final AuthorServiceImpl authorServiceImpl;
    private final BookController bookController;

    public MenuService(BookService bookService, HttpClientServiceImpl httpClientServiceImpl, AuthorServiceImpl authorServiceImpl, BookController bookController) {
        this.bookService = bookService;
        this.httpClientServiceImpl = httpClientServiceImpl;
        this.authorServiceImpl = authorServiceImpl;
        this.bookController = bookController;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(""" 
                    \n\n\n\n____________________________________________________________________
                    1 - Buscar libro por titulo mediante la API                        |
                    2 - Listar libros registrados                                      |
                    3 - Listar Autores disponibles en la base de datos                 |
                    4 - Buscar listado de libros escritos por autor mediante la API    |
                    5 - Listar libros de un autor mediante la base de datos            |
                    6 - Buscar autores vivos en determinado año                        |
                    7 - Contar la cantidad de libros disponibles en un idioma          |
                    8 - Listar 5 libros mas descargados encontrados en la base de datos|
                                                                                       |
                    0 - Salir;                                                         |
                    *******************************************************************\n
                    """);

            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("Ingrese el título del libro: ");
                    String title = scanner.nextLine();
                    bookService.searchAndSaveBookByTitle(title);
                    break;
                case "2":
                    bookService.showAllBooks();
                    break;
                case "3":
                    authorServiceImpl.listAllAuthors();
                    break;
                case "4":
                    httpClientServiceImpl.getBooksByAuthor();
                    break;
                case "5":
                    bookService.showBooksByAuthorName();
                    break;
                case "6":
                    httpClientServiceImpl.getLivingAuthorsByYear();
                    break;
                case "7":
                    bookService.countBooksByLanguage();
                    break;
                case "8":
                    bookService.showTop5MostDownloadedBooks();
                    break;

                case "0":
                    System.out.println("Saliendo del programa...");
                    return;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
            }
        }
    }
}