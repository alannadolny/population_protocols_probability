package Gausses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Matrixes.NormalMatrix;
import Variables.*;

public class Gauss<T extends Operations<T>> {

    public Integer[] findRowAndColumnWithMaximumElement(NormalMatrix<T> matrix, Integer fromRow) {
        Integer rowIndex = fromRow;
        Integer columnIndex = fromRow;
        T maximumElement = matrix.getMatrix().get(fromRow).get(fromRow);
        for (int i = fromRow; i < matrix.countRows(); i++) {
            for (int j = fromRow; j < matrix.countRows(); j++) {
                if ((maximumElement.absolute().compare(matrix.getMatrix().get(i).get(j).absolute())) == -1) {
                    maximumElement = maximumElement.initialize(matrix.getMatrix().get(i).get(j));
                    rowIndex = i;
                    columnIndex = j;
                }
            }
        }
        return new Integer[]{
                rowIndex,
                columnIndex
        };
    }

    public Integer findRowWithMaximumElement(NormalMatrix<T> matrix, Integer fromRow) {
        Integer index = fromRow;
        T maximumElement = matrix.getMatrix().get(fromRow).get(fromRow);
        for (int i = fromRow; i < matrix.countRows(); i++) {
            if (maximumElement.absolute().compare((matrix.getMatrix().get(i).get(fromRow).absolute())) == -1) {
                maximumElement = maximumElement.initialize(matrix.getMatrix().get(i).get(fromRow));
                index = i;
            }
        }
        return index;
    }

    public NormalMatrix<T> G(NormalMatrix<T> matrix, NormalMatrix<T> vector, String mode) {
        Integer[] switchedColumns = new Integer[matrix.countRows()];
        List<T> results = new ArrayList<>();
        for (int i = 0; i < vector.countRows(); i++) {
            matrix.getMatrix().get(i).add(vector.getMatrix().get(i).get(0));
        }
        int leadingNumberIndex = 0;
        while (leadingNumberIndex < matrix.countRows()) { // elementy wiodace
            if (mode.equals("PG"))
                if (findRowWithMaximumElement(matrix, leadingNumberIndex) != leadingNumberIndex) {
                    int index = findRowWithMaximumElement(matrix, leadingNumberIndex);
                    List<T> switching = matrix.getMatrix().get(leadingNumberIndex);
                    matrix.getMatrix().set(leadingNumberIndex, matrix.getMatrix().get(index));
                    matrix.getMatrix().set(index, switching);
                }
            if (mode.equals("FG")) {
                Integer[] indexes = findRowAndColumnWithMaximumElement(matrix, leadingNumberIndex);
                List<T> switched = matrix.getMatrix().get(leadingNumberIndex);
                matrix.getMatrix().set(leadingNumberIndex, matrix.getMatrix().get(indexes[0]));
                matrix.getMatrix().set(indexes[0], switched);

                switchedColumns[leadingNumberIndex] = indexes[1];
                for (int i = 0; i < matrix.countRows(); i++) {
                    T currentElement = matrix.getMatrix().get(i).get(leadingNumberIndex);
                    matrix.getMatrix().get(i).set(leadingNumberIndex, matrix.getMatrix().get(i).get(indexes[1]));
                    matrix.getMatrix().get(i).set(indexes[1], currentElement);

                }
            }

            for (int i = leadingNumberIndex + 1; i < matrix.countRows(); i++) { // wiersze
                T x;
                if (matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).equals(matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).initializeWithZero()))
                    x = matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).initializeWithZero();
                else
                    x = matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).initialize(matrix.getMatrix().get(i).get(leadingNumberIndex));
                x.divide(matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex));
                for (int j = 0; j < matrix.countColumns(); j++) { // kolumny
                    T toSubtract = matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).initialize(matrix.getMatrix().get(leadingNumberIndex).get(j));
                    toSubtract.multiply(x);
                    matrix.getMatrix().get(i).get(j).subtract(toSubtract);
                }

            }
            leadingNumberIndex++;
        }
        for (int i = matrix.countRows() - 1; i >= 0; i--) {
            int resultIndex;
            for (int j = matrix.countRows() - 1; j > i; j--) {
                resultIndex = matrix.countRows() - 1 - j;
                T temp = matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).initialize(matrix.getMatrix().get(i).get(j));
                T nextTemp = matrix.getMatrix().get(leadingNumberIndex).get(leadingNumberIndex).initialize(results.get(resultIndex));
                temp.multiply(nextTemp);
                matrix.getMatrix().get(i).get(matrix.countRows()).subtract(temp);
            }
            matrix.getMatrix().get(i).get(matrix.countColumns() - 1).divide(matrix.getMatrix().get(leadingNumberIndex - 1).get(leadingNumberIndex - 1));
            results.add(matrix.getMatrix().get(i).get(matrix.countColumns() - 1));
            leadingNumberIndex--;
        }

        Collections.reverse(results);

        if (mode.equals("FG"))
            for (int i = switchedColumns.length - 1; i >= 0; i--) {
                if (i != switchedColumns[i]) {
                    T temp = results.get(i);
                    results.set(i, results.get(switchedColumns[i]));
                    results.set(switchedColumns[i], temp);
                }
            }

        return new NormalMatrix<>(1, results);
    }

    public NormalMatrix<T> GJ(NormalMatrix<T> matrix, NormalMatrix<T> vector, int iter) {
        List<T> results = new ArrayList<>();
        for (int i = 0; i < matrix.countRows(); i++) {
            results.add(matrix.getMatrix().get(0).get(0).initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            List<T> newResult = new ArrayList<>();
            for (int i = 0; i < matrix.countRows(); i++) {
                newResult.add(matrix.getMatrix().get(0).get(0).initializeWithZero());
                T summary = vector.getMatrix().get(i).get(0).initialize(vector.getMatrix().get(i).get(0));
                for (int j = 0; j < matrix.countColumns(); j++) {
                    if (i != j) {
                        T temp2 = matrix.getMatrix().get(i).get(j).initialize(matrix.getMatrix().get(i).get(j));
                        temp2.multiply(results.get(j));
                        summary.subtract(temp2);
                    }
                }
                summary.divide(matrix.getMatrix().get(i).get(i));
                newResult.set(i, summary);
            }
            results = newResult;
        }
        return new NormalMatrix<>(1, results);
    }

    public NormalMatrix<T> GS(NormalMatrix<T> matrix, NormalMatrix<T> vector, int iter) {
        List<T> results = new ArrayList<>();
        for (int i = 0; i < matrix.countRows(); i++) {
            results.add(matrix.getMatrix().get(0).get(0).initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            for (int i = 0; i < matrix.countRows(); i++) {
                T summary = matrix.getMatrix().get(0).get(0).initializeWithZero();
                for (int j = 0; j < matrix.countColumns(); j++) {
                    if (j != i) {
                        T temp2 = matrix.getMatrix().get(i).get(j).initialize(matrix.getMatrix().get(i).get(j));
                        temp2.multiply(results.get(j));
                        summary.add(temp2);
                    }
                }
                T temp = vector.getMatrix().get(i).get(0).initialize(vector.getMatrix().get(i).get(0));
                temp.subtract(summary);
                temp.divide(matrix.getMatrix().get(i).get(i));
                results.set(i, temp);
            }
        }
        return new NormalMatrix<>(1, results);
    }

}