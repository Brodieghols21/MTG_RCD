// src/main/java/com/gmail/brodiegholson0/mtg_randomdraft/MTGRandomCardClickable.java

package com.gmail.brodiegholson0.mtg_randomdraft;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MTGRandomCardClickable extends JLabel {
    JsonObject randomCard = null;
    String cardImageURL = null;
    boolean isCommander = true;

    public MTGRandomCardClickable(ScryfallRandomCardFetcher fetcher, boolean legal) {
        super();
        getRandomCard(true, fetcher, legal);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!MTGRandomCardDraft.canPickCard) {
                    return;
                }
                MTGRandomCardDraft.canPickCard = false;
                try {
                    MTGRandomCardDraft screen = (MTGRandomCardDraft) SwingUtilities.getWindowAncestor(MTGRandomCardClickable.this);

                    if (isCommander) {
                        MTGRandomCardDraft.commanderColors = randomCard.getAsJsonArray("color_identity");
                        screen.addCommanderToRightPanel(randomCard);
                    } else {
                        screen.addCardToRightPanel(randomCard);
                    }
                    screen.commanderCheckBox.setSelected(false);
                    screen.randomizeCards(fetcher);

                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        });
    }

    public void getRandomCard(boolean isCommander, ScryfallRandomCardFetcher fetcher, boolean legal) {
        this.isCommander = isCommander;
        MTGRandomCardIcon newCard = new MTGRandomCardIcon(isCommander, fetcher, legal);
        randomCard = newCard.randomCard;
        cardImageURL = newCard.cardImageURL;
        setIcon(newCard);
    }
}