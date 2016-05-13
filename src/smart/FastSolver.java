package smart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FastSolver {
    private int singlePlayer;
    private long trump;

    private boolean _log;
    private Stats _stats;

    public FastSolver(boolean collectStats) {
        _log = collectStats;
        _stats = new Stats();
    }

    public List<Long> calculateFullGame(int singlePlayer, long trump, int pointsInSkat, int playsFirst, long player0Cards, long player1Cards, long player2Cards) {
        this.singlePlayer = singlePlayer;
        this.trump = trump;

        GameStatus status = new GameStatus();
        status.singlePlayerPoints = pointsInSkat;
        status.playsNext = playsFirst;
        status.player0cards = player0Cards;
        status.player1cards = player1Cards;
        status.player2cards = player2Cards;
        status.cardsPlayed = new long[3];
        status.playedTricks = new ArrayList<Long>();
        GameStatus result = solve(status, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return result.playedTricks;
    }

    private GameStatus solve(GameStatus status, int alpha, int beta, int level) {
        int offset = 0;
        if (status.cardsPlayed[0] != 0) offset++;
        if (status.cardsPlayed[1] != 0) offset++;
        int player = (status.playsNext + offset) % 3;
        GameStatus alphaNode = null;
        GameStatus betaNode = null;
        long cards = 0;
        switch (player) {
            case 0 : cards = status.player0cards; break;
            case 1 : cards = status.player1cards; break;
            case 2 : cards = status.player2cards; break;
        }
        if (status.cardsPlayed[0] != 0) {
            //we have to check whats actually allowed
            cards = CardUtils.getAllowedCards(status.cardsPlayed[0], cards, trump);
        }

        //if there are no more cards, we are done
        if (cards == 0) {
            return status;
        }

        //remove redundant cards seems to slow us down
        //cards = filterCards(cards);

        //find the best card
        if (level < 0) { //todo
            ExecutorService executor = Executors.newFixedThreadPool(8);
            List<FutureTask<GameStatus>> taskList = new ArrayList<FutureTask<GameStatus>>();
            for (long card : CardUtils.getAllCards(cards)) {
                FutureTask<GameStatus> futureTask = new FutureTask<GameStatus>(new PlayTask(status, player, card, alpha, beta, level));
                taskList.add(futureTask);
                executor.execute(futureTask);
            }
            for (FutureTask<GameStatus> task : taskList) {
                GameStatus newStatus = null;
                try {
                    newStatus = task.get();

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
                    if (alpha >= beta) {
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            executor.shutdown();
        } else {
            for (long card : CardUtils.getAllCards(cards)) {
                GameStatus newStatus = status.play(player, card);
                newStatus = solve(newStatus, alpha, beta, level + 1);

//                if (level == 1) {
//                    System.out.println(CardUtils.toString(card) + ": " + newStatus.singlePlayerPoints);
//                }

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
                if (alpha >= beta) {
                    if (_log) _stats.alphaBetaBreak(level);
                    break;
                }
            }
        }

        if (_log && level == 0) {
            _stats.dump();
        }
        if (player == singlePlayer) {
            //we are a MAX node
            return alphaNode;
        } else {
            //we are a MIN node
            return betaNode;
        }
    }

    private long filterCards(long cards) {
        if (!CardUtils.contains(cards, Cards.REDUNDANT_FILTER)) return cards;
        if (CardUtils.contains(cards, Cards.DIAMONDS_EIGHT)) {
            cards = CardUtils.removeCard(cards, Cards.DIAMONDS_SEVEN);
            cards = CardUtils.removeCard(cards, Cards.DIAMONDS_NINE);
        }
        if (CardUtils.contains(cards, Cards.HEARTS_EIGHT)) {
            cards = CardUtils.removeCard(cards, Cards.HEARTS_SEVEN);
            cards = CardUtils.removeCard(cards, Cards.HEARTS_NINE);
        }
        if (CardUtils.contains(cards, Cards.SPADES_EIGHT)) {
            cards = CardUtils.removeCard(cards, Cards.SPADES_SEVEN);
            cards = CardUtils.removeCard(cards, Cards.SPADES_NINE);
        }
        if (CardUtils.contains(cards, Cards.CLUBS_EIGHT)) {
            cards = CardUtils.removeCard(cards, Cards.CLUBS_SEVEN);
            cards = CardUtils.removeCard(cards, Cards.CLUBS_NINE);
        }
        return cards;
    }

    private class PlayTask implements Callable<GameStatus> {

        private final GameStatus _status;
        private final long _card;
        private final int _player;
        private final int _alpha;
        private final int _beta;
        private final int _level;

        PlayTask(GameStatus status, int player, long card, int alpha, int beta, int level) {
            _status = status;
            _player = player;
            _card = card;
            _alpha = alpha;
            _beta = beta;
            _level = level;
        }

        @Override
        public GameStatus call() throws Exception {
            GameStatus newStatus = _status.play(_player, _card);
            newStatus = solve(newStatus, _alpha, _beta, _level + 1);
            return newStatus;
        }
    }

    private class GameStatus {
        public long player0cards;
        public long player1cards;
        public long player2cards;
        public int playsNext;
        public int singlePlayerPoints;
        public List<Long> playedTricks;
        public long[] cardsPlayed;

        public GameStatus play(int player, long card) {
            long start = System.currentTimeMillis();
            GameStatus status = new GameStatus();
            status.playsNext = playsNext;
            status.singlePlayerPoints = singlePlayerPoints;
            status.playedTricks = new ArrayList<Long>(playedTricks);
            status.cardsPlayed = arrayCopy(cardsPlayed, card);
            switch (player) {
                case 0:
                    status.player0cards = CardUtils.removeCard(player0cards, card);
                    status.player1cards = player1cards;
                    status.player2cards = player2cards;
                    break;
                case 1:
                    status.player0cards = player0cards;
                    status.player1cards = CardUtils.removeCard(player1cards, card);
                    status.player2cards = player2cards;
                    break;
                case 2:
                    status.player0cards = player0cards;
                    status.player1cards = player1cards;
                    status.player2cards = CardUtils.removeCard(player2cards, card);
                    break;
            }
            if (_log) _stats.statusCopy(System.currentTimeMillis() - start);
            start = System.currentTimeMillis();
            if (status.cardsPlayed[2] != 0) {
                //the trick is full, calculate points etc
                int cardWins = CardUtils.getWinningCardIndex(status.cardsPlayed[0], status.cardsPlayed[1], status.cardsPlayed[2], trump);
                int winner = (playsNext + cardWins) % 3;
                if (winner == singlePlayer) {
                    int points = CardUtils.getPoints(status.cardsPlayed[0] | status.cardsPlayed[1] | status.cardsPlayed[2]);
                    status.singlePlayerPoints += points;
                }
                status.playedTricks.add(status.cardsPlayed[0]);
                status.playedTricks.add(status.cardsPlayed[1]);
                status.playedTricks.add(status.cardsPlayed[2]);
                status.cardsPlayed = new long[3];
                status.playsNext = winner;
                if (_log) _stats.statusTrick(System.currentTimeMillis() - start);
            }
            return status;
        }
    }

    private long[] arrayCopy(long[] cardsPlayed, long card) {
        long[] copy = new long[3];
        if (cardsPlayed[0] == 0) {
            copy[0] = card;
            copy[1] = 0;
            copy[2] = 0;
            return copy;
        }
        if (cardsPlayed[1] == 0) {
            copy[0] = cardsPlayed[0];
            copy[1] = card;
            copy[2] = 0;
            return copy;
        }
        if (cardsPlayed[2] == 0) {
            copy[0] = cardsPlayed[0];
            copy[1] = cardsPlayed[1];
            copy[2] = card;
            return copy;
        }
        throw new RuntimeException("cardsplayed broken");
    }
}
