// src/main/java/com/gmail/brodiegholson0/mtg_randomdraft/MTGRandomCardDraft.java

package com.gmail.brodiegholson0.mtg_randomdraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MTGRandomCardDraft extends JFrame {
    public static boolean canPickCard = true;
    public JCheckBox commanderCheckBox = null;
    public JCheckBox legalCheckBox = null;
    public JPanel leftPanel = null;
    public JPanel rightPanel = null;
    public JPanel commanderPanel = null;
    public JPanel cardGridPanel = null;
    public JButton randomizeButton = null;
    public JButton copyButton = null;
    public JTextField numberOfCardsTextField = null;
    MTGRandomCardClickable[] cardChoices = new MTGRandomCardClickable[5];

    public static JsonArray commanderColors = new JsonArray();

    public MTGRandomCardDraft() {
        super("MTG Random Card Draft");

        ImageIcon logoIcon = new ImageIcon("src/main/resources/com/gmail/brodiegholson0/mtg_randomdraft/RCDLogo512.png");
        setIconImage(logoIcon.getImage());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove window decorations
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this); // Set the window to full screen
        } else {
            System.err.println("Full screen mode not supported");
            setSize(1920, 1080); // Fallback to a predefined size
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    gd.setFullScreenWindow(null); // Exit full screen
                    dispose();
                    setUndecorated(false); // Restore window decorations
                    setPreferredSize(new Dimension(1280, 720)); // Set preferred size to 720p
                    setMinimumSize(new Dimension(1280, 720)); // Set minimum size to 720p
                    pack(); // Adjust the frame to the preferred size
                    setVisible(true);
                }
            }
        });

        ScryfallRandomCardFetcher fetcher = new ScryfallRandomCardFetcher();

        // Create the left and right panels
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY); // Just for visual distinction

        try {
            for (int i = 0; i < 5; i++) {
                cardChoices[i] = new MTGRandomCardClickable(fetcher, true);
                Thread.sleep(50);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Add the first three cards to the first row
        for (int i = 0; i < 3; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            leftPanel.add(cardChoices[i], gbc);
        }

        // Add the last two cards to the second row
        for (int i = 3; i < 5; i++) {
            gbc.gridx = i - 3;
            gbc.gridy = 1;
            leftPanel.add(cardChoices[i], gbc);
        }

        // Add the commander checkbox, legal cards only checkbox, number of cards to pick from, randomize button, and copy button to the bottom right slot
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        commanderCheckBox = new JCheckBox("Commander");
        commanderCheckBox.setSelected(true);
        commanderCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        legalCheckBox = new JCheckBox("Legal Only");
        legalCheckBox.setSelected(true);
        legalCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        // Create a panel for the number of cards input
        JPanel numberOfCardsPanel = new JPanel(new BorderLayout());
        JLabel numberOfCardsLabel = new JLabel("Number of cards: ");
        numberOfCardsTextField = new JTextField("5");
        numberOfCardsTextField.setPreferredSize(new Dimension(30, 20)); // Set preferred size to fit a single digit
        numberOfCardsPanel.add(numberOfCardsLabel, BorderLayout.WEST);
        numberOfCardsPanel.add(numberOfCardsTextField, BorderLayout.CENTER);

// Add the number of cards panel to the button panel
        buttonPanel.add(numberOfCardsPanel);
        randomizeButton = new JButton("Randomize");
        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Code to run when the button is clicked
                randomizeCards(fetcher);
            }
        });
        if(cardGridPanel == null || getCardCount() == 0){
            copyButton = new JButton("Add Cards First!");
        }
        else{
            copyButton = new JButton("Copy " + getCardCount() + " Cards to Clipboard");

        }
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Copying cards to clipboard");
                copyCardsToClipboard();
            }
        });

        buttonPanel.add(commanderCheckBox);
        buttonPanel.add(legalCheckBox);
        buttonPanel.add(numberOfCardsPanel);
        buttonPanel.add(randomizeButton);
        buttonPanel.add(copyButton);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        leftPanel.add(buttonPanel, gbc);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.DARK_GRAY); // Just for visual distinction

        // Create the commander panel
        commanderPanel = new JPanel(new BorderLayout());
        commanderPanel.setBackground(Color.BLACK);
        JLabel commanderLabel = new JLabel("Commander", SwingConstants.CENTER);
        commanderLabel.setForeground(Color.WHITE);
        commanderPanel.add(commanderLabel, BorderLayout.NORTH);

        // Create the card grid panel
        cardGridPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, variable rows, 10px gaps
        cardGridPanel.setBackground(Color.DARK_GRAY);

        // Add the commander panel and card grid panel to the right panel
        rightPanel.add(commanderPanel, BorderLayout.NORTH);
        rightPanel.add(cardGridPanel, BorderLayout.CENTER);

        // Make the right panel scrollable
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Set the unit increment for faster scrolling
        rightScrollPane.getVerticalScrollBar().setBlockIncrement(50); // Set the block increment for faster scrolling

        // Create a JSplitPane to split the window
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightScrollPane);
        splitPane.setResizeWeight(.25); // Set the initial divider location to the middle

        // Add the split pane to the frame
        add(splitPane);

        // Set the divider location to the middle
        splitPane.setDividerLocation(.25);

        setVisible(true); // Display frame
    }

    public void addCommanderToRightPanel(JsonObject card) {
        MTGNotRandomCardClickable commanderCard = new MTGNotRandomCardClickable(card);
        commanderCard.setName("commanderCard");

        // Remove existing commander card if present
        if (commanderPanel.getComponentCount() > 1) {
            commanderPanel.remove(1);
        }

        // Add the new commander card
        commanderPanel.add(commanderCard, BorderLayout.CENTER);

        commanderPanel.revalidate();
        commanderPanel.repaint();
        updateCopyButtonText();
    }

    public void addCardToRightPanel(JsonObject card) {
        MTGNotRandomCardClickable cardPanel = new MTGNotRandomCardClickable(card);
        cardGridPanel.add(cardPanel);
        cardGridPanel.revalidate();
        cardGridPanel.repaint();
        updateCopyButtonText();
    }

    public void randomizeCards(ScryfallRandomCardFetcher fetcher) {
        try {
            // Disable the button and cards
            randomizeButton.setEnabled(false);
            for (MTGRandomCardClickable cardChoice : cardChoices) {
                cardChoice.setEnabled(false);
            }

            int numCards;
            try{
                String numberOfCards = numberOfCardsTextField.getText().trim();
                numCards = Integer.parseInt(numberOfCards);
            } catch (Exception e) {
                numCards = 5;
                //throw new RuntimeException(e);
            }

            MTGRandomCardDraft screen = this;
            for (int i = 0; i < 5; i++) {
                if(i < numCards){
                    screen.cardChoices[i].setVisible(true);
                    screen.cardChoices[i].setEnabled(true);
                    screen.cardChoices[i].getRandomCard(commanderCheckBox.isSelected(), fetcher, legalCheckBox.isSelected());
                    Thread.sleep(50);
                }
                else{
                    cardChoices[i].setVisible(false);
                    cardChoices[i].setEnabled(false);
                }
            }
            revalidate();
            repaint();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Re-enable the button and cards
            randomizeButton.setEnabled(true);
            for (MTGRandomCardClickable cardChoice : cardChoices) {
                cardChoice.setEnabled(true);
            }
            canPickCard = true;
        }
    }

    private void updateCopyButtonText() {
        copyButton.setText("Copy " + getCardCount() + " Cards to Clipboard");
    }

    private int getCardCount() {
        return cardGridPanel.getComponentCount() + (commanderPanel.getComponentCount() > 1 ? 1 : 0);
    }

    private void copyCardsToClipboard() {
        StringBuilder cardNames = new StringBuilder();

        // Add commander card name if present
        if (commanderPanel.getComponentCount() > 1) {
            MTGNotRandomCardClickable commanderCard = (MTGNotRandomCardClickable) commanderPanel.getComponent(1);
            cardNames.append(commanderCard.getCardName()).append("\n");
        }

        // Add other card names
        for (Component component : cardGridPanel.getComponents()) {
            if (component instanceof MTGNotRandomCardClickable card) {
                cardNames.append(card.getCardName()).append("\n");
            }
        }

        System.out.println(cardNames.toString().trim());
        // Copy to clipboard
        StringSelection stringSelection = new StringSelection(cardNames.toString().trim());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    public static void main(String[] args) {
        new MTGRandomCardDraft();
    }
}