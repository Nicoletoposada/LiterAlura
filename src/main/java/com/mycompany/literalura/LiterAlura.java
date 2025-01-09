package com.mycompany.literalura;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

@SpringBootApplication
public class LiterAlura implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LiterAlura.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.println("Ingrese el término de búsqueda:");
                    String searchTerm = scanner.nextLine();
                    String apiUrl = "https://gutendex.com/books/?search=" + searchTerm;
                    BookResponse bookResponse = fetchBooks(apiUrl);
                    if (bookResponse != null) {
                        displayBooks(bookResponse);
                    } else {
                        System.out.println("No se pudieron recuperar los libros.");
                    }
                    break;

                case 2:
                    System.out.println("Gracias por usar LiterAlura. ¡Hasta pronto!");
                    exit = true;
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n--- Menú de LiterAlura ---");
        System.out.println("1. Buscar libros por título");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int getUserChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida. Por favor, ingrese un número.");
            return -1; // Valor para indicar opción inválida
        }
    }

    private BookResponse fetchBooks(String apiUrl) {
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

    private void displayBooks(BookResponse bookResponse) {
        System.out.println("\nTotal de libros encontrados: " + bookResponse.getCount());
        for (Book book : bookResponse.getResults()) {
            System.out.println(book); // Usa el método toString de Book
        }
    }
}
