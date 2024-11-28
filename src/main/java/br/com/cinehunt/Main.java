package br.com.cinehunt;

import br.com.cinehunt.api.ApiClient;
import br.com.cinehunt.handler.SearchHandler;
import br.com.cinehunt.service.DataProcessor;
import br.com.cinehunt.service.JsonTransformer;
import br.com.cinehunt.service.SearchService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        ApiClient api = new ApiClient();
        JsonTransformer transformer = new JsonTransformer();
        SearchService searchService = new SearchService(api, gson, transformer);
        DataProcessor dataProcessor = new DataProcessor(gson, transformer, searchService);

/*
        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
*/
        int serverPort = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);


        server.createContext("/search", new SearchHandler(searchService, dataProcessor));

        server.setExecutor(Executors.newFixedThreadPool(10)); // Pool de threads para manipulação de requisições
        server.start();
        System.out.println("Servidor HTTP está rodando na porta " + serverPort);
    }
}
