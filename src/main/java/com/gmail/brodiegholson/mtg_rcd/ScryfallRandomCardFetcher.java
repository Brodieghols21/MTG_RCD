package com.gmail.brodiegholson0.mtg_randomdraft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ScryfallRandomCardFetcher {
    private static final String SCRYFALL_RANDOM_COMMANDER_URL = "https://api.scryfall.com/cards/random?q=is%3Acommander";
    private static final String SCRYFALL_RANDOM_CARD_FITTING_COMMANDER_URL = "https://api.scryfall.com/cards/random?q=commander%3A";
    private static final String SCRYFALL_COMMANDER_LEGAL_ADDON = "+format%3Acommander";
    private HttpClient httpClient;
    private Gson gson;

    public ScryfallRandomCardFetcher() {
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();
    }

    public JsonObject fetchRandomCommander(boolean legal) throws Exception {
        String url = SCRYFALL_RANDOM_COMMANDER_URL;
        if(legal){
            url += SCRYFALL_COMMANDER_LEGAL_ADDON;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), JsonObject.class);
    }

    public JsonObject fetchRandomCardFittingCommander(boolean legal) throws Exception {
        String url = SCRYFALL_RANDOM_CARD_FITTING_COMMANDER_URL + convertStringColors(MTGRandomCardDraft.commanderColors);
        if(legal){
            url += SCRYFALL_COMMANDER_LEGAL_ADDON;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), JsonObject.class);
    }

    public static String convertStringColors(JsonArray colors) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < colors.size(); i++) {
            result.append(colors.get(i).getAsString());
        }
        if(result.isEmpty()) {
            return "C";
        }
        return result.toString();
    }
}
