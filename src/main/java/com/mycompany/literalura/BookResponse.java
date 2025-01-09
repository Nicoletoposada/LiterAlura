package com.mycompany.literalura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private int count;
    private List<Book> results;

    // Getters, Setters y toString
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Book> getResults() {
        return results;
    }

    public void setResults(List<Book> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "BookResponse{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}
