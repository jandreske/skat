package model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameParams params;
    private Logger logger;

    //apparently, "Stich" is "trick" in English
    private int tricksPlayed;
    private Player playsNext;

    private List<Card> skat;

    public Game(GameParams params, Logger logger) {
        this.params = params;
        playsNext = params.forehand;
        this.logger = logger;
    }

    public GameParams getParams() {
        return params;
    }

    public void dealCards(List<Card> vorhand, List<Card> mittelhand, List<Card> hinterhand, List<Card> skat) {
        //todo verify numbers, no duplicates, etc
        params.forehand.dealCards(vorhand);
        logger.logCards(params.forehand.name, vorhand);
        params.middlehand.dealCards(mittelhand);
        logger.logCards(params.middlehand.name, mittelhand);
        params.rearhand.dealCards(hinterhand);
        logger.logCards(params.rearhand.name, hinterhand);
        this.skat = new ArrayList<Card>(skat);
        logger.logCards("Skat", skat);
    }

    public void changeSkat(Player player) {
        //todo: check that dealcards was called before
        //todo: implement "Drücken", for now we just assume the guy keeps the two cards in skat
        player.makeSinglePlayer();
        int points = 0;
        for (Card card : skat) {
            points += card.getValue();
        }
        player.earnPoints(points);
        logger.log(player.name + " gets " + points + " for putting these cards to the skat: " + skat.get(0) + ", " + skat.get(1));
    }

    public void playTrick(List<Card> cards) {
        if (cards.size() != 3) throw new IllegalArgumentException("Invalid number of cards for one trick.");
        playTrick(cards.get(0), cards.get(1), cards.get(2));
    }

    public void playTrick(Card firstCard, Card secondCard, Card thirdCard) {
        //todo: check that changeskat was called before
        Player firstPlayer = playsNext;
        Player secondPlayer = getNextPlayer(firstPlayer);
        Player thirdPlayer = getNextPlayer(secondPlayer);

        //make sure all plays are legal
        checkHasCard(firstPlayer, firstCard);
        checkHasCard(secondPlayer, secondCard);
        checkHasCard(thirdPlayer, thirdCard);
        checkValidPlays(firstCard, secondCard, secondPlayer, thirdCard, thirdPlayer);

        //figure out the points and who won the trick
        int points = firstCard.getValue() + secondCard.getValue() + thirdCard.getValue();
        Card winningCard = getWinningCard(firstCard, secondCard, thirdCard);
        Player winningPlayer = getWinningPlayer(winningCard);

        //actually play the cards, add the points and save who goes next
        firstPlayer.playCard(firstCard);
        secondPlayer.playCard(secondCard);
        thirdPlayer.playCard(thirdCard);
        winningPlayer.earnPoints(points);
        playsNext = winningPlayer;
        tricksPlayed ++;
        logger.log("Cards played: " + firstPlayer.name + ": " + firstCard + ", " + secondPlayer.name + ": " + secondCard + ", " + thirdPlayer.name + ": " + thirdCard);
        logger.log(winningPlayer.name + " wins the trick and gets " + points + " points.");

        //todo: check whether game is over and who won
    }

    public Player getWinningPlayer(Card winningCard) {
        if (params.forehand.getCards().contains(winningCard)) return params.forehand;
        if (params.middlehand.getCards().contains(winningCard)) return params.middlehand;
        if (params.rearhand.getCards().contains(winningCard)) return params.rearhand;
        throw new RuntimeException("No player has the winning card, something went horribly wrong.");
    }

    public Card getWinningCard(Card firstCard, Card secondCard, Card thirdCard) {
        List<Card> cards = new ArrayList<Card>();
        cards.add(firstCard);
        cards.add(secondCard);
        cards.add(thirdCard);

        //check whether one is trump and if so, remove the non-trump cards as potential winners
        boolean trumpPlayed = false;
        for (Card card : cards) {
            if (params.isTrump(card)) {
                trumpPlayed = true;
                break;
            }
        }
        List<Card> potentialWinners = new ArrayList<Card>();
        if (trumpPlayed) {
            //a trump is involved, only trumps can win
            for (Card card : cards) {
                if (params.isTrump(card)) potentialWinners.add(card);
            }
        } else {
            //no trump involved, so we check which cards are of correct color
            Color color = firstCard.color;
            for (Card card : cards) {
                if (card.color == color) potentialWinners.add(card);
            }
        }

        //todo: sort this out - but beware, the current logic actually only applies to regular games where jacks are trump, always
        //among all cards still in here, the one with the highest value wins, except that jacks are higher
        Card winner = potentialWinners.get(0);
        for (Card card : potentialWinners) {
            if (card.getValue() > winner.getValue() && winner.cardType != CardType.JACK) winner = card;
            if (card.cardType == CardType.JACK) {
                if (card.getValue() == winner.getValue()) {
                    //this is for jacks, clubs over spades over hearts over diamonds
                    if (card.color == Color.CLUBS) winner = card;
                    if (card.color == Color.SPADES && winner.color != Color.CLUBS) winner = card;
                    if (card.color == Color.HEARTS && winner.color != Color.CLUBS && winner.color != Color.SPADES)
                        winner = card;
                } else {
                    winner = card;
                }
            }
        }

        return winner;
    }

    private void checkValidPlays(Card firstCard, Card secondCard, Player secondPlayer, Card thirdCard, Player thirdPlayer) {
        boolean trump = params.isTrump(firstCard);
        Color color = firstCard.color;
        checkValidPlay(trump, color, secondCard, secondPlayer);
        checkValidPlay(trump, color, thirdCard, thirdPlayer);
    }

    private void checkValidPlay(boolean trump, Color color, Card card, Player player) {
        if (!isValidPlay(trump, color, card, player)) {
            throw new RuntimeException("Invalid play, player did not play right color although he could");
        }
    }

    private boolean isValidPlay(boolean trump, Color color, Card card, Player player) {
        //if trump was played, and the player also played trump, he is fine
        if (trump && params.isTrump(card)) return true;
        //if trump was played, and the card is no trump, but the player has trump, it is not valid
        if (trump && !params.isTrump(card) && player.hasTrump(params)) return false;
        //if trump was played, and the card is no trump, and the player has no trump, he is fine
        if (trump && !params.isTrump(card) && !player.hasTrump(params)) return true;

        //if no trump was played, and the player played a non-trump card in the right color, he is fine
        if (!trump && color == card.color && !params.isTrump(card)) return true;
        //if no trump was played, and the player has no non-trump card in the right color, he is fine whatever he played
        if (!trump && !player.hasColorExcludeTrump(color, params)) return true;
        //if no trump was played, and the player played a trump, and he has non-trump cards in the right color, it is invalid
        if (!trump && params.isTrump(card) && player.hasColorExcludeTrump(color, params)) return false;
        //if no trump was played, and the player played a card of wrong color, and he has non trump cards in the right color, it is invalid
        if (!trump && color != card.color && player.hasColorExcludeTrump(color, params)) return false;
        //if none of the above options was hit, I made a stupid mistake
        throw new IllegalStateException("Bug in isValidPlay, no condition hit with trump: " + trump + ", color: " + color.name() + ", card: " + card);
    }

    private void checkHasCard(Player player, Card card) {
        if (!player.getCards().contains(card)) {
            throw new RuntimeException("Tried to play a card the player has not in hand.");
        }
    }

    public Player getNextPlayer(Player player) {
        if (player == params.forehand) return params.middlehand;
        if (player == params.middlehand) return params.rearhand;
        if (player == params.rearhand) return params.forehand;
        throw new IllegalArgumentException("model.Player not present in model.GameParams.");
    }

    public Player getPlaysNext() {
        return playsNext;
    }

    public List<Card> getAllowedPlays(Card firstCard, Player player) {
        //todo: this is not really efficient, but should work for now
        boolean trump = params.isTrump(firstCard);
        Color color = firstCard.color;
        List<Card> allowedCards = new ArrayList<Card>();
        for (Card card : player.getCards()) {
            if (isValidPlay(trump, color, card, player)) allowedCards.add(card);
        }
        return allowedCards;
    }

    public List<Card> getAllowedPlays(Card firstCard, List<Card> cards) {
        //todo: this is complicated and inefficient, change
        Player player = new Player("");
        player.dealCards(cards);
        return getAllowedPlays(firstCard, player);
    }

    public void logStatus() {
        logger.log("");
        logger.log(tricksPlayed + " tricks played.");
        Player single = null;
        if (params.forehand.isSinglePlayer()) single = params.forehand;
        if (params.middlehand.isSinglePlayer()) single = params.middlehand;
        if (params.rearhand.isSinglePlayer()) single = params.rearhand;
        if (single == null) throw new RuntimeException("No single player found.");
        logger.log("Singleplayer " + single.name + " collected " + single.getPoints() + " points.");
    }
}
