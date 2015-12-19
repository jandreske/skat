package examples;


import model.*;
import solvers.FullGameChecker;
import solvers.OneTrickChecker;

import java.util.ArrayList;
import java.util.List;

public class ExampleGameThree {


    public static void main(String[] args) {
        Logger logger = new Logger(true);
        GameParams params = new GameParams(Color.CLUBS, GameType.REGULAR);
        params.forehand = new Player("Jochen");
        params.middlehand = new Player("Fred");
        params.rearhand = new Player("Lisa");
        Game game = new Game(params, logger);

        game.dealCards(jochensCards, fredsCards, lisasCards, skat);
        game.changeSkat(params.forehand);

        long start = System.currentTimeMillis();
        FullGameChecker solver = new FullGameChecker();
        List<Trick> tricks = solver.calculateFullGame(game);
        long elapsed = System.currentTimeMillis() - start;
        logger.log("Calculations took " + elapsed / 1000 + " seconds");

        for (Trick trick : tricks) {
            game.playTrick(trick.cards);
        }

        game.logStatus();
    }

    private static List<Card> jochensCards = new ArrayList<Card>() {{
        add(new Card(Color.HEARTS, CardType.JACK));
        add(new Card(Color.DIAMONDS, CardType.JACK));
        add(new Card(Color.CLUBS, CardType.ACE));
        add(new Card(Color.CLUBS, CardType.TEN));
        add(new Card(Color.CLUBS, CardType.SEVEN));
        add(new Card(Color.SPADES, CardType.ACE));
        add(new Card(Color.DIAMONDS, CardType.TEN));
        add(new Card(Color.DIAMONDS, CardType.KING));
        add(new Card(Color.DIAMONDS, CardType.EIGHT));
        add(new Card(Color.DIAMONDS, CardType.SEVEN));
    }};

    private static List<Card> fredsCards = new ArrayList<Card>() {{
        add(new Card(Color.CLUBS, CardType.JACK));
        add(new Card(Color.CLUBS, CardType.EIGHT));
        add(new Card(Color.CLUBS, CardType.QUEEN));
        add(new Card(Color.CLUBS, CardType.NINE));
        add(new Card(Color.SPADES, CardType.TEN));
        add(new Card(Color.SPADES, CardType.EIGHT));
        add(new Card(Color.SPADES, CardType.SEVEN));
        add(new Card(Color.HEARTS, CardType.NINE));
        add(new Card(Color.HEARTS, CardType.ACE));
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
        add(new Card(Color.HEARTS, CardType.SEVEN));
        add(new Card(Color.HEARTS, CardType.EIGHT));
    }};
}
