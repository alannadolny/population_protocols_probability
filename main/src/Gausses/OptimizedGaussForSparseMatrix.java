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

        public NormalMatrix<T> G(SparseMatrix<T> matrix, String mode) {
            List<T> results = new ArrayList<>();
            int numCols = matrix.countColumns();
            int numRows = matrix.countRows();
            int leadingNumber = 0;
            while (leadingNumber < numRows) {
                int finalLeadingNumber = leadingNumber;
                List<Integer> toOperate = matrix.getSparseMatrix().keySet().stream().filter(t -> t.getValue().equals(finalLeadingNumber) && t.getKey() > finalLeadingNumber).map(Pair::getKey).collect(Collectors.toList());

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

            System.out.println(matrix);

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
                T temp = matrix.getTypeElement().initializeWithZero();
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, numCols))) {
                    temp = matrix.getSparseMatrix().get(new Pair<>(i, numCols));
                }
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
                T temp = matrix.getTypeElement().initializeWithZero();
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, numCols))) {
                    temp = matrix.getSparseMatrix().get(new Pair<>(i, numCols));
                }
                temp.subtract(tabOfSummary.get(i));
                temp.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                results.set(i, temp);
            }
        }
        return new NormalMatrix<>(1, results);
    }
}
