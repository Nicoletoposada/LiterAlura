package com.mycompany.literalura;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LiterAlura{
    public static void main(String[] args) {
        // Endpoint de la API Gutendex
        String apiUrl = "https://gutendex.com/books/?search=pride";

        // Crear el cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Configurar la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl)) // URI del endpoint
                .GET() // Método GET
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar el código de estado
            if (response.statusCode() == 200) {
                // Crear el ObjectMapper para analizar el JSON
                ObjectMapper mapper = new ObjectMapper();

                // Mapear el JSON a la clase BookResponse
                BookResponse bookResponse = mapper.readValue(response.body(), BookResponse.class);

                // Mostrar el conteo total de libros
                System.out.println("Total de libros encontrados: " + bookResponse.getCount());

                // Mostrar los títulos y autores de los primeros resultados
                System.out.println("Libros encontrados:");
                for (Book book : bookResponse.getResults()) {
                    System.out.println("Título: " + book.getTitle());
                    System.out.println("Autor(es):");
                    for (Author author : book.getAuthors()) {
                        System.out.println("  - " + author.getName());
                    }
                    System.out.println("Descargas: " + book.getDownload_count());
                    System.out.println("------");
                }
            } else {    
                System.out.println("Error al realizar la solicitud. Código de estado: " + response.statusCode());
            }

        } catch (Exception e) {
            System.err.println("Ocurrió un error al procesar la solicitud:");
            e.printStackTrace();
        }
    }
}
