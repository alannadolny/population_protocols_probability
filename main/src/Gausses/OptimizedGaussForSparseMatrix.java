package Gausses;

import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptimizedGaussForSparseMatrix<T extends Operations<T>> {

    public Integer findRowWithMaximumElement(SparseMatrix<T> matrix, Integer fromRow) {
        Map<Pair<Integer, Integer>, T> toOperate = matrix.getSparseMatrix().entrySet().stream().filter(el -> el.getKey().getKey().equals(fromRow) && el.getKey().getValue() > fromRow).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Integer index = fromRow;
        T maximumElement = matrix.getSparseMatrix().get(new Pair<>(fromRow, fromRow));

        for (Map.Entry<Pair<Integer, Integer>, T> pair : toOperate.entrySet()) {
            Integer key = pair.getKey().getKey();
            if ((maximumElement.absolute()).compare(matrix.getSparseMatrix().get(new Pair<>(key, fromRow)).absolute()) == -1) {
                maximumElement = maximumElement.initialize(matrix.getSparseMatrix().get(new Pair<>(key, fromRow)));
                index = key;
            }
        }

        return index;
    }

    public NormalMatrix<T> G(SparseMatrix<T> matrix, String mode) {
        List<T> results = new ArrayList<>();
        int numCols = matrix.countColumns();
        int numRows = matrix.countRows();
        int leadingNumber = 0;
        while (leadingNumber < numRows) {

            int finalLeadingNumber = leadingNumber;
            List<Integer> toOperate = matrix.getSparseMatrix().keySet().stream().filter(t -> t.getValue().equals(finalLeadingNumber) && t.getKey() > finalLeadingNumber).map(Pair::getKey).collect(Collectors.toList());

            int index = findRowWithMaximumElement(matrix, finalLeadingNumber);
            if (index != finalLeadingNumber) {
                for (int i = 0; i < numCols; i++) {
                    Pair<Integer, Integer> pair = new Pair<>(index, i);
                    Pair<Integer, Integer> pairWithLeading = new Pair<>(finalLeadingNumber, i);
                    boolean firstCondition = matrix.getSparseMatrix().containsKey(pair);
                    boolean secondCondition = matrix.getSparseMatrix().containsKey(pairWithLeading);
                    if (firstCondition && secondCondition) {
                        T firstNumber = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(index, i)));
                        matrix.getSparseMatrix().put(pair, matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairWithLeading)));
                        matrix.getSparseMatrix().put(pairWithLeading, firstNumber);
                    } else {
                        if (!firstCondition && secondCondition) {
                            matrix.getSparseMatrix().put(new Pair<>(index, i), matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairWithLeading)));
                            matrix.getSparseMatrix().remove(pairWithLeading);
                        } else if (firstCondition) {
                            matrix.getSparseMatrix().put(pairWithLeading, matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pair)));
                            matrix.getSparseMatrix().remove(new Pair<>(index, i));
                        }
                    }
                }
            }

            for (Integer el : toOperate) {
                T x = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(el, finalLeadingNumber)));
                x.divide(matrix.getSparseMatrix().get(new Pair<>(finalLeadingNumber, finalLeadingNumber)));
                for (int i = finalLeadingNumber + 1; i < numCols; i++) {
                    Pair<Integer, Integer> pairIJ = new Pair<>(el, i);
                    Pair<Integer, Integer> pairLeadingJ = new Pair<>(finalLeadingNumber, i);
                    if (matrix.getSparseMatrix().containsKey(pairLeadingJ)) {
                        T toSubtract = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairLeadingJ));
                        toSubtract.multiply(x);
                        if (matrix.getSparseMatrix().containsKey(pairIJ)) {
                            matrix.getSparseMatrix().get(pairIJ).subtract(toSubtract);
                        } else {
                            toSubtract.reverseSign();
                            matrix.getSparseMatrix().put(pairIJ, toSubtract);
                        }
                    }
                }
            }

            leadingNumber++;
        }

        for (int i = numRows - 1; i >= 0; i--) {
            int resultIndex;
            Pair<Integer, Integer> pairINumRows = new Pair<>(i, numRows);
            for (int j = numRows - 1; j > i; j--) {
                Pair<Integer, Integer> pairIJ = new Pair<>(i, j);
                T temp;
                resultIndex = numRows - 1 - j;
                if (matrix.getSparseMatrix().containsKey(pairIJ)) {
                    temp = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairIJ));
                    temp.multiply(matrix.getTypeElement().initialize(results.get(resultIndex)));
                } else temp = matrix.getTypeElement().initializeWithZero();
                if (matrix.getSparseMatrix().containsKey(pairINumRows))
                    matrix.getSparseMatrix().get(pairINumRows).subtract(temp);
                else {
                    temp.reverseSign();
                    matrix.getSparseMatrix().put(pairINumRows, temp);
                }
            }
            matrix.getSparseMatrix().get(pairINumRows).divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumber - 1, leadingNumber - 1)));
            results.add(matrix.getSparseMatrix().get(pairINumRows));
            leadingNumber--;
        }
        Collections.reverse(results);

        return new NormalMatrix<>(1, results);
    }


    public NormalMatrix<T> GJ(SparseMatrix<T> matrix, int iter) {
        List<T> results = new ArrayList<>();
        int numCols = matrix.countColumns() - 1;
        int numRows = matrix.countRows();
        for (int i = 0; i < numRows; i++) {
            results.add(matrix.getTypeElement().initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            List<T> newResult = new ArrayList<>();
            for (int i = 0; i < numRows; i++) {
                newResult.add(matrix.getTypeElement().initializeWithZero());
            }
            for (Map.Entry<Pair<Integer, Integer>, T> entry : matrix.getSparseMatrix().entrySet()) {
                if (!entry.getKey().getKey().equals(entry.getKey().getValue()) && entry.getKey().getValue() < numCols) {
                    T temp = results.get(entry.getKey().getValue()).initialize(results.get(entry.getKey().getValue()));
                    temp.multiply(entry.getValue());
                    temp.add(newResult.get(entry.getKey().getKey()));
                    newResult.set(entry.getKey().getKey(), temp);
                }
            }
            for (int i = 0; i < numRows; i++) {
                T temp = matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()).initialize(matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()));
                temp.subtract(newResult.get(i));
                temp.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                newResult.set(i, temp);
            }
            results = newResult;
        }
        return new NormalMatrix<>(1, results);
    }

    public NormalMatrix<T> GS(SparseMatrix<T> matrix, int iter) {
        List<T> results = new ArrayList<>();
        int numCols = matrix.countColumns() - 1;
        int numRows = matrix.countRows();
        for (int i = 0; i < numRows; i++) {
            results.add(matrix.getTypeElement().initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            List<T> tabOfSummary = new ArrayList<>();
            for (int i = 0; i < numRows; i++) {
                tabOfSummary.add(matrix.getTypeElement().initializeWithZero());
            }
            for (Map.Entry<Pair<Integer, Integer>, T> entry : matrix.getSparseMatrix().entrySet()) {
                if (!entry.getKey().getKey().equals(entry.getKey().getValue()) && entry.getKey().getValue() < numCols) {
                    T temp = entry.getValue().initialize(entry.getValue());
                    temp.multiply(results.get(entry.getKey().getValue()));
                    temp.add(tabOfSummary.get(entry.getKey().getKey()));
                    tabOfSummary.set(entry.getKey().getKey(), temp);
                }
            }
            for (int i = 0; i < numRows; i++) {
                T temp = matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()).initialize(matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()));
                temp.subtract(tabOfSummary.get(i));
                temp.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                results.set(i, temp);
            }
        }
        return new NormalMatrix<>(1, results);
    }
}
