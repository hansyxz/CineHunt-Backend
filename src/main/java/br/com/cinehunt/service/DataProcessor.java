package br.com.cinehunt.service;

import br.com.cinehunt.models.Movie;
import br.com.cinehunt.models.Series;
import br.com.cinehunt.util.EntertainmentData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataProcessor {
    private final Gson gson;
    private final JsonTransformer transformer;
    private final SearchService searchService;

    public DataProcessor(Gson gson, JsonTransformer transformer, SearchService searchService) {
        this.gson = gson;
        this.transformer = transformer;
        this.searchService = searchService;
    }

    public JsonArray processedResults(JsonArray results) throws IOException, InterruptedException {
        JsonArray finalResults = new JsonArray();
        results = formatsDate(results);

        for (JsonElement element: results) {
            JsonObject media = element.getAsJsonObject();
            String type = media.get("type").getAsString().toLowerCase();

            // Pega informações adicionais fazendo uma busca usando o id
            JsonObject additionalData = searchService.searchById(media.get("id").getAsInt(), type);
            formatsOverview(media, additionalData);

            JsonObject mergedData = transformer.mergeJsonObject(media, additionalData);
            addGenres(mergedData);

            JsonElement jsonEntertainment = filterResults(mergedData, type);

            if (jsonEntertainment != null) {
                finalResults.add(jsonEntertainment);
            }
        }

        return finalResults;
    }

    public void addGenres(JsonObject mergedData) {
        JsonArray genresList = mergedData.get("genres").getAsJsonArray();
        JsonArray genreNames = new JsonArray();

        if (!genresList.isEmpty()) {
            for (JsonElement genres : genresList) {
                if (genres.getAsJsonObject().get("name").getAsString().equals("Soap")) {
                    genreNames = new JsonArray();
                    genreNames.add("Soap"); // Limpo a lsta e coloco o genero soap, que vai ser filtrado depois
                    break;
                }
                genreNames.add(genres.getAsJsonObject().get("name"));
            }
        } else {
            genreNames.add("empty");
        }

        mergedData.add("genres", genreNames);
    }

    public JsonElement filterResults(JsonObject mergedData, String type) {
        var genre = mergedData.getAsJsonArray("genres");

        if ((mergedData.get("vote_count").getAsInt() > 400 || mergedData.get("vote_count").getAsInt() == 0)
                && mergedData.get("popularity").getAsFloat() > 20.000
                && !(mergedData.get("overview").getAsString().equals("empty") || genre.get(0).getAsString().equals("empty"))
                && !genre.get(0).getAsString().equals("Soap")) {
            if (type.equals("filme")) {
                EntertainmentData entertainmentData = gson.fromJson(mergedData, EntertainmentData.class);
                Movie newMovie = new Movie(entertainmentData);

                return gson.toJsonTree(newMovie);
            }
            if (type.equals("serie")) {
                EntertainmentData entertainmentData = gson.fromJson(mergedData, EntertainmentData.class);
                Series newSeries = new Series(entertainmentData);

                return gson.toJsonTree(newSeries);
            }
        }
        return null;
    }

    public JsonArray formatsDate(JsonArray entertainmentArray) {
        for (JsonElement element : entertainmentArray) {
            String type = element.getAsJsonObject().get("type").getAsString().toLowerCase();

            if (type.equals("serie")) {
                element.getAsJsonObject().addProperty("release_date", element.getAsJsonObject().get("first_air_date").getAsString());
            }

            if (!element.getAsJsonObject().get("release_date").getAsString().isEmpty()) {
                String year = element.getAsJsonObject().get("release_date").getAsString().substring(0, 4);
                element.getAsJsonObject().addProperty("release_date", year);
            } else {
                element.getAsJsonObject().addProperty("release_date", "9999");
            }
        }
        return sortByDate(entertainmentArray);
    }

    public void formatsOverview(JsonObject dataName, JsonObject dataId) {
        if (dataId.get("overview").getAsString().isEmpty()) {
            dataName.addProperty("overview", "empty");
        } else {
            dataName.addProperty("overview", dataId.get("overview").getAsString()); // sobrescreve o overview antigo
        }
    }

    // Converte JsonArray para List<JsonElement>
    public JsonArray sortByDate(JsonArray entertainmentArray) {
        List<JsonElement> entertainmentListOrganized = new ArrayList<>();
        entertainmentArray.forEach(entertainmentListOrganized::add);

        entertainmentListOrganized.sort(new Comparator<JsonElement>() { // Ordenar a lista com base na data de lançamento (do mais recente para o mais antigo)
            @Override
            public int compare(JsonElement element1, JsonElement element2) {
                int releaseDate1 = element1.getAsJsonObject().get("release_date").getAsInt();
                int releaseDate2 = element2.getAsJsonObject().get("release_date").getAsInt();
                return Integer.compare(releaseDate2, releaseDate1);
            }
        });

        entertainmentArray = new JsonArray();
        for (JsonElement entertainment : entertainmentListOrganized) {
            entertainmentArray.add(entertainment);
        }

        return entertainmentArray;
    }
}
