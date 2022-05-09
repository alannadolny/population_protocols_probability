package Gausses;

import Matrixes.SparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;

public class OptimizedGaussForSparseMatrix<T extends Operations<T>> {
    public void G(SparseMatrix<T> matrix, String mode) {
        int numCols = matrix.countColumns();
        int numRows = matrix.countRows();
        System.out.println(matrix);
        //pewnie trzeba bedzie wrzucic to w petle while
        for (Map.Entry<Pair<Integer, Integer>, T> entry : matrix.getSparseMatrix().entrySet()) {
            if(!entry.getKey().getKey().equals(entry.getKey().getValue()) && entry.getKey().getKey() > entry.getKey().getValue() && entry.getKey().getValue() < numRows) {
                Pair<Integer, Integer> leadingNumberIndex = new Pair<>(entry.getKey().getValue(), entry.getKey().getValue());
                T leadingNumber = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(leadingNumberIndex));
                T currentNumber = matrix.getTypeElement().initialize(entry.getValue());
                currentNumber.divide(leadingNumber);
                for(int i = 0; i < numCols; i++) {
                    if(matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex.getKey(), i))) {
                        T toSubtract = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex.getKey(), i)));
                        toSubtract.multiply(currentNumber);
                        //poprawic indexy od ktorych jest odejmowane, to powinno wyzerowac pod wiodacymi
                        System.out.println(entry.getKey() + " : " + matrix.getSparseMatrix().get(entry.getKey()).returnValue());
                        matrix.getSparseMatrix().get(entry.getKey()).subtract(toSubtract);
                    } else {
                        //dodac case dla liczb obok wiodacych, tych ktorych macierz nie zawiera
                    }
                }
            }
        }
        System.out.println(matrix);
    }
}
