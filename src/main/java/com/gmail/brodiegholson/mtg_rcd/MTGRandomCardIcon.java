package com.gmail.brodiegholson0.mtg_randomdraft;

import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MTGRandomCardIcon extends ImageIcon {
    JsonObject randomCard = null;
    String cardImageURL = null;
    private static final int MAX_RETRIES = 5;

    public MTGRandomCardIcon(boolean isCommander, ScryfallRandomCardFetcher fetcher, boolean legal) {
        super();

        try {
            if (isCommander) {
                getRandomCommander(fetcher, legal);
            } else {
                getRandomCard(fetcher, legal);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getRandomCard(ScryfallRandomCardFetcher fetcher, boolean legal) throws Exception {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            randomCard = fetcher.fetchRandomCardFittingCommander(legal);
            if (randomCard == null || randomCard.getAsJsonObject("image_uris") == null ||
                    (cardImageURL = randomCard.getAsJsonObject("image_uris").get("normal").getAsString()) == null) {
                retries++;
                Thread.sleep(50);
            } else {
                setImage(new ImageIcon(new URL(cardImageURL)).getImage().getScaledInstance(672 / 3, 936 / 3, Image.SCALE_SMOOTH));
                return;
            }
        }
        throw new Exception("Failed to fetch a valid random card after " + MAX_RETRIES + " attempts.");
    }

    private void getRandomCommander(ScryfallRandomCardFetcher fetcher, boolean legal) throws Exception {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            randomCard = fetcher.fetchRandomCommander(legal);
            if (randomCard == null || randomCard.getAsJsonObject("image_uris") == null ||
                    (cardImageURL = randomCard.getAsJsonObject("image_uris").get("normal").getAsString()) == null) {
                retries++;
                Thread.sleep(50);
            } else {
                setImage(new ImageIcon(new URL(cardImageURL)).getImage().getScaledInstance(672 / 3, 936 / 3, Image.SCALE_SMOOTH));
                return;
            }
        }
        throw new Exception("Failed to fetch a valid random commander after " + MAX_RETRIES + " attempts.");
    }
}