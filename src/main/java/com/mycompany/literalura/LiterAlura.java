package com.mycompany.literalura;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LiterAlura {
    public static void main(String[] args) {
        // Endpoint de la API Gutendex
        String apiUrl = "https://gutendex.com/books/?search=pride";

        // Obtener y procesar datos
        BookResponse bookResponse = fetchBooks(apiUrl);
        if (bookResponse != null) {
            displayBooks(bookResponse);
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

    private static void displayBooks(BookResponse bookResponse) {
        System.out.println("Total de libros encontrados: " + bookResponse.getCount());
        for (Book book : bookResponse.getResults()) {
            System.out.println(book); // Usa el método toString de Book
        }
    }
}
