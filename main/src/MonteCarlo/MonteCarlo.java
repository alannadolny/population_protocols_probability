package MonteCarlo;

import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonteCarlo {

    private final Integer For;
    private final Integer Against;
    private final Integer AllVoters;
    private final Long amountOfTests;
    private final ArrayList<String> arrayOfProbability;

    private final Map<Pair<String, String>, Pair<String, String>> transitionFunction = new HashMap<>(Map.of(
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

    public MonteCarlo(Integer aFor, Integer against, Integer allVoters, Long symulations) {
        this.arrayOfProbability = new ArrayList<>();
        this.For = aFor;
        this.Against = against;
        this.AllVoters = allVoters;
        this.amountOfTests = symulations;
    }

    public boolean checkStableState(ArrayList<Voter> voters) {
        for (int i = 0; i < voters.size() - 1; i++) {
            if (!voters.get(i).getVote().equals(voters.get(i + 1).getVote())) return true;
        }
        return false;
    }

    public ArrayList<Voter> generateVoters() {
        ArrayList<Voter> votersList = new ArrayList<>();
        for (int i = 0; i < this.For; i++) {
            votersList.add(new Voter("T"));
        }
        for (int i = 0; i < this.Against; i++) {
            votersList.add(new Voter("N"));
        }
        for (int i = 0; i < this.AllVoters - this.For - this.Against; i++) {
            votersList.add(new Voter("U"));
        }
        return votersList;
    }

    public Double calculateProbability(ArrayList<String> votes) {
        double forVotes = 0, allVotes = votes.size();
        for (String vote : votes) {
            if (vote.equals("T")) forVotes++;
        }
        return (forVotes / allVotes);
    }

    public Double symulate() {
        for (int i = 0; i < amountOfTests; i++) {
            ArrayList<Voter> votersToSymulate = generateVoters();
            while (checkStableState(votersToSymulate)) {
                Voter firstVoter = votersToSymulate.get((int) (Math.random() * votersToSymulate.size()));
                Voter secondVoter = votersToSymulate.get((int) (Math.random() * votersToSymulate.size()));
                Pair<String, String> newPair = transitionFunction.get(new Pair<>(firstVoter.getVote(), secondVoter.getVote()));
                firstVoter.setVote(newPair.getKey());
                secondVoter.setVote(newPair.getValue());
            }
            this.arrayOfProbability.add(votersToSymulate.get(0).getVote());
        }
        return calculateProbability(this.arrayOfProbability);
    }

}
