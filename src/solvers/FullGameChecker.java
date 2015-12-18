package solvers;


import model.Card;
import model.Color;
import model.Game;
import model.Trick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullGameChecker {
    private int singlePlayer;
    private Color trump;
    private Game game;

    public Card getBestCard(List<Card> played) {
        //todo: give best card to play out, after the given cards have been played
        return null;
    }

    public Card getBestCard() {
        return getBestCard(new ArrayList<Card>());
    }

    public Trick getBestTrick() {
        //todo: return the next trick if everybody plays optimal
        return null;
    }

    /**
     * Calculates a full game, assuming every player plays perfectly
     * @param game the game to use for the initial status
     * @return list of tricks in the order they should be played
     */
    public List<Trick> calculateFullGame(Game game) {
        singlePlayer = getSinglePlayer(game);
        trump = game.getParams().trump;
        this.game = game;

        GameStatus status = new GameStatus();
        status.singlePlayerPoints = getSinglePlayerPoints(game);
        status.playsNext = getPlaysNext(game);
        status.playerCards.put(0, game.getParams().forehand.getCards());
        status.playerCards.put(1, game.getParams().middlehand.getCards());
        status.playerCards.put(2, game.getParams().rearhand.getCards());
        GameStatus result = solve(status, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return result.playedTricks;
    }

    private GameStatus solve(GameStatus status, int alpha, int beta, int level) {
        int player = (status.playsNext + status.cardsPlayed.size()) % 3;
        GameStatus alphaNode = status;
        GameStatus betaNode = status;
        List<Card> cards = status.playerCards.get(player);
        if (!status.cardsPlayed.isEmpty()) {
            //we have to check whats actually allowed
            cards = game.getAllowedPlays(status.cardsPlayed.get(0), cards);
        }
        for (Card card : cards) {
            GameStatus newStatus = solve(status.play(player, card), alpha, beta, level + 1);
            if (player == singlePlayer) {
                //we are a MAX node
                if (newStatus.singlePlayerPoints > alpha) {
                    alpha = newStatus.singlePlayerPoints;
                    alphaNode = newStatus;
                }
            } else {
                //we are a MIN node
                if (newStatus.singlePlayerPoints < beta) {
                    beta = newStatus.singlePlayerPoints;
                    betaNode = newStatus;
                }
            }
            if (alpha >= beta) break;
        }
        if (player == singlePlayer) {
            //we are a MAX node
            return alphaNode;
        } else {
            //we are a MIN node
            return betaNode;
        }
    }

    private int getPlaysNext(Game game) {
        if (game.getPlaysNext() == game.getParams().forehand) return 0;
        if (game.getPlaysNext() == game.getParams().middlehand) return 1;
        if (game.getPlaysNext() == game.getParams().rearhand) return 2;
        throw new IllegalArgumentException("No matching player plays next");
    }

    private int getSinglePlayerPoints(Game game) {
        if (game.getParams().forehand.isSinglePlayer()) return game.getParams().forehand.getPoints();
        if (game.getParams().middlehand.isSinglePlayer()) return game.getParams().middlehand.getPoints();
        if (game.getParams().rearhand.isSinglePlayer()) return game.getParams().rearhand.getPoints();
        throw new IllegalArgumentException("No single player set");
    }

    private int getSinglePlayer(Game game) {
        if (game.getParams().forehand.isSinglePlayer()) return 0;
        if (game.getParams().middlehand.isSinglePlayer()) return 1;
        if (game.getParams().rearhand.isSinglePlayer()) return 2;
        throw new IllegalArgumentException("No single player set");
    }

    private class GameStatus {
        public Map<Integer,List<Card>> playerCards = new HashMap<Integer, List<Card>>();
        public int playsNext;
        public int singlePlayerPoints;
        public List<Trick> playedTricks = new ArrayList<Trick>();
        public List<Card> cardsPlayed = new ArrayList<Card>();

        public GameStatus play(int player, Card card) {
            GameStatus status = new GameStatus();
            status.playsNext = playsNext;
            status.singlePlayerPoints = singlePlayerPoints;
            status.playedTricks = new ArrayList<Trick>(playedTricks);
            status.cardsPlayed = new ArrayList<Card>(cardsPlayed);
            status.cardsPlayed.add(card);
            status.playerCards.put(0, new ArrayList<Card>(playerCards.get(0)));
            status.playerCards.put(1, new ArrayList<Card>(playerCards.get(1)));
            status.playerCards.put(2, new ArrayList<Card>(playerCards.get(2)));
            status.playerCards.get(player).remove(card);
            if (status.cardsPlayed.size() == 3) {
                //the trick is full, calculate points etc
                Trick trick = new Trick();
                trick.cards = new ArrayList<Card>(status.cardsPlayed);
                trick.points = trick.cards.get(0).getValue() + trick.cards.get(1).getValue() + trick.cards.get(2).getValue();
                Card winningCard = game.getWinningCard(trick.cards.get(0), trick.cards.get(1), trick.cards.get(2));
                int winner = (playsNext + status.cardsPlayed.indexOf(winningCard)) % 3;
                if (winner == singlePlayer) status.singlePlayerPoints += trick.points;
                status.playedTricks.add(trick);
                status.cardsPlayed.clear();
                status.playsNext = winner;
            }
            return status;
        }
    }
}
