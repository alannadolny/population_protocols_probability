package MonteCarlo;

import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SolveMatrix {

    private final Integer amountOfVoters;
    private ArrayList<Double> result;

    public SolveMatrix(Integer amountOfVoters) {
        this.result = new ArrayList<>();
        this.amountOfVoters = amountOfVoters;
    }

    public ArrayList<Double> getResult() {
        return result;
    }

    public Map<Integer, Pair<Integer, Integer>> generateIndexes() {
        Map<Integer, Pair<Integer, Integer>> indexes = new HashMap<>(Map.of());
        int t = 0, n = 0, counter = 0;
        while (t + n <= this.amountOfVoters) {
            indexes.put(counter, new Pair<>(t, n));
            if (n + t == this.amountOfVoters) {
                n = 0;
                t++;
            } else {
                n++;
            }
            counter++;
        }
        return indexes;
    }

    public void solve() {
        Map<Integer, Pair<Integer, Integer>> indexes = generateIndexes();

        for (int i = 0; i < indexes.size(); i++) {
            MonteCarlo monteCarlo = new MonteCarlo(indexes.get(i).getKey(), indexes.get(i).getValue(), this.amountOfVoters);
            result.add(monteCarlo.symulate());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        DecimalFormat df = new DecimalFormat("+0.00;-0.00");
        for (Double el : this.result) {
            result.append(df.format(el)).append("\n");
        }
        return result.toString();
    }

}
