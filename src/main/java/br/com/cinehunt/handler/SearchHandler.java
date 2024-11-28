package br.com.cinehunt.handler;

import br.com.cinehunt.service.DataProcessor;
import br.com.cinehunt.service.SearchService;
import com.google.gson.JsonArray;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// Classe para lidar com as requisições na rota "/search"
public class SearchHandler implements HttpHandler {
    private final SearchService searchService;
    private final DataProcessor dataProcessor;

    public SearchHandler(SearchService searchService, DataProcessor dataProcessor) {
        this.searchService = searchService;
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "application/json");
                headers.add("Access-Control-Allow-Origin", "*");

                String query = exchange.getRequestURI().getQuery();

                String searchTerm = query != null && query.contains("=") ? query.substring(query.indexOf('=') + 1) : "";
                JsonArray response = processSearch(searchTerm);

                sendResponse(exchange, response);
            } else {
                sendErrorResponse(exchange, 405, "");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "");
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private JsonArray processSearch(String searchTerm) throws IOException, InterruptedException {

        JsonArray responseAPI = searchService.searchMedia(searchTerm.replace(" ", "+"));
        JsonArray results = dataProcessor.processedResults(responseAPI);
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i).toString());
        }

        return results;
    }

    public void sendResponse(HttpExchange exchange, JsonArray response) throws IOException {

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String jsonResponse = response.toString();
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, responseBytes.length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.close();
    }
}
