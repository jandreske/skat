package smart;

public class Cards {

    public static final long DIAMONDS_SEVEN =    1;
    public static final long DIAMONDS_EIGHT =    2;
    public static final long DIAMONDS_NINE =     4;
    public static final long DIAMONDS_QUEEN =    8;
    public static final long DIAMONDS_KING =     16;
    public static final long DIAMONDS_TEN =      32;
    public static final long DIAMONDS_ACE =      64;

    public static final long HEARTS_SEVEN =      128;
    public static final long HEARTS_EIGHT =      256;
    public static final long HEARTS_NINE =       512;
    public static final long HEARTS_QUEEN =      1024;
    public static final long HEARTS_KING =       2048;
    public static final long HEARTS_TEN =        4096;
    public static final long HEARTS_ACE =        8192;

    public static final long SPADES_SEVEN =      16384;
    public static final long SPADES_EIGHT =      32768;
    public static final long SPADES_NINE =       65536;
    public static final long SPADES_QUEEN =      131072;
    public static final long SPADES_KING =       262144;
    public static final long SPADES_TEN =        524288;
    public static final long SPADES_ACE =        1048576;

    public static final long CLUBS_SEVEN =       2097152;
    public static final long CLUBS_EIGHT =       4194304;
    public static final long CLUBS_NINE =        8388608;
    public static final long CLUBS_QUEEN =       16777216;
    public static final long CLUBS_KING =        33554432;
    public static final long CLUBS_TEN =         67108864;
    public static final long CLUBS_ACE =         134217728;

    public static final long DIAMONDS_JACK =     268435456;
    public static final long HEARTS_JACK =       536870912;
    public static final long SPADES_JACK =       1073741824;
    public static final long CLUBS_JACK =        2147483648L;

    public static final long DIAMONDS =     128 - 1;
    public static final long HEARTS =       16384 - 128;
    public static final long SPADES =       2097152 - 16384;
    public static final long CLUBS =        268435456 - 2097152;
    public static final long JACKS =        4294967296L - 268435456;

    public static final long TWO_POINTS =       JACKS;
    public static final long THREE_POINTS =     DIAMONDS_QUEEN | HEARTS_QUEEN | SPADES_QUEEN | CLUBS_QUEEN;
    public static final long FOUR_POINTS =      DIAMONDS_KING | HEARTS_KING | SPADES_KING | CLUBS_KING;
    public static final long TEN_POINTS =       DIAMONDS_TEN | HEARTS_TEN | SPADES_TEN | CLUBS_TEN;
    public static final long ELEVEN_POINTS =    DIAMONDS_ACE | HEARTS_ACE | SPADES_ACE | CLUBS_ACE;

    public static final long REDUNDANT_FILTER = DIAMONDS_EIGHT | SPADES_EIGHT | CLUBS_EIGHT | HEARTS_EIGHT;

}
