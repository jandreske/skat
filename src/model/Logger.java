package model;

import java.util.List;

public class Logger {
    //todo: add options to log to file etc

    private boolean sysOut;

    public Logger(boolean sysOut) {
        this.sysOut = sysOut;
    }

    public void log(String message) {
        if (sysOut) {
            System.out.println(message);
        }
    }

    public void logCards(String name, List<Card> cards) {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": ");
        for (Card card : cards) {
            builder.append(card).append(", ");
        }
        //todo cut off trailing comma
        log(builder.toString());
    }
}
