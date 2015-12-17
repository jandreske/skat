import model.Color;
import model.GameType;

public class GameParams {
    public final Color trump;
    public final GameType type;

    public GameParams(Color trump, GameType type) {
        this.trump = trump;
        this.type = type;
    }
}
