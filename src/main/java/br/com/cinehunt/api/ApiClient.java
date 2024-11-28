package br.com.cinehunt.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private final String urlBase = "https://api.themoviedb.org/3/";
    private final String apiKey = "102f3a2e38dafe579a6995124e3ce539";
    private final HttpClient httpClient;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> searchMediaByName(String name, String type) throws IOException, InterruptedException {
        String url = String.format(urlBase + "search/" + type + "?query=" + name + "&api_key=" + apiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> searchMediaById(int id, String type) throws IOException, InterruptedException {
        String url = urlBase + type + "/" + id + "?language=pt-BR&api_key=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> searchFamousMedia(String type) throws IOException, InterruptedException {
        String url = String.format(urlBase + "discover/" + type + "?include_adult=false&include_video=false&language=pt-BR&page=1&sort_by=popularity.desc&api_key=" + apiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
