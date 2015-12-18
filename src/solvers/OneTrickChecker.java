package solvers;


import model.Card;
import model.Game;
import model.Player;
import model.Trick;

import java.util.ArrayList;
import java.util.List;

public class OneTrickChecker {

    public static List<Card> getTrick(Game game) {
        Trick bestTrick = getTrick(new ArrayList<Card>(), game, game.getPlaysNext());
        return bestTrick.cards;
    }

    private static Trick getTrick(List<Card> cards, Game game, Player player) {
        if (cards.size() < 3) {
            //trick not yet complete
            List<Card> possiblePlays;
            if (cards.isEmpty()) {
                possiblePlays = player.getCards();
            } else {
                //lets check what is actually allowed to follow the first card
                possiblePlays = game.getAllowedPlays(cards.get(0), player);
            }
            Trick bestTrick = new Trick();
            bestTrick.points = Integer.MIN_VALUE;
            for (Card card : possiblePlays) {
                List<Card> newCards = new ArrayList<Card>(cards);
                newCards.add(card);
                Trick trick = getTrick(newCards, game, game.getNextPlayer(player));
                if (trick.winner.isSinglePlayer() != player.isSinglePlayer()) {
                    //we are not on the same team as the one making the trick, count points negative
                    trick.points = -1 * trick.points;
                }
                if (trick.points > bestTrick.points) {
                    //this trick is better for us than the one we knew about before
                    bestTrick = trick;
                }
            }
            bestTrick.points = Math.abs(bestTrick.points);
            return bestTrick;
        } else {
            //we have a full trick, build and return it
            Trick trick = new Trick();
            trick.cards = cards;
            trick.points = cards.get(0).getValue() + cards.get(1).getValue() + cards.get(2).getValue();
            trick.winner = game.getWinningPlayer(game.getWinningCard(cards.get(0), cards.get(1), cards.get(2)));
            return trick;
        }
    }

    public static void playTrick(Game game) {
        game.playTrick(getTrick(game));
    }
}
