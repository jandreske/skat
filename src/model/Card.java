package model;


public class Card {

    public final Color color;
    public final Value value;

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card)) return false;
        if (other == this) return true;
        Card card = (Card) other;
        return (color == card.color && value == card.value);
    }

    //todo override hashcode, just in case
}
