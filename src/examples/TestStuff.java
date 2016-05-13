package examples;

import smart.CardUtils;
import smart.Cards;
import smart.FastSolver;
import java.util.List;

public class TestStuff {


    public static void main(String[] args) {
        long trump;
        long player0Cards;
        long player1Cards;
        long player2Cards;
        FastSolver fastSolver;
        List<Long> fastTricks;

        trump = Cards.DIAMONDS | Cards.JACKS;
        player0Cards = Cards.CLUBS_ACE | Cards.CLUBS_SEVEN;
        player1Cards = Cards.CLUBS_TEN | Cards.DIAMONDS_SEVEN;
        player2Cards = Cards.CLUBS_KING | Cards.DIAMONDS_NINE;

        fastSolver = new FastSolver(false);
        fastTricks = fastSolver.calculateFullGame(0, trump, 0, 0, player0Cards, player1Cards, player2Cards);

        CardUtils.printCards(fastTricks);



        trump = Cards.DIAMONDS | Cards.JACKS;
        player0Cards = Cards.CLUBS_ACE | Cards.CLUBS_SEVEN;
        player1Cards = Cards.HEARTS_SEVEN | Cards.DIAMONDS_SEVEN;
        player2Cards = Cards.SPADES_QUEEN | Cards.SPADES_KING;

        fastSolver = new FastSolver(false);
        fastTricks = fastSolver.calculateFullGame(0, trump, 0, 0, player0Cards, player1Cards, player2Cards);

        CardUtils.printCards(fastTricks);



        trump = Cards.DIAMONDS | Cards.JACKS;
        player0Cards = Cards.CLUBS_ACE | Cards.CLUBS_SEVEN;
        player1Cards = Cards.HEARTS_SEVEN | Cards.HEARTS_QUEEN;
        player2Cards = Cards.CLUBS_QUEEN | Cards.CLUBS_KING;

        fastSolver = new FastSolver(false);
        fastTricks = fastSolver.calculateFullGame(0, trump, 0, 0, player0Cards, player1Cards, player2Cards);

        CardUtils.printCards(fastTricks);
    }
}
