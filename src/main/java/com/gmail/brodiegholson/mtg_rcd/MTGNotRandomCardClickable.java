package com.gmail.brodiegholson0.mtg_randomdraft;

import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MTGNotRandomCardClickable extends JLabel {
    JsonObject card = null;
    String cardImageURL = null;

    public MTGNotRandomCardClickable(JsonObject card) {
        super();
        setCard(card);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if(e.getButton() == MouseEvent.BUTTON3){
                        JPanel parent = (JPanel) getParent();
                        parent.remove(MTGNotRandomCardClickable.this);
                        parent.revalidate();
                        parent.repaint();
                    }
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }

        });
    }

    public String getCardName(){
        if(card == null){
            return "";
        }
        return card.get("name").getAsString();
    }

    public void setCard(JsonObject card) {
        this.card = card;
        MTGNotRandomCardIcon newCard = new MTGNotRandomCardIcon(card);
        cardImageURL = newCard.cardImageURL;
        setIcon(newCard);
    }

}
