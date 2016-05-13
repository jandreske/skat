package smart;

import java.util.*;

public class Stats {

    private Map<Integer, Integer> solveCallMap = new HashMap();
    private Map<Integer, Integer> abBreakMap = new HashMap();
    private int playcount = 0;
    private long playtime = 0;
    private int solvecount = 0;
    private long solvetime = 0;
    private int copycount = 0;
    private long copytime = 0;
    private int trickcount = 0;
    private long tricktime = 0;

    public void solveCall(int level) {
        if (solveCallMap.containsKey(level)) {
            solveCallMap.put(level, solveCallMap.get(level) + 1);
        } else {
            solveCallMap.put(level, 1);
        }
    }

    public void alphaBetaBreak(int level) {
        if (abBreakMap.containsKey(level)) {
            abBreakMap.put(level, abBreakMap.get(level) + 1);
        } else {
            abBreakMap.put(level, 1);
        }
    }

    public void playTime(long l) {
        playcount++;
        playtime += l;
    }

    public void solveTime(long l) {
        solvecount++;
        solvetime += l;
    }

    public void dump() {
        StringBuilder builder = new StringBuilder();
        builder.append(playcount + " plays in " + playtime + "\n");
        builder.append(solvecount + " solves in " + solvetime  + "\n");
        builder.append(copycount + " copies in " + copytime  + "\n");
        builder.append(trickcount + " tricks in " + tricktime  + "\n");
        List<Integer> levels = new ArrayList<Integer>(solveCallMap.keySet());
        Collections.sort(levels);
        for (int level : levels) {
            builder.append("level " + level + ": " + solveCallMap.get(level) + " calls, " + abBreakMap.get(level) + " breaks"  + "\n");
        }
        System.out.print(builder.toString());
    }

    public void statusCopy(long l) {
        copycount++;
        copytime += l;
    }

    public void statusTrick(long l) {
        trickcount++;
        tricktime += l;
    }
}
