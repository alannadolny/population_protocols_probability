package Gausses;

import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GaussForSparseMatrix<T extends Operations<T>> {

    public Integer findRowWithMaximumElement(SparseMatrix<T> matrix, Integer fromRow) {
        Integer index = fromRow;
        T maximumElement = matrix.getSparseMatrix().get(new Pair<>(fromRow, fromRow));
        for (int i = fromRow; i < matrix.countRows(); i++) {
            if (matrix.getSparseMatrix().containsKey(new Pair<>(i, fromRow))) {
                if (maximumElement.absolute().compare(matrix.getSparseMatrix().get(new Pair<>(i, fromRow)).absolute()) == -1) {
                    maximumElement = maximumElement.initialize(matrix.getSparseMatrix().get(new Pair<>(i, fromRow)));
                    index = i;
                }
            }
        }
        return index;
    }

    public NormalMatrix<T> G(SparseMatrix<T> matrix, String mode) {
        List<T> results = new ArrayList<>();
        int leadingNumberIndex = 0;
        while (leadingNumberIndex < matrix.countRows()) { // elementy wiodace
            if (mode.equals("PG"))
                if (findRowWithMaximumElement(matrix, leadingNumberIndex) != leadingNumberIndex) {
                    int index = findRowWithMaximumElement(matrix, leadingNumberIndex);
                    for (int i = 0; i < matrix.countRows(); i++) {
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(index, i))) {
                            if (matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex, i))) {
                                T elementToSwitchFromMaximumRow = matrix.getSparseMatrix().get(new Pair<>(index, i));
                                T elementToSwitch = matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, i));
                                matrix.getSparseMatrix().put(new Pair<>(index, i), elementToSwitch);
                                matrix.getSparseMatrix().put(new Pair<>(leadingNumberIndex, i), elementToSwitchFromMaximumRow);
                            }
                        }
                    }
                }


            for (int i = leadingNumberIndex + 1; i < matrix.countRows(); i++) { // wiersze
                T x;
                if (matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, leadingNumberIndex)).equals(matrix.getTypeElement().initializeWithZero()))
                    x = matrix.getTypeElement().initializeWithZero(); //.get(new Pair<>(leadingNumberIndex, leadingNumberIndex)).initializeWithZero();
                else {
                    if (matrix.getSparseMatrix().containsKey(new Pair<>(i, leadingNumberIndex))) {
                        x = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(i, leadingNumberIndex)));
                        x.divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, leadingNumberIndex)));
                    } else {
                        x = matrix.getTypeElement().initializeWithZero();
                    }
                }
                //x.divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, leadingNumberIndex)));
                for (int j = 0; j < matrix.countRows() + 1; j++) { // kolumny
                    if (matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex, j))) {
                        T toSubtract = matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, leadingNumberIndex)).initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, j)));
                        toSubtract.multiply(x);
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                            matrix.getSparseMatrix().get(new Pair<>(i, j)).subtract(toSubtract);
                        }
                    }
                }
            }
            leadingNumberIndex++;
        }

        for (int i = matrix.countRows() - 1; i >= 0; i--) {
            int resultIndex;
            for (int j = matrix.countRows() - 1; j > i; j--) {
                resultIndex = matrix.countRows() - 1 - j;
                T temp, nextTemp;
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                    temp = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(i, j)));
                } else {
                    temp = matrix.getTypeElement().initializeWithZero();
                }
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, matrix.countRows()))) {
                    nextTemp = matrix.getTypeElement().initialize(results.get(resultIndex));
                } else {
                    nextTemp = matrix.getTypeElement().initializeWithZero();
                }
                temp.multiply(nextTemp);
                if (!matrix.getSparseMatrix().containsKey(new Pair<>(i, matrix.countRows()))) {
                    matrix.getSparseMatrix().put(new Pair<>(i, matrix.countRows()), matrix.getTypeElement().initializeWithOne());
                }
                matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).subtract(temp);
            }
            if (matrix.getSparseMatrix().containsKey(new Pair<>(i, matrix.countRows())) && matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex - 1, leadingNumberIndex - 1))) {
                matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex - 1, leadingNumberIndex - 1)));
                results.add(matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())));
            } else {
                results.add(matrix.getTypeElement().initializeWithZero());
            }
            leadingNumberIndex--;
        }

        Collections.reverse(results);
        System.out.println(matrix.toString());
        return new NormalMatrix<>(1, results);
    }
}
