package Gausses;

import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GaussForSparseMatrix<T extends Operations<T>> {

    public Integer findRowWithMaximumElement(SparseMatrix<T> matrix, Integer fromRow) {
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

    public NormalMatrix<T> G(SparseMatrix<T> matrix, String mode) {
        List<T> results = new ArrayList<>();
        int leadingNumberIndex = 0;
        while (leadingNumberIndex < matrix.countRows()) { // elementy wiodace
            if (mode.equals("PG"))
                if (findRowWithMaximumElement(matrix, leadingNumberIndex) != leadingNumberIndex) {
                    int index = findRowWithMaximumElement(matrix, leadingNumberIndex); // wiersz z maks elementem
                    System.out.println("=====================");
                    System.out.println(leadingNumberIndex + " <-> " + index);
                    System.out.println(matrix);
                    for (int i = 0; i < matrix.countColumns(); i++) { //obecne kolumny, leading numbers trzyma ten do zamiany
                        // wyodrabniony kurwa blok kodu
                        boolean firstCondition = matrix.getSparseMatrix().containsKey(new Pair<>(index, i));
                        boolean secondCondition = matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex, i));

                        BigDecimal xd = matrix.getSparseMatrix().get(new Pair<>(index, i)) == null ? BigDecimal.ZERO : matrix.getSparseMatrix().get(new Pair<>(index, i)).returnValue();
                        BigDecimal xd2 = matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, i)) == null ? BigDecimal.ZERO : matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, i)).returnValue();

                        System.out.println(xd + " - " + xd2);
                        System.out.println(firstCondition + " && " +secondCondition);
                        // lepszy Kazimierz Odnowiciel niz kurwa jprdl, czy tam jebac kurwa xdddd

                        if (firstCondition && secondCondition) {
                            T elementToSwitchFromMaximumRow = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(index, i)));
                            T elementToSwitch = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, i)));
                            matrix.getSparseMatrix().put(new Pair<>(index, i), elementToSwitch);
                            matrix.getSparseMatrix().put(new Pair<>(leadingNumberIndex, i), elementToSwitchFromMaximumRow);
                        }

                        if (firstCondition && !secondCondition) {
                            T elementToSwitchFromMaximumRow = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(index, i)));
                            matrix.getSparseMatrix().put(new Pair<>(leadingNumberIndex, i), elementToSwitchFromMaximumRow);
                            matrix.getSparseMatrix().remove(new Pair<>(index, i));

                        }

                        if (!firstCondition && secondCondition) {
                            T elementToSwitch = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, i)));
                            matrix.getSparseMatrix().put(new Pair<>(index, i), elementToSwitch);
                            matrix.getSparseMatrix().remove(new Pair<>(leadingNumberIndex, i));

                        }


                    }
                    System.out.println(matrix);
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
                        T toSubtract = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, j)));
                        toSubtract.multiply(x);
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                            matrix.getSparseMatrix().get(new Pair<>(i, j)).subtract(toSubtract);
                        } else {
                            matrix.getSparseMatrix().put(new Pair<>(i, j), toSubtract);
                        }
                    }
                }
            }
            leadingNumberIndex++;
        }

        for (int i = matrix.countRows() - 1; i >= 0; i--) {
            int resultIndex;
            for (int j = matrix.countRows() - 1; j > i; j--) {
                T temp, nextTemp;
                resultIndex = matrix.countRows() - 1 - j;
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j)))
                    temp = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(i, j)));
                else temp = matrix.getTypeElement().initializeWithZero();
                nextTemp = matrix.getTypeElement().initialize(results.get(resultIndex));
                temp.multiply(nextTemp);
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, matrix.countRows())))
                    matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).subtract(temp);
                else {
                    matrix.getSparseMatrix().put(new Pair<>(i, matrix.countRows()), matrix.getTypeElement().initializeWithZero());
                    matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).subtract(temp);
                }
            }
            matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex - 1, leadingNumberIndex - 1)));
            results.add(matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())));
            leadingNumberIndex--;
        }

        Collections.reverse(results);

        return new NormalMatrix<>(1, results);
    }

    public NormalMatrix<T> GJ(SparseMatrix<T> matrix, int iter) {
        List<T> results = new ArrayList<>();
        for (int i = 0; i < matrix.countRows(); i++) {
            results.add(matrix.getTypeElement().initializeWithOne());
        }
        System.out.println(matrix);
        for (int h = 0; h < iter; h++) {
            List<T> newResult = new ArrayList<>();
            for (int i = 0; i < matrix.countRows(); i++) {
//                    System.out.println(matrix);
                newResult.add(matrix.getTypeElement().initializeWithZero());
//                System.out.println(matrix.countColumns()+","+i);
//                System.out.println(matrix.getSparseMatrix().get(new Pair<>(9, 10)).returnValue());
                T summary;
                if (matrix.getSparseMatrix().containsKey(new Pair<>(matrix.countColumns() - 1, i))) {
                    summary = matrix.getSparseMatrix().get(new Pair<>(matrix.countColumns() - 1, i));
                } else {
                    summary = matrix.getTypeElement().initializeWithZero();
                }
//                    System.out.println("T1");
                for (int j = 0; j < matrix.countColumns() - 1; j++) {
                    if (i != j) {
                        T temp2 = matrix.getTypeElement().initializeWithZero();
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
//                                System.out.println("T2");
                            temp2 = matrix.getSparseMatrix().get(new Pair<>(i, j)).initialize(matrix.getSparseMatrix().get(new Pair<>(i, j)));
                        }
                        System.out.println(results.get(j).returnValue());
                        temp2.multiply(results.get(j));
                        summary.subtract(temp2);
                    }
                }
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, i))) {
//                    System.out.println("T3");
                    summary.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
//                    System.out.print(summary.returnValue()+",");
//                    System.out.println(matrix.getSparseMatrix().get(new Pair<>(i, i)).returnValue());
                    newResult.set(i, summary);
                } else {
                    newResult.set(i, matrix.getTypeElement().initializeWithZero());
                }
            }
            for (T el : results) {
                System.out.println(el.returnValue());
            }
            results = newResult;
        }

        return new NormalMatrix<>(1, results);
    }
}
