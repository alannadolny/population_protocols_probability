import java.io.Serializable;
import java.util.*;

import javafx.util.Pair;

public class GenerateEquation {

    private final int size;

    Map<Pair<String, String>, Pair<String, String>> transitionFunction = new HashMap<>(Map.of(
            new Pair<>("T", "T"), new Pair<>("T", "T"),
            new Pair<>("N", "T"), new Pair<>("U", "U"),
            new Pair<>("T", "N"), new Pair<>("U", "U"),
            new Pair<>("N", "N"), new Pair<>("N", "N"),
            new Pair<>("U", "U"), new Pair<>("U", "U"),
            new Pair<>("T", "U"), new Pair<>("T", "T"),
            new Pair<>("U", "T"), new Pair<>("T", "T"),
            new Pair<>("N", "U"), new Pair<>("N", "N"),
            new Pair<>("U", "N"), new Pair<>("N", "N")
    ));

    public int getSize() {
        return size;
    }

    public GenerateEquation(int size) {
        this.size = size;
    }

    public ArrayList<String> GenerateVoters(int t, int n) {
        ArrayList<String> allVoters = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            allVoters.add("T");
        }
        for (int i = 0; i < n; i++) {
            allVoters.add("N");
        }
        for (int i = 0; i < (this.size - t - n); i++) {
            allVoters.add("U");
        }
        return allVoters;
    }

    public ArrayList<Integer> sumVotes(ArrayList<String> votes) {
        int votesFor = 0, votesAgainst = 0, unknown = 0;

        for (String el : votes) {
            if (el.equals("T")) votesFor++;
            if (el.equals("N")) votesAgainst++;
            if (el.equals("U")) unknown++;
        }

        return new ArrayList<>(Arrays.asList(votesFor, votesAgainst, unknown));
    }

}
