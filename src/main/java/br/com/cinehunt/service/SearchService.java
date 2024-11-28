package br.com.cinehunt.service;

import br.com.cinehunt.api.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class SearchService {
    private final ApiClient apiClient;
    private final Gson gson;
    private final JsonTransformer transformer;

    public SearchService(ApiClient apiClient, Gson gson, JsonTransformer transformer) {
        this.apiClient = apiClient;
        this.gson = gson;
        this.transformer = transformer;
    }

    public JsonArray searchMedia(String searchTerm) throws IOException, InterruptedException {
        if (!searchTerm.isEmpty()) {
            HttpResponse<String> movieResponse = apiClient.searchMediaByName(searchTerm, "movie");
            HttpResponse<String> seriesResponse = apiClient.searchMediaByName(searchTerm, "tv");

            return processedResponse(movieResponse, seriesResponse);
        } else {
            HttpResponse<String> movieResponse = apiClient.searchFamousMedia("movie");
            HttpResponse<String> seriesResponse = apiClient.searchFamousMedia("tv");

            return processedResponse(movieResponse, seriesResponse);
        }
    }

    public JsonObject searchById(int id, String type) throws IOException, InterruptedException {
        HttpResponse<String> response;

        if (type.equals("filme")) {
            response = apiClient.searchMediaById(id, "movie");
        } else {
            response = apiClient.searchMediaById(id, "tv");
        }
        return gson.fromJson(response.body(), JsonObject.class);
    }

    public JsonArray processedResponse(HttpResponse<String> movieResponse, HttpResponse<String> seriesResponse) {
        JsonObject movieResults = gson.fromJson(movieResponse.body(), JsonObject.class);
        JsonObject seriesResults = gson.fromJson(seriesResponse.body(), JsonObject.class);

        JsonArray moviesList = movieResults.getAsJsonArray("results");
        JsonArray seriesList = seriesResults.getAsJsonArray("results");

        transformer.addType("Filme", moviesList);
        transformer.addType("Serie", seriesList);

        return transformer.mergeArray(moviesList, seriesList);
    }
}
