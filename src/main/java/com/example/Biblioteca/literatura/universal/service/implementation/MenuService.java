package com.example.Biblioteca.literatura.universal.service.implementation;

import com.example.Biblioteca.literatura.universal.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class MenuService {

    private final BookService bookService;
    private final HttpClientServiceImpl httpClientServiceImpl;

    public MenuService(BookService bookService, HttpClientServiceImpl httpClientServiceImpl) {
        this.bookService = bookService;
        this.httpClientServiceImpl = httpClientServiceImpl;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(""" 
                    1 - Buscar libro por titulo mediante la API
                    2 - Listar libros registrados
                    3 - Buscar listado de libros escritos por autor mediante la API
                    4 - Listar libros por idioma 
                    
                    0 - Salir;
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
                    httpClientServiceImpl.getBooksByAuthor();
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