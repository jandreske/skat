package solvers;


import model.Card;
import model.Game;
import model.Player;
import model.Trick;

import java.util.ArrayList;
import java.util.List;

public class OneTrickChecker {

    //todo: de duplicate that code into a smarter method instead of three
    public static List<Card> getTrick(Game game) {
        List<Card> possiblePlays = game.getPlaysNext().getCards();
        Trick bestTrick = new Trick();
        bestTrick.points = Integer.MIN_VALUE;
        for (Card card : possiblePlays) {
            Trick trick = followUpPlays(card, game);
            if (trick.winner.isSinglePlayer() != game.getPlaysNext().isSinglePlayer()) {
                //we are not on the same team as the one making the trick, count points negative
                trick.points = -1 * trick.points;
            }
            if (trick.points > bestTrick.points) {
                //this trick is better for us than the one we knew about before
                bestTrick = trick;
            }
        }
        return bestTrick.cards;
    }

    private static Trick followUpPlays(Card firstCard, Game game) {
        Player me = game.getNextPlayer(game.getPlaysNext());
        List<Card> allowedPlays = game.getAllowedPlays(firstCard, me);
        Trick bestTrick = new Trick();
        bestTrick.points = Integer.MIN_VALUE;
        for (Card card : allowedPlays) {
            Trick trick = lastCardPlays(firstCard, card, game);
            if (trick.winner.isSinglePlayer() != me.isSinglePlayer()) {
                //we are not on the same team as the one making the trick, count points negative
                trick.points = -1 * trick.points;
            }
            if (trick.points > bestTrick.points) {
                //this trick is better for us than the one we knew about before
                bestTrick = trick;
            }
        }
        //undo the negativity
        bestTrick.points = Math.abs(bestTrick.points);
        return bestTrick;
    }

    private static Trick lastCardPlays(Card firstCard, Card secondCard, Game game) {
        Player me = game.getNextPlayer(game.getNextPlayer(game.getPlaysNext()));
        List<Card> allowedPlays = game.getAllowedPlays(firstCard, me);
        Trick bestTrick = new Trick();
        bestTrick.points = Integer.MIN_VALUE;
        for (Card card : allowedPlays) {
            Trick trick = new Trick();
            trick.cards = new ArrayList<Card>();
            trick.cards.add(firstCard);
            trick.cards.add(secondCard);
            trick.cards.add(card);
            trick.points = firstCard.getValue() + secondCard.getValue() + card.getValue();
            trick.winner = game.getWinningPlayer(game.getWinningCard(firstCard, secondCard, card));
            if (trick.winner.isSinglePlayer() != me.isSinglePlayer()) {
                //we are not on the same team as the one making the trick, count points negative
                trick.points = -1 * trick.points;
            }
            if (trick.points > bestTrick.points) {
                //this trick is better for us than the one we knew about before
                bestTrick = trick;
            }
        }
        //undo the negativity
        bestTrick.points = Math.abs(bestTrick.points);
        return bestTrick;
    }

    public static void playTrick(Game game) {
        game.playTrick(getTrick(game));
    }
}
