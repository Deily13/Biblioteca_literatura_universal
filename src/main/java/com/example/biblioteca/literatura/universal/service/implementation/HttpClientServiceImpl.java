package com.example.biblioteca.literatura.universal.service.implementation;

import com.example.biblioteca.literatura.universal.model.Book;
import com.example.biblioteca.literatura.universal.model.Author;
import com.example.biblioteca.literatura.universal.repository.BookRepository;
import com.example.biblioteca.literatura.universal.service.HttpClientService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

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

@Service
public class HttpClientServiceImpl implements HttpClientService {

    private static final String API_URL = "https://gutendex.com/books/";
    private final BookRepository bookRepository;
    private final AuthorServiceImpl authorService;

    public HttpClientServiceImpl(BookRepository bookRepository, AuthorServiceImpl authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    private JsonObject sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } else {
            System.out.println("Error al consultar la API: " + response.statusCode());
            return null;
        }
    }

    private Book parseBookJson(JsonObject bookJson, String authorName) {
        String bookTitle = bookJson.get("title").getAsString();
        int downloadCount = bookJson.has("download_count") ? bookJson.get("download_count").getAsInt() : 0;
        String language = bookJson.has("languages") && bookJson.getAsJsonArray("languages").size() > 0
                ? bookJson.getAsJsonArray("languages").get(0).getAsString()
                : "Unknown Language";

        Book book = new Book();
        book.setTitle(bookTitle);
        book.setAuthor(authorName);
        book.setDownload_count(downloadCount);
        book.setLanguage(language);

        return book;
    }

    private Author parseAuthorJson(JsonObject authorJson) {
        String authorName = authorJson.get("name").getAsString();
        int birthYear = authorJson.has("birth_year") ? authorJson.get("birth_year").getAsInt() : 0;
        int deathYear = authorJson.has("death_year") ? authorJson.get("death_year").getAsInt() : 0;

        Author author = new Author();
        author.setName(authorName);
        author.setBirth_year(birthYear);
        author.setDeath_year(deathYear);

        return author;
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


                    if (bookRepository.existsByTitleIgnoreCase(bookTitle)) {
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
                } else {
                    // Si no se encuentra el libro en los resultados de la API
                    System.out.println("No se encontró el libro \"" + title + "\" en la API.");
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
            String encodedAuthorName = URLEncoder.encode(authorName, StandardCharsets.UTF_8);
            String apiUrl = API_URL + "?search=" + encodedAuthorName;
            JsonObject responseJson = sendRequest(apiUrl);

            if (responseJson != null) {
                JsonArray results = responseJson.getAsJsonArray("results");
                List<Book> booksByAuthor = new ArrayList<>();

                for (int i = 0; i < results.size(); i++) {
                    JsonObject bookJson = results.get(i).getAsJsonObject();
                    Book book = parseBookJson(bookJson, authorName);

                    if (!bookRepository.existsByTitleIgnoreCase(book.getTitle())) {
                        booksByAuthor.add(book);
                    } else {
                        System.out.println("El libro \"" + book.getTitle() + "\" ya existe en la base de datos.");
                    }
                }

                if (!booksByAuthor.isEmpty()) {
                    bookRepository.saveAll(booksByAuthor);
                    System.out.println("Los libros nuevos del autor han sido guardados en la base de datos.");
                } else {
                    System.out.println("No hay libros nuevos para agregar en la base de datos.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLivingAuthorsByYear() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el año para buscar autores vivos: ");
        int year = scanner.nextInt();

        try {
            JsonObject responseJson = sendRequest(API_URL + "?languages=en");

            if (responseJson != null) {
                JsonArray results = responseJson.getAsJsonArray("results");

                System.out.println("\nAutores vivos en el año " + year + ":");
                for (int i = 0; i < results.size(); i++) {
                    JsonObject book = results.get(i).getAsJsonObject();
                    JsonArray authors = book.getAsJsonArray("authors");

                    for (int j = 0; j < authors.size(); j++) {
                        Author author = parseAuthorJson(authors.get(j).getAsJsonObject());
                        if (author.getBirth_year() <= year && (author.getDeath_year() == 0 || author.getDeath_year() > year)) {
                            System.out.println("Nombre del autor: " + author.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
