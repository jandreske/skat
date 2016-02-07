package smart;


import java.util.ArrayList;
import java.util.List;

public class CardUtils {

    public static long combineCards(List<Long> cards) {
        long result = 0;
        for (long card : cards) {
            result = result | card;
        }
        return result;
    }

    public static long intersectCards(long cards, long filter) {
        return cards & filter;
    }

    public static boolean contains(long cards, long filter) {
        return (cards & filter) > 0;
    }

    public static long removeCard(long cards, long card) {
        return cards ^ card;
    }

    public static long getAllowedCards(long firstCard, long cards, long trump) {
        long allowedCards = 0;
        if (contains(trump, firstCard)) {
            allowedCards = intersectCards(cards, trump);
        } else {
            long color = getColor(firstCard);
            allowedCards = intersectCards(color, cards);
        }
        if (allowedCards == 0) allowedCards = cards;
        return allowedCards;
    }

    private static long getColor(long card) {
        if ((Cards.DIAMONDS & card) > 0) return Cards.DIAMONDS;
        if ((Cards.HEARTS & card) > 0) return Cards.HEARTS;
        if ((Cards.SPADES & card) > 0) return Cards.SPADES;
        if ((Cards.CLUBS & card) > 0) return Cards.CLUBS;
        return 0;
    }

    public static boolean sameColor(long card1, long card2) {
        if ((Cards.DIAMONDS & card1) > 0 && (Cards.DIAMONDS & card2) > 0) return true;
        if ((Cards.HEARTS & card1) > 0 && (Cards.HEARTS & card2) > 0) return true;
        if ((Cards.SPADES & card1) > 0 && (Cards.SPADES & card2) > 0) return true;
        if ((Cards.CLUBS & card1) > 0 && (Cards.CLUBS & card2) > 0) return true;
        return false;
    }

    public static long getWinningCard(long firstcard, long secondcard, long thirdcard, long trump) {
        if (contains(trump, (firstcard | secondcard | thirdcard))) {
            firstcard = intersectCards(trump, firstcard);
            secondcard = intersectCards(trump, secondcard);
            thirdcard = intersectCards(trump, thirdcard);
        } else {
            secondcard = sameColor(firstcard, secondcard) ? secondcard : 0;
            thirdcard = sameColor(firstcard, thirdcard) ? thirdcard : 0;
        }
        return Math.max(firstcard, Math.max(secondcard, thirdcard));
    }

    public static int getWinningCardIndex(long firstcard, long secondcard, long thirdcard, long trump) {
        long winningCard = getWinningCard(firstcard, secondcard, thirdcard, trump);
        if (firstcard == winningCard) return 0;
        if (secondcard == winningCard) return 1;
        if (thirdcard == winningCard) return 2;
        throw new RuntimeException("Weird bug in getWinningCardIndex, winning card value was " + winningCard);
    }

    public static int getPoints(long cards) {
        int points = 0;
        points += 2 * numberOfBitsSet(intersectCards(Cards.TWO_POINTS, cards));
        points += 3 * numberOfBitsSet(intersectCards(Cards.THREE_POINTS, cards));
        points += 4 * numberOfBitsSet(intersectCards(Cards.FOUR_POINTS, cards));
        points += 10 * numberOfBitsSet(intersectCards(Cards.TEN_POINTS, cards));
        points += 11 * numberOfBitsSet(intersectCards(Cards.ELEVEN_POINTS, cards));
        return points;
    }

    private static int numberOfBitsSet(long value) {
        int count = 0;
        while (value > 0) {           // until all bits are zero
            if ((value & 1) == 1) {   // check lower bit
                count++;
            }
            value >>= 1;              // shift bits, removing lower bit
        }
        return count;
    }

    //for debugging
    public static void printBits(long value) {
        //careful, lowest bit will get printed first (very left)
        for (int i = 0; i < 32; i++) {
            if ((value & 1) == 1) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
            value >>= 1;
        }
        System.out.println();
    }

    public static List<Long> getAllCards(long cards) {
        List<Long> result = new ArrayList<Long>();
        long check = 1;
        for (int i = 0; i < 32; i++) {
            if ((check & cards) > 0) {
                result.add(check);
            }
            check <<= 1;
        }
        return result;
    }
}
