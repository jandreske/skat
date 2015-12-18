package model;

public enum CardType {
    ACE(11),
    TEN(10),
    KING(4),
    QUEEN(3),
    JACK(2),
    NINE(0),
    EIGHT(0),
    SEVEN(0);

    private final int value;

    private CardType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
