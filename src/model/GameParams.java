package model;

public class GameParams {
    public final Color trump;
    public final GameType type;

    public Player forehand;
    public Player middlehand;
    public Player rearhand;

    public GameParams(Color trump, GameType type) {
        this.trump = trump;
        this.type = type;
    }

    public boolean isTrump(Card card) {
        switch (type) {
            case REGULAR:   return (card.cardType == CardType.JACK || card.color == trump);
            case GRAND:     return (card.cardType == CardType.JACK);
            case NULL:      return false;
            default:        throw new RuntimeException("Unsupported model.GameType: " + type.name());
        }
    }
}
