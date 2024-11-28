package br.com.cinehunt.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonTransformer {
    public void addType(String type, JsonArray jsonList) {
        for (int i = 0; i < jsonList.size(); i++) {
            JsonObject objet = jsonList.get(i).getAsJsonObject();
            objet.addProperty("type", type);
        }
    }

    public JsonArray mergeArray(JsonArray movies, JsonArray series) {
        JsonArray resultsList = new JsonArray();
        resultsList.addAll(movies);
        resultsList.addAll(series);

        return resultsList;
    }

    public JsonObject mergeJsonObject(JsonObject searchName, JsonObject searchId) {
        JsonObject mergedJson = new JsonObject();

        searchName.entrySet().forEach(entry -> mergedJson.add(entry.getKey(), entry.getValue()));

        searchId.entrySet().forEach(entry -> {
            if (!mergedJson.has(entry.getKey())) {
                mergedJson.add(entry.getKey(), entry.getValue());
            }
        });

        return mergedJson;
    }
}
