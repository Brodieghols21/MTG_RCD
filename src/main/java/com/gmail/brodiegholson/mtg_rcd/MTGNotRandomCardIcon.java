package com.gmail.brodiegholson0.mtg_randomdraft;

import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MTGNotRandomCardIcon extends ImageIcon {
    JsonObject card = null;
    String cardImageURL = null;
    private static final int MAX_RETRIES = 5;

    public MTGNotRandomCardIcon(JsonObject card) {
        super();
        this.card = card;
        setCard(card);
    }

    private void setCard(JsonObject card) {
        try {
            if (card == null || card.getAsJsonObject("image_uris") == null ||
                    (cardImageURL = card.getAsJsonObject("image_uris").get("normal").getAsString()) == null) {
                return;
            } else {
                setImage(new ImageIcon(new URL(cardImageURL)).getImage().getScaledInstance(672 / 3, 936 / 3, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}