import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

public class SparseMatrix {

    private Map<Pair<Integer, Integer>, Float> sparseMatrix; //in the future it should be generic

    private final GenerateEquation generateEquation;

    public Map<Pair<Integer, Integer>, Float> getSparseMatrix() {
        return this.sparseMatrix;
    }

    public SparseMatrix(GenerateEquation generateEquation) {
        this.sparseMatrix =  new HashMap<>(Map.of());
        this.generateEquation = generateEquation;
    }

    public void fillMatrix() {
        int t = 0, n = 0;
        ArrayList<ArrayList<ArrayList<String>>> possibleChanges = new ArrayList<>();

        for (int i = 0; i < generateEquation.getSize() * generateEquation.getSize(); i++) {
            sparseMatrix.put(new Pair<>(i, i), 1F);
        }
        int counter = 0;

        while (t + n <= this.generateEquation.getSize()) {
            possibleChanges.add(new ArrayList<>());
            ArrayList<String> possibleVotes = this.generateEquation.GenerateVoters(t, n);

            for (int i = 0; i < this.generateEquation.getSize(); i++) {
                for(int j = 0; j < this.generateEquation.getSize(); j++) {
                    
                    ArrayList<String> tempElements = new ArrayList<>();
                    ArrayList<String> copyOfPossibleVotes = new ArrayList<>(possibleVotes);
                    copyOfPossibleVotes.remove(possibleVotes.get(i));
                    copyOfPossibleVotes.remove(possibleVotes.get(j));
                    if(i == j) continue;
                    tempElements.add(generateEquation.transitionFunction.get(new Pair<>(possibleVotes.get(i), possibleVotes.get(j))).getValue());
                    tempElements.add(copyOfPossibleVotes.get(0));
                    tempElements.add(generateEquation.transitionFunction.get(new Pair<>(possibleVotes.get(i), possibleVotes.get(j))).getKey());

                    possibleChanges.get(counter).add(tempElements);
                    //System.out.println(tempElements);
                }
            }
            counter++;
            //System.out.println("========================");

            if (n + t == this.generateEquation.getSize()) {
                n = 0;
                t++;
            } else {
                n++;
            }
        }
        System.out.println(possibleChanges);
    }

}
