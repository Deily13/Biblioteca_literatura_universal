package com.example.Biblioteca.literatura.universal.service.implementation;


import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import com.example.Biblioteca.literatura.universal.model.Book;
import com.example.Biblioteca.literatura.universal.model.Author;
import com.example.Biblioteca.literatura.universal.repository.BookRepository;
import com.example.Biblioteca.literatura.universal.service.HttpClientService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;


@Service
public class HttpClientServiceImpl implements HttpClientService {

    private static final String API_URL = "https://gutendex.com/books/";
    private final BookRepository bookRepository;
    private final AuthorServiceImpl authorService;  // Inyectamos el servicio de autor


    public HttpClientServiceImpl(BookRepository bookRepository, AuthorServiceImpl authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Override
    public Book getBookByTitle(String title) {
        try {
            // Codificar el título para la búsqueda en la API
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String apiUrl = API_URL + "?search=" + encodedTitle;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");

                if (results.size() > 0) {
                    JsonObject firstBook = results.get(0).getAsJsonObject();

                    String bookTitle = firstBook.get("title").getAsString();
                    String authorName = firstBook.has("authors") && firstBook.getAsJsonArray("authors").size() > 0
                            ? firstBook.getAsJsonArray("authors").get(0).getAsJsonObject().get("name").getAsString()
                            : "Unknown Author";

                    // Obtener birth_year y death_year del autor
                    int birthYear = firstBook.has("authors") && firstBook.getAsJsonArray("authors").size() > 0
                            ? firstBook.getAsJsonArray("authors").get(0).getAsJsonObject().get("birth_year").getAsInt()
                            : 0; // Si no se encuentra, asignar 0
                    int deathYear = firstBook.has("authors") && firstBook.getAsJsonArray("authors").size() > 0
                            ? firstBook.getAsJsonArray("authors").get(0).getAsJsonObject().get("death_year").getAsInt()
                            : 0; // Si no se encuentra, asignar 0

                    int downloadCount = firstBook.has("download_count") ? firstBook.get("download_count").getAsInt() : 0;
                    String language = firstBook.has("languages") && firstBook.getAsJsonArray("languages").size() > 0
                            ? firstBook.getAsJsonArray("languages").get(0).getAsString()
                            : "Unknown Language";


                    if (bookRepository.existsByTitle(bookTitle)) {
                        System.out.println("El libro ya existe en la base de datos.");
                        return null;
                    }

                    // Crear un objeto Author
                    Author author = new Author();
                    author.setName(authorName);
                    author.setBirth_year(birthYear);  // Guardar el año de nacimiento
                    author.setDeath_year(deathYear);  // Guardar el año de muerte

                    // Guardar el autor en la base de datos
                    authorService.saveAuthor(author);

                    // Crear el objeto Book
                    Book book = new Book();
                    book.setTitle(bookTitle);
                    book.setAuthor(authorName);
                    book.setDownload_count(downloadCount);
                    book.setLanguage(language);

                    // Guardar el libro en la base de datos
                    bookRepository.save(book);
                    System.out.println("El libro ha sido guardado en la base de datos.");

                    return book;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getBooksByAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del autor: ");
        String authorName = scanner.nextLine();

        try {
            // Codificar el nombre del autor para la búsqueda en la API
            String encodedAuthorName = URLEncoder.encode(authorName, StandardCharsets.UTF_8);
            String apiUrl = API_URL + "?search=" + encodedAuthorName;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");

                if (results.size() > 0) {
                    List<Book> booksByAuthor = new ArrayList<>();

                    for (int i = 0; i < results.size(); i++) {
                        JsonObject bookObject = results.get(i).getAsJsonObject();

                        String bookTitle = bookObject.get("title").getAsString();
                        int downloadCount = bookObject.has("download_count") ? bookObject.get("download_count").getAsInt() : 0;
                        String language = bookObject.has("languages") && bookObject.getAsJsonArray("languages").size() > 0
                                ? bookObject.getAsJsonArray("languages").get(0).getAsString()
                                : "Unknown Language";

                        System.out.println("Título: " + bookTitle);
                        System.out.println("Idioma: " + language);
                        System.out.println("Descargas: " + downloadCount);
                        System.out.println("-----------------------------");

                        // Verificar si el libro ya existe en la base de datos
                        if (!bookRepository.existsByTitle(bookTitle)) {
                            // Crear el objeto Book y agregarlo a la lista si no existe en la base de datos
                            Book book = new Book();
                            book.setTitle(bookTitle);
                            book.setAuthor(authorName);
                            book.setDownload_count(downloadCount);
                            book.setLanguage(language);
                            booksByAuthor.add(book);
                        } else {
                            System.out.println("El libro \"" + bookTitle + "\" ya existe en la base de datos y no se agregará nuevamente.");
                        }
                    }

                    // Persistir los libros nuevos en la base de datos
                    if (!booksByAuthor.isEmpty()) {
                        bookRepository.saveAll(booksByAuthor);
                        System.out.println("Los libros nuevos del autor han sido guardados en la base de datos.");
                    } else {
                        System.out.println("No hay libros nuevos para agregar en la base de datos.");
                    }
                } else {
                    System.out.println("No se encontraron libros para el autor especificado.");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getLivingAuthorsByYear() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el año para buscar autores vivos: ");
        int year = scanner.nextInt();

        try {
            String apiUrl = API_URL + "?languages=en"; // Cambiar el filtro según los requerimientos de la API

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");

                System.out.println("\nAutores vivos en el año " + year + ":");
                System.out.println("-------------------------------------");

                for (int i = 0; i < results.size(); i++) {
                    JsonObject book = results.get(i).getAsJsonObject();
                    JsonArray authors = book.getAsJsonArray("authors");

                    for (int j = 0; j < authors.size(); j++) {
                        JsonObject author = authors.get(j).getAsJsonObject();

                        // Verificar si el autor estaba vivo en el año especificado
                        int birthYear = author.has("birth_year") ? author.get("birth_year").getAsInt() : 0;
                        int deathYear = author.has("death_year") ? author.get("death_year").getAsInt() : 0;

                        if (birthYear <= year && (deathYear == 0 || deathYear > year)) {
                            String authorName = author.get("name").getAsString();
                            System.out.println("Nombre del autor: " + authorName);
                        }
                    }
                }

                System.out.println("-------------------------------------\n");

            } else {
                System.out.println("Error al consultar la API: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}