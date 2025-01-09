package com.mycompany.literalura;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LiterAlura {
    private static final String BASE_API_URL = "https://gutendex.com/books/";
    private static final List<Book> bookCatalog = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            displayMenu();
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1 -> searchBookByTitle(scanner);
                case 2 -> listAllBooks();
                case 3 -> filterBooksByLanguage(scanner);
                case 0 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (option != 0);
    }

    private static void displayMenu() {
        System.out.println("\n=== Menú ===");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar todos los libros");
        System.out.println("3. Filtrar libros por idioma");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void searchBookByTitle(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String title = scanner.nextLine();
        String apiUrl = BASE_API_URL + "?search=" + title.replace(" ", "+");

        BookResponse bookResponse = fetchBooks(apiUrl);
        if (bookResponse != null && !bookResponse.getResults().isEmpty()) {
            Book book = bookResponse.getResults().get(0); // Tomar el primer resultado
            book.setLanguage(bookResponse.getResults().get(0).getLanguages().get(0)); // Guardar primer idioma
            bookCatalog.add(book);
            System.out.println("Libro agregado al catálogo: ");
            System.out.println(book);
        } else {
            System.out.println("No se encontró ningún libro con ese título.");
        }
    }

    private static BookResponse fetchBooks(String apiUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), BookResponse.class);
            } else {
                System.out.println("Error: Código de estado " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al realizar la solicitud:");
            e.printStackTrace();
        }
        return null;
    }

    private static void listAllBooks() {
        if (bookCatalog.isEmpty()) {
            System.out.println("El catálogo está vacío.");
        } else {
            System.out.println("\n=== Catálogo de Libros ===");
            for (Book book : bookCatalog) {
                System.out.println(book);
            }
        }
    }

    private static void filterBooksByLanguage(Scanner scanner) {
    System.out.println("Ejemplos de idiomas disponibles:");
    System.out.println("en: Inglés, es: Español, fr: Francés, de: Alemán, it: Italiano");
    System.out.println("pt: Portugués, nl: Holandés, ru: Ruso, zh: Chino, ja: Japonés");
    System.out.println("Ingrese el código de idioma para filtrar: ");
    
    String language = scanner.nextLine();
    List<Book> filteredBooks = bookCatalog.stream()
            .filter(book -> language.equalsIgnoreCase(book.getLanguage()))
            .toList();

    if (filteredBooks.isEmpty()) {
        System.out.println("No se encontraron libros en ese idioma.");
    } else {
        System.out.println("\n=== Libros en Idioma " + language + " ===");
        for (Book book : filteredBooks) {
            System.out.println(book);
            }
        }
    }
}
