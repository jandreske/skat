package smart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastSolver {
    private int singlePlayer;
    private long trump;

    public List<Long> calculateFullGame(int singlePlayer, long trump, int pointsInSkat, int playsFirst, long player0Cards, long player1Cards, long player2Cards) {
        this.singlePlayer = singlePlayer;
        this.trump = trump;

        GameStatus status = new GameStatus();
        status.singlePlayerPoints = pointsInSkat;
        status.playsNext = playsFirst;
        status.playerCards.put(0, player0Cards);
        status.playerCards.put(1, player1Cards);
        status.playerCards.put(2, player2Cards);
        GameStatus result = solve(status, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return result.playedTricks;
    }

    private GameStatus solve(GameStatus status, int alpha, int beta, int level) {
        int player = (status.playsNext + status.cardsPlayed.size()) % 3;
        GameStatus alphaNode = null;
        GameStatus betaNode = null;
        long cards = status.playerCards.get(player);
        if (!status.cardsPlayed.isEmpty()) {
            //we have to check whats actually allowed
            cards = CardUtils.getAllowedCards(status.cardsPlayed.get(0), cards, trump);
        }
        //if there are no more cards, we are done
        if (cards == 0) return status;
        //find the best card
        for (long card : CardUtils.getAllCards(cards)) {
            GameStatus newStatus = solve(status.play(player, card), alpha, beta, level + 1);
            if (player == singlePlayer) {
                //we are a MAX node
                if (newStatus.singlePlayerPoints >= alpha || alphaNode == null) {
                    alpha = newStatus.singlePlayerPoints;
                    alphaNode = newStatus;
                }
            } else {
                //we are a MIN node
                if (newStatus.singlePlayerPoints <= beta || betaNode == null) {
                    beta = newStatus.singlePlayerPoints;
                    betaNode = newStatus;
                }
            }
            //if the result will be discarded one level up because it is too good / bad anyway, then stop
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

    private class GameStatus {
        public Map<Integer, Long> playerCards = new HashMap<Integer, Long>();
        public int playsNext;
        public int singlePlayerPoints;
        public List<Long> playedTricks = new ArrayList<Long>();
        public List<Long> cardsPlayed = new ArrayList<Long>();

        public GameStatus play(int player, long card) {
            GameStatus status = new GameStatus();
            status.playsNext = playsNext;
            status.singlePlayerPoints = singlePlayerPoints;
            status.playedTricks = new ArrayList<Long>(playedTricks);
            status.cardsPlayed = new ArrayList<Long>(cardsPlayed);
            status.cardsPlayed.add(card);
            status.playerCards.put(0, (playerCards.get(0)));
            status.playerCards.put(1, (playerCards.get(1)));
            status.playerCards.put(2, (playerCards.get(2)));
            status.playerCards.put(player, CardUtils.removeCard(playerCards.get(player), card));
            if (status.cardsPlayed.size() == 3) {
                //the trick is full, calculate points etc
                int cardWins = CardUtils.getWinningCardIndex(status.cardsPlayed.get(0), status.cardsPlayed.get(1), status.cardsPlayed.get(2), trump);
                int winner = (playsNext + cardWins) % 3;
                int points = CardUtils.getPoints(status.cardsPlayed.get(0) | status.cardsPlayed.get(1) | status.cardsPlayed.get(2));
                if (winner == singlePlayer) status.singlePlayerPoints += points;
                status.playedTricks.addAll(status.cardsPlayed);
                status.cardsPlayed.clear();
                status.playsNext = winner;
            }
            return status;
        }
    }
}
