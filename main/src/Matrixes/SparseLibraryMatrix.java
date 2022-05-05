package Matrixes;

import Variables.Operations;
import javafx.util.Pair;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;

import java.util.HashMap;
import java.util.Map;

public class SparseLibraryMatrix<T extends Operations<T>> {
    public final DMatrixSparseTriplet matrix;

    public SparseLibraryMatrix(int n, int initLength) {
        this.matrix = new DMatrixSparseTriplet(n + 1, n + 1, initLength);
    }

    public void addItem(int row, int col, double value) {
        this.matrix.set(row, col, value);
    }

    public Integer countRows() {
        return this.matrix.numRows;
    }

    public Integer countColumns() {
        return this.matrix.numCols;
    }

    public void addManyItems(NormalMatrix<T> normalMatrix) {
        T zeroElement = normalMatrix.getMatrix().get(0).get(0).initializeWithZero();
        for (int i = 0; i < normalMatrix.countRows(); i++) {
            for (int j = 0; j < normalMatrix.countColumns(); j++) {
                if (normalMatrix.getMatrix().get(i).get(j).compare(zeroElement) != 0) {
                    this.addItem(i, j, normalMatrix.getMatrix().get(i).get(j).returnValue().doubleValue());
                }
            }
        }
    }


    public Map<Pair<Integer, Integer>, Double> getSparseMatrix() {
        Map<Pair<Integer, Integer>, Double> toReturn = new HashMap<>(Map.of());
        for (int i = 0; i < this.matrix.nz_value.length; i++) {
            toReturn.put(new Pair<>(this.matrix.nz_rowcol.data[i * 2], this.matrix.nz_rowcol.data[(i * 2) + 1]), this.matrix.nz_value.data[i]);
        }
        return toReturn;
    }

//    public void solve(){
//        this.matrix.
//    }

}