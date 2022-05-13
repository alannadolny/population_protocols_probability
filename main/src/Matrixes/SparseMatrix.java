package Matrixes;

import Variables.Operations;
import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SparseMatrix<T extends Operations<T>> {

    private final Map<Pair<Integer, Integer>, T> sparseMatrix;

    private final GenerateEquation generateEquation;
    private final T typeElement;
    private final int numRows;
    private final int probability;

    public Map<Pair<Integer, Integer>, T> getSparseMatrix() {
        return this.sparseMatrix;
    }

    public T getTypeElement() {
        return this.typeElement;
    }

    public void setElement(Integer x, Integer y, T value) {
        this.sparseMatrix.put(new Pair<>(x, y), value);
    }

    public Map<Pair<Integer, Integer>, Integer> generateIndexes() {
        Map<Pair<Integer, Integer>, Integer> indexes = new HashMap<>(Map.of());
        int t = 0, n = 0, counter = 0;
        while (t + n <= this.generateEquation.getSize()) {
            indexes.put(new Pair<>(t, n), counter);
            if (n + t == this.generateEquation.getSize()) {
                n = 0;
                t++;
            } else {
                n++;
            }
            counter++;
        }
        return indexes;
    }

    public SparseMatrix(GenerateEquation generateEquation, T typeElement) {
        this.sparseMatrix = new HashMap<>(Map.of());
        this.generateEquation = generateEquation;
        this.typeElement = typeElement;
        this.numRows = generateIndexes().size();
        this.probability = this.generateEquation.getSize() * (this.generateEquation.getSize() - 1);
    }

    public SparseMatrix(NormalMatrix<T> matrix, NormalMatrix<T> vector) {
        this.typeElement = matrix.getMatrix().get(0).get(0).initializeWithZero();
        this.generateEquation = new GenerateEquation(0);
        this.sparseMatrix = new HashMap<>(Map.of());
        this.numRows = matrix.countRows();
        this.probability = this.generateEquation.getSize() * (this.generateEquation.getSize() - 1);
        for (int i = 0; i < matrix.countRows(); i++) {
            for (int j = 0; j < matrix.countColumns(); j++) {
                if (matrix.getMatrix().get(i).get(j).compare(this.typeElement) != 0) {
                    this.sparseMatrix.put(new Pair<>(i, j), matrix.getMatrix().get(i).get(j));
                }
            }
            if (vector.getMatrix().get(i).get(0).compare(this.typeElement) != 0) {
                this.sparseMatrix.put(new Pair<>(i, matrix.countColumns()), vector.getMatrix().get(i).get(0));
            }
        }
    }

    public void fillMatrix() {
        int size = this.generateEquation.getSize();
        int n = 0, t = 0;
        int rowIndex = 0;

        Map<Pair<Integer, Integer>, Integer> indexes = generateIndexes();

        while (t + n <= size) {
            ArrayList<String> currentVoters = this.generateEquation.GenerateVoters(t, n);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == j) continue;
                    ArrayList<String> changedVoters = new ArrayList<>(currentVoters);
                    String first = changedVoters.get(i);
                    String second = changedVoters.get(j);
                    Pair<String, String> afterTransition = this.generateEquation.transitionFunction.get(new Pair<>(first, second));
                    changedVoters.remove(first);
                    changedVoters.remove(second);
                    changedVoters.add(afterTransition.getKey());
                    changedVoters.add(afterTransition.getValue());
                    ArrayList<Integer> results = this.generateEquation.sumVotes(changedVoters);
                    Integer columnIndex = indexes.get(new Pair<>(results.get(0), results.get(1)));
                    T toPut = this.typeElement.initializeWithOne();
                    if (this.sparseMatrix.containsKey(new Pair<>(rowIndex, columnIndex))) {
                        this.sparseMatrix.get(new Pair<>(rowIndex, columnIndex)).add(toPut);
                    } else {
                        this.sparseMatrix.put(new Pair<>(rowIndex, columnIndex), toPut);
                    }
                }
            }

            if (n + t == size) {
                n = 0;
                t++;
            } else {
                n++;
            }
            rowIndex++;
        }

        for (Map.Entry<Pair<Integer, Integer>, T> entry : this.getSparseMatrix().entrySet()) {
            entry.getValue().divide(this.typeElement.initizalizeWithInteger(this.probability));
        }

        for (int i = 0; i < rowIndex - 1; i++) {
            Pair<Integer, Integer> leadingPair = new Pair<>(i, i);
            if (this.getSparseMatrix().containsKey(leadingPair)) {
                T currentElement = this.getSparseMatrix().get(leadingPair);
                if (currentElement.returnValue().equals(this.typeElement.initializeWithOne().returnValue())) {
                    currentElement.reverseSign();
                } else {
                    currentElement.subtract(this.getTypeElement().initializeWithOne());
                }
            } else {
                T temp = this.getTypeElement().initializeWithOne();
                temp.reverseSign();
                this.getSparseMatrix().put(leadingPair, temp);
            }
        }

        this.sparseMatrix.put(new Pair<>(rowIndex - 1, rowIndex), this.typeElement.initializeWithOne());
    }

    public Integer countRows() {
        return this.numRows;
    }

    public Integer countColumns() {
        return this.numRows + 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        DecimalFormat df = new DecimalFormat("+0.0000;-0.0000");
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numRows + 1; j++) {
                if (this.sparseMatrix.containsKey(new Pair<>(i, j)))
                    result.append(df.format(this.sparseMatrix.get(new Pair<>(i, j)).returnValue())).append(" ");
                else result.append("+X.XXXX ");
            }
            result.append("\n");
        }
        return result.toString();
    }

}