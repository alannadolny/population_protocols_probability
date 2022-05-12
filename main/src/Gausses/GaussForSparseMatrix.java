package Gausses;

import Matrixes.NormalMatrix;
import Matrixes.OldSparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GaussForSparseMatrix<T extends Operations<T>> {

    public Integer findRowWithMaximumElement(OldSparseMatrix<T> matrix, Integer fromRow) {
        Integer index = fromRow;
        T maximumElement = matrix.getSparseMatrix().get(new Pair<>(fromRow, fromRow));
        for (int i = fromRow; i < matrix.countRows(); i++) {
            if (matrix.getSparseMatrix().containsKey(new Pair<>(i, fromRow))) {
                if ((maximumElement.absolute()).compare(matrix.getSparseMatrix().get(new Pair<>(i, fromRow)).absolute()) == -1) {
                    maximumElement = maximumElement.initialize(matrix.getSparseMatrix().get(new Pair<>(i, fromRow)));
                    index = i;
                }
            }
        }
        return index;
    }


    public NormalMatrix<T> G(OldSparseMatrix<T> matrix, String mode) {
        List<T> results = new ArrayList<>();
        int leadingNumberIndex = 0;
        int numCols = matrix.countColumns();
        int numRows = matrix.countRows();
        while (leadingNumberIndex < numRows) {
            if (mode.equals("PG")) {
                int index = findRowWithMaximumElement(matrix, leadingNumberIndex);
                if (index != leadingNumberIndex) {
                    for (int i = 0; i < numCols; i++) {
                        Pair<Integer, Integer> pair = new Pair<>(index, i);
                        Pair<Integer, Integer> pairWithLeading = new Pair<>(leadingNumberIndex, i);
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
            }

            for (int i = leadingNumberIndex + 1; i < numRows; i++) {
                Pair<Integer, Integer> pairWithLeading = new Pair<>(i, leadingNumberIndex);
                if (matrix.getSparseMatrix().containsKey(pairWithLeading)) {
                    if (matrix.getSparseMatrix().containsKey(pairWithLeading)) {
                        T x = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairWithLeading));
                        x.divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, leadingNumberIndex)));
                        for (int j = leadingNumberIndex; j < numRows + 1; j++) {
                            Pair<Integer, Integer> pairIJ = new Pair<>(i, j);
                            Pair<Integer, Integer> pairLeadingJ = new Pair<>(leadingNumberIndex, j);
                            if (j == leadingNumberIndex) {
                                matrix.getSparseMatrix().remove(pairWithLeading);
                            } else if (matrix.getSparseMatrix().containsKey(pairLeadingJ)) {
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
                }

            }

            leadingNumberIndex++;
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
            matrix.getSparseMatrix().get(pairINumRows).divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex - 1, leadingNumberIndex - 1)));
            results.add(matrix.getSparseMatrix().get(pairINumRows));
            leadingNumberIndex--;
        }
        Collections.reverse(results);

        return new NormalMatrix<>(1, results);
    }


    public NormalMatrix<T> GJ(OldSparseMatrix<T> matrix, int iter) {
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
                T summary = matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()).initialize(matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()));
                for (int j = 0; j < numCols; j++) {
                    if (i != j) {
                        T temp2 = matrix.getSparseMatrix().getOrDefault(new Pair<>(i, j), matrix.getTypeElement().initializeWithZero()).initialize(matrix.getSparseMatrix().getOrDefault(new Pair<>(i, j), matrix.getTypeElement().initializeWithZero()));
                        temp2.multiply(results.get(j));
                        summary.subtract(temp2);
                    }
                }
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, i))) {
                    summary.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                    newResult.set(i, summary);
                } else {
                    newResult.set(i, matrix.getTypeElement().initializeWithZero());
                }
            }
            results = newResult;
        }
        return new NormalMatrix<>(1, results);
    }

    public NormalMatrix<T> GS(OldSparseMatrix<T> matrix, int iter) {
        List<T> results = new ArrayList<>();
        int numCols = matrix.countColumns() - 1;
        int numRows = matrix.countRows();
        for (int i = 0; i < numRows; i++) {
            results.add(matrix.getTypeElement().initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            for (int i = 0; i < numRows; i++) {
                T summary = matrix.getTypeElement().initializeWithZero();
                for (int j = 0; j < numCols; j++) {
                    if (i != j) {
                        T temp2 = matrix.getSparseMatrix().getOrDefault(new Pair<>(i, j), matrix.getTypeElement().initializeWithZero()).initialize(matrix.getSparseMatrix().getOrDefault(new Pair<>(i, j), matrix.getTypeElement().initializeWithZero()));
                        temp2.multiply(results.get(j));
                        summary.add(temp2);
                    }
                }
                T temp = matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()).initialize(matrix.getSparseMatrix().getOrDefault(new Pair<>(i, numCols), matrix.getTypeElement().initializeWithZero()));
                temp.subtract(summary);
                temp.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                results.set(i, temp);
            }
        }
        return new NormalMatrix<>(1, results);
    }
}
