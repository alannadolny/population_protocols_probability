import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class SparseMatrix {

    private Map<Pair<Integer, Integer>, Float> sparseMatrix; //in the future it should be generic

    private final GenerateEquation generateEquation;
    private int counter = 0;

    public Map<Pair<Integer, Integer>, Float> getSparseMatrix() {
        return this.sparseMatrix;
    }

    public SparseMatrix(GenerateEquation generateEquation) {
        this.sparseMatrix = new HashMap<>(Map.of());
        this.generateEquation = generateEquation;
    }

    public void fillMatrix() {
        int t = 0, n = 0;
        ArrayList<ArrayList<ArrayList<Integer>>> possibleChanges = new ArrayList<>();
        ArrayList<Map<ArrayList<Integer>, Long>> groupedVotes = new ArrayList<>();

        while (t + n <= this.generateEquation.getSize()) {
            possibleChanges.add(new ArrayList<>());
            ArrayList<String> possibleVotes = this.generateEquation.GenerateVoters(t, n);

            for (int i = 0; i < this.generateEquation.getSize(); i++) {
                for (int j = 0; j < this.generateEquation.getSize(); j++) {

                    ArrayList<String> tempElements = new ArrayList<>();
                    ArrayList<String> copyOfPossibleVotes = new ArrayList<>(possibleVotes);
                    copyOfPossibleVotes.remove(possibleVotes.get(i));
                    copyOfPossibleVotes.remove(possibleVotes.get(j));
                    if (i == j) continue;
                    tempElements.add(generateEquation.transitionFunction.get(new Pair<>(possibleVotes.get(i), possibleVotes.get(j))).getValue());
                    tempElements.add(copyOfPossibleVotes.get(0));
                    tempElements.add(generateEquation.transitionFunction.get(new Pair<>(possibleVotes.get(i), possibleVotes.get(j))).getKey());

                    possibleChanges.get(this.counter).add(generateEquation.sumVotes(tempElements));
                }
            }
            counter++;

            if (n + t == this.generateEquation.getSize()) {
                n = 0;
                t++;
            } else {
                n++;
            }
        }

        for (ArrayList<ArrayList<Integer>> el : possibleChanges) {
            groupedVotes.add(generateEquation.groupVotes(el));
        }

        for (int i = 0; i < groupedVotes.size(); i++) {
            for (Map.Entry<ArrayList<Integer>, Long> entry : groupedVotes.get(i).entrySet()) {
                sparseMatrix.put(new Pair<>(i, (entry.getKey().get(0) * 3) + entry.getKey().get(1)), (entry.getValue() / (float) generateEquation.getVotesQuantity(groupedVotes.get(0))));
            }
        }
        
        for (int i = 0; i < generateEquation.getSize() * generateEquation.getSize(); i++) {
            if (this.sparseMatrix.containsKey(new Pair<>(i, i)) && this.sparseMatrix.get(new Pair<>(i, i)) != 1.0)
                this.sparseMatrix.put(new Pair<>(i, i), 1F - this.sparseMatrix.get(new Pair<>(i, i)));
            else sparseMatrix.put(new Pair<>(i, i), 1F);
        }
        this.sparseMatrix.put(new Pair<>(counter - 1, counter), 1F);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter + 1; j++) {
                if (this.sparseMatrix.containsKey(new Pair<>(i, j)))
                    result.append(df.format(this.sparseMatrix.get(new Pair<>(i, j)))).append(" ");
                else result.append("0,00 ");
            }
            result.append("\n");
        }
        return result.toString();
    }

}
