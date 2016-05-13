package examples;


import model.*;
import smart.CardUtils;
import smart.Cards;
import smart.FastSolver;
import solvers.FullGameChecker;

import java.util.ArrayList;
import java.util.List;

public class TimeCheck {


    public static void main(String[] args) {
        Logger logger = new Logger(false);

        for (int i = 7; i <= 7; i++) {
//            GameParams params = new GameParams(Color.CLUBS, GameType.REGULAR);
//            params.forehand = new Player("Jochen");
//            params.middlehand = new Player("Fred");
//            params.rearhand = new Player("Lisa");
//            Game game = new Game(params, logger);
//
//            game.dealCards(jochensCards.subList(0, i), fredsCards.subList(0, i), lisasCards.subList(0, i), skat);
//            game.changeSkat(params.forehand);
//
//            long start = System.currentTimeMillis();
//            FullGameChecker solver = new FullGameChecker();
//            List<Trick> tricks = solver.calculateFullGame(game);
//            long elapsed = System.currentTimeMillis() - start;
//            System.out.println(i + " cards - old:" + elapsed);


            long trump = Cards.CLUBS | Cards.JACKS;
            long player0Cards = CardUtils.combineCards(jochensCardsSmart.subList(0, i));
            long player1Cards = CardUtils.combineCards(fredsCardsSmart.subList(0, i));
            long player2Cards = CardUtils.combineCards(lisasCardsSmart.subList(0, i));

            long start = System.currentTimeMillis();
            FastSolver fastSolver = new FastSolver(true);
            List<Long> fastTricks = fastSolver.calculateFullGame(0, trump, 0, 0, player0Cards, player1Cards, player2Cards);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(i + " cards - new:" + elapsed);
        }
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

    private static List<Long> jochensCardsSmart = new ArrayList<Long>() {{
        add(Cards.HEARTS_JACK);
        add(Cards.DIAMONDS_JACK);
        add(Cards.CLUBS_ACE);
        add(Cards.CLUBS_TEN);
        add(Cards.CLUBS_SEVEN);
        add(Cards.SPADES_ACE);
        add(Cards.DIAMONDS_TEN);
        add(Cards.DIAMONDS_KING);
        add(Cards.DIAMONDS_EIGHT);
        add(Cards.DIAMONDS_SEVEN);
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

    private static List<Long> fredsCardsSmart = new ArrayList<Long>() {{
        add(Cards.CLUBS_JACK);
        add(Cards.CLUBS_EIGHT);
        add(Cards.CLUBS_QUEEN);
        add(Cards.CLUBS_NINE);
        add(Cards.SPADES_TEN);
        add(Cards.SPADES_EIGHT);
        add(Cards.SPADES_SEVEN);
        add(Cards.HEARTS_NINE);
        add(Cards.HEARTS_ACE);
        add(Cards.DIAMONDS_QUEEN);
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

    private static List<Long> lisasCardsSmart = new ArrayList<Long>() {{
        add(Cards.SPADES_JACK);
        add(Cards.CLUBS_KING);
        add(Cards.SPADES_KING);
        add(Cards.SPADES_QUEEN);
        add(Cards.SPADES_NINE);
        add(Cards.HEARTS_TEN);
        add(Cards.HEARTS_QUEEN);
        add(Cards.HEARTS_KING);
        add(Cards.DIAMONDS_NINE);
        add(Cards.DIAMONDS_ACE);
    }};

    private static List<Card> skat = new ArrayList<Card>() {{
        add(new Card(Color.HEARTS, CardType.SEVEN));
        add(new Card(Color.HEARTS, CardType.EIGHT));
    }};
}
