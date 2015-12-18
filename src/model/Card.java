package model;

public class Card {

    public final Color color;
    public final CardType cardType;

    public Card(Color color, CardType cardType) {
        this.color = color;
        this.cardType = cardType;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card)) return false;
        if (other == this) return true;
        Card card = (Card) other;
        return (color == card.color && cardType == card.cardType);
    }

    @Override
    public String toString() {
        return color.name() + " " + cardType.name();
    }

    public int getValue() {
        return cardType.getValue();
    }

    //todo override hashcode, just in case
}
