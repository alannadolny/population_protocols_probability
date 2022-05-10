package Gausses;

import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptimizedGaussForSparseMatrix<T extends Operations<T>> {
    public NormalMatrix<T> G(SparseMatrix<T> matrix, String mode) {
        int numCols = matrix.countColumns();
        int numRows = matrix.countRows();
//        System.out.println(matrix);
        //pewnie trzeba bedzie wrzucic to w petle while
        for (Map.Entry<Pair<Integer, Integer>, T> entry : matrix.getSparseMatrix().entrySet()) {
            if (!entry.getKey().getKey().equals(entry.getKey().getValue()) && entry.getKey().getKey() > entry.getKey().getValue() && entry.getKey().getValue() < numRows) {
                Pair<Integer, Integer> leadingNumberIndex = new Pair<>(entry.getKey().getValue(), entry.getKey().getValue());
                T leadingNumber = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(leadingNumberIndex));
                T currentNumber = matrix.getTypeElement().initialize(entry.getValue());
                currentNumber.divide(leadingNumber);
                for (int i = 0; i < numCols; i++) {
                    if (matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex.getKey(), i))) {
                        T toSubtract = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex.getKey(), i)));
                        toSubtract.multiply(currentNumber);
                        //poprawic indexy od ktorych jest odejmowane, to powinno wyzerowac pod wiodacymi
//                        System.out.println(entry.getKey() + " : " + matrix.getSparseMatrix().get(entry.getKey()).returnValue());
                        matrix.getSparseMatrix().get(entry.getKey()).subtract(toSubtract);
                    } else {
                        //dodac case dla liczb obok wiodacych, tych ktorych macierz nie zawiera
                    }
                }
            }
        }
//        System.out.println(matrix);
        List<T> temp = new ArrayList<>();
        return new NormalMatrix<>(1, temp);
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
                    tabOfSummary.set(entry.getKey().getKey(), temp);
                }
            }
            for (int i = 0; i < numRows; i++) {
                T temp = matrix.getTypeElement().initializeWithZero();
                if (matrix.getSparseMatrix().containsKey(new Pair<>(numCols - 1, i))) {
                    temp = matrix.getSparseMatrix().get(new Pair<>(numCols - 1, i));
                }
                temp.subtract(tabOfSummary.get(i));
                temp.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                results.set(i, temp);
            }
        }
        return new NormalMatrix<>(1, results);
    }
}
