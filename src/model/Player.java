package model;


import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<Card> cards;
    public final String name;
    private boolean singlePlayer;

    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<Card>();
        this.singlePlayer = false;
    }

    public void dealCards(List<Card> cards) {
        //todo: Prüfen, dass es 10 Karten sind, keine doppelt, er noch keine hat, etc
        this.cards.addAll(cards);
    }

    public void playCard(Card card) {
        if (!cards.contains(card)) throw new IllegalArgumentException("Player does not have card.");
        cards.remove(card);
    }

    public List<Card> getCards() {
        //return copy so it cannot accidentally get modified
        return new ArrayList<Card>(cards);
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public void makeSinglePlayer() {
        this.singlePlayer = true;
    }
}
