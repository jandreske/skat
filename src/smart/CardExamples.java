package smart;

import java.util.ArrayList;

public class CardExamples {

    public static void main(String[] args) {
        //all jacks and all clubs cards are trump for our example
        long trump = Cards.JACKS | Cards.CLUBS;
        //create example hand
        long player1Cards = CardUtils.combineCards(new ArrayList<Long>() {{
            add(Cards.CLUBS_ACE);
            add(Cards.CLUBS_SEVEN);
            add(Cards.SPADES_JACK);
            add(Cards.HEARTS_KING);
            add(Cards.HEARTS_JACK);
        }});
        long player2Cards = CardUtils.combineCards(new ArrayList<Long>() {{
            add(Cards.DIAMONDS_TEN);
            add(Cards.DIAMONDS_NINE);
            add(Cards.SPADES_QUEEN);
            add(Cards.HEARTS_EIGHT);
            add(Cards.HEARTS_NINE);
        }});

        //now we can check whether the players have trump or specific colors
        boolean hasTrumpP1 = CardUtils.contains(trump, player1Cards);
        boolean hasClubsP1 = CardUtils.contains(Cards.CLUBS, player1Cards);
        boolean hasSpadesP1 = CardUtils.contains(Cards.SPADES, player1Cards);
        boolean hasHeartsP1 = CardUtils.contains(Cards.HEARTS, player1Cards);
        boolean hasDiamondsP1 = CardUtils.contains(Cards.DIAMONDS, player1Cards);
        System.out.println("Player 1 - trump: " + hasTrumpP1 + ", clubs: " + hasClubsP1 + ", spades: " + hasSpadesP1 + ", hearts: " + hasHeartsP1 + ", diamonds: " + hasDiamondsP1);

        boolean hasTrumpP2 = CardUtils.contains(trump, player2Cards);
        boolean hasClubsP2 = CardUtils.contains(Cards.CLUBS, player2Cards);
        boolean hasSpadesP2 = CardUtils.contains(Cards.SPADES, player2Cards);
        boolean hasHeartsP2 = CardUtils.contains(Cards.HEARTS, player2Cards);
        boolean hasDiamondsP2 = CardUtils.contains(Cards.DIAMONDS, player2Cards);
        System.out.println("Player 2 - trump: " + hasTrumpP2 + ", clubs: " + hasClubsP2 + ", spades: " + hasSpadesP2 + ", hearts: " + hasHeartsP2 + ", diamonds: " + hasDiamondsP2);

        //we can also check whether two cards have the same color
        boolean same;
        same = CardUtils.sameColor(Cards.HEARTS_KING, Cards.HEARTS_SEVEN);
        System.out.println("Same color - hearts king and hearts seven: " + same);
        same = CardUtils.sameColor(Cards.DIAMONDS_SEVEN, Cards.DIAMONDS_ACE);
        System.out.println("Same color - diamonds seven and diamonds ace: " + same);
        same = CardUtils.sameColor(Cards.HEARTS_KING, Cards.HEARTS_JACK);
        System.out.println("Same color - hearts king and hearts jack: " + same);
        same = CardUtils.sameColor(Cards.SPADES_ACE, Cards.CLUBS_SEVEN);
        System.out.println("Same color - spades ace and clubs seven: " + same);

        //we can check which card wins a trick and how much points it is worth
        int index, points;
        index = CardUtils.getWinningCardIndex(Cards.DIAMONDS_ACE, Cards.DIAMONDS_NINE, Cards.DIAMONDS_KING, trump);
        points = CardUtils.getPoints(Cards.DIAMONDS_ACE | Cards.DIAMONDS_NINE | Cards.DIAMONDS_KING);
        System.out.println("Trick of diamonds ace, diamonds nine and diamonds king is won by card with index: " + index);
        System.out.println("Number of points: " + points);

        index = CardUtils.getWinningCardIndex(Cards.DIAMONDS_ACE, Cards.DIAMONDS_NINE, Cards.CLUBS_QUEEN, trump);
        points = CardUtils.getPoints(Cards.DIAMONDS_ACE | Cards.DIAMONDS_NINE | Cards.CLUBS_QUEEN);
        System.out.println("Trick of diamonds ace, diamonds nine and clubs queen is won by card with index: " + index);
        System.out.println("Number of points: " + points);

        index = CardUtils.getWinningCardIndex(Cards.DIAMONDS_EIGHT, Cards.DIAMONDS_NINE, Cards.SPADES_TEN, trump);
        points = CardUtils.getPoints(Cards.DIAMONDS_EIGHT | Cards.DIAMONDS_NINE | Cards.SPADES_TEN);
        System.out.println("Trick of diamonds eight, diamonds nine and spades ten is won by card with index: " + index);
        System.out.println("Number of points: " + points);

    }
}
