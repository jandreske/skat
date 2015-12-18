package examples;


import model.*;

import java.util.ArrayList;
import java.util.List;

public class ExampleGameOne {


    public static void main(String[] args) {
        Logger logger = new Logger(true);
        GameParams params = new GameParams(Color.HEARTS, GameType.REGULAR);
        params.forehand = new Player("Jochen");
        params.middlehand = new Player("Fred");
        params.rearhand = new Player("Lisa");
        Game game = new Game(params, logger);

        game.dealCards(jochensCards, fredsCards, lisasCards, skat);
        game.changeSkat(params.forehand);
        game.playTrick(new Card(Color.SPADES, CardType.ACE), new Card(Color.SPADES, CardType.EIGHT), new Card(Color.SPADES, CardType.NINE));
        game.playTrick(new Card(Color.CLUBS, CardType.TEN), new Card(Color.CLUBS, CardType.EIGHT), new Card(Color.CLUBS, CardType.KING));
        game.playTrick(new Card(Color.DIAMONDS, CardType.SEVEN), new Card(Color.DIAMONDS, CardType.QUEEN), new Card(Color.DIAMONDS, CardType.NINE));
        game.playTrick(new Card(Color.DIAMONDS, CardType.EIGHT), new Card(Color.DIAMONDS, CardType.ACE), new Card(Color.DIAMONDS, CardType.KING));
    }

    private static List<Card> jochensCards = new ArrayList<Card>() {{
        add(new Card(Color.HEARTS, CardType.JACK));
        add(new Card(Color.DIAMONDS, CardType.JACK));
        add(new Card(Color.CLUBS, CardType.TEN));
        add(new Card(Color.CLUBS, CardType.NINE));
        add(new Card(Color.CLUBS, CardType.SEVEN));
        add(new Card(Color.SPADES, CardType.ACE));
        add(new Card(Color.HEARTS, CardType.EIGHT));
        add(new Card(Color.DIAMONDS, CardType.TEN));
        add(new Card(Color.DIAMONDS, CardType.KING));
        add(new Card(Color.DIAMONDS, CardType.SEVEN));
    }};

    private static List<Card> fredsCards = new ArrayList<Card>() {{
        add(new Card(Color.CLUBS, CardType.JACK));
        add(new Card(Color.CLUBS, CardType.EIGHT));
        add(new Card(Color.CLUBS, CardType.QUEEN));
        add(new Card(Color.SPADES, CardType.TEN));
        add(new Card(Color.SPADES, CardType.EIGHT));
        add(new Card(Color.SPADES, CardType.SEVEN));
        add(new Card(Color.HEARTS, CardType.NINE));
        add(new Card(Color.HEARTS, CardType.ACE));
        add(new Card(Color.DIAMONDS, CardType.EIGHT));
        add(new Card(Color.DIAMONDS, CardType.QUEEN));
    }};

    private static List<Card> lisasCards = new ArrayList<Card>() {{
        add(new Card(Color.SPADES, CardType.JACK));
        add(new Card(Color.CLUBS, CardType.KING));
        add(new Card(Color.SPADES, CardType.KING));
        add(new Card(Color.SPADES, CardType.QUEEN));
        add(new Card(Color.SPADES, CardType.NINE));
        add(new Card(Color.HEARTS, CardType.TEN));
        add(new Card(Color.HEARTS, CardType.QUEEN));
        add(new Card(Color.HEARTS, CardType.KING));
        add(new Card(Color.DIAMONDS, CardType.NINE));
        add(new Card(Color.DIAMONDS, CardType.ACE));
    }};

    private static List<Card> skat = new ArrayList<Card>() {{
        add(new Card(Color.CLUBS, CardType.ACE));
        add(new Card(Color.HEARTS, CardType.SEVEN));
    }};
}
