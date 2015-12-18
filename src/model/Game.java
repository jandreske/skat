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

    private Player getWinningPlayer(Card winningCard) {
        if (params.forehand.getCards().contains(winningCard)) return params.forehand;
        if (params.middlehand.getCards().contains(winningCard)) return params.middlehand;
        if (params.rearhand.getCards().contains(winningCard)) return params.rearhand;
        throw new RuntimeException("No player has the winning card, something went horribly wrong.");
    }

    private Card getWinningCard(Card firstCard, Card secondCard, Card thirdCard) {
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
            //no trum involved, so we check which cards are of correct color
            Color color = firstCard.color;
            for (Card card : cards) {
                if (card.color == color) potentialWinners.add(card);
            }
        }

        //among all cards still in here, the one with the highest value wins
        Card winner = potentialWinners.get(0);
        for (Card card : cards) {
            if (card.getValue() > winner.getValue()) winner = card;
            if (card.getValue() == winner.getValue()) {
                //todo: make better
                //this is for jacks, clubs over spades over hearts over diamonds
                if (card.color == Color.CLUBS) winner = card;
                if (card.color == Color.SPADES && winner.color != Color.CLUBS) winner = card;
                if (card.color == Color.HEARTS && winner.color != Color.CLUBS && winner.color != Color.SPADES) winner = card;
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
        //check that if trump was played, the card is either trump or the player has no trump
        if (trump && !params.isTrump(card) && player.hasTrump(params)) {
            throw new RuntimeException("Invalid player, player did not play trump although he could and had to");
        }
        if (color != card.color && player.hasColor(color)) {
            throw new RuntimeException("Invalid player, player did not play right color although he could");
        }
    }

    private void checkHasCard(Player player, Card card) {
        if (!player.getCards().contains(card)) {
            throw new RuntimeException("Tried to play a card the player has not in hand.");
        }
    }

    private Player getNextPlayer(Player player) {
        if (player == params.forehand) return params.middlehand;
        if (player == params.middlehand) return params.rearhand;
        if (player == params.rearhand) return params.forehand;
        throw new IllegalArgumentException("model.Player not present in model.GameParams.");
    }
}
