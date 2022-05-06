package Matrixes;

import Variables.MyDouble;
import Variables.Operations;
import javafx.util.Pair;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.sparse.csc.CommonOps_DSCC;
import org.ejml.sparse.csc.decomposition.chol.CholeskyUpLooking_DSCC;
import org.ejml.sparse.csc.linsol.chol.LinearSolverCholesky_DSCC;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SparseLibraryMatrix {

    private DMatrixSparseCSC matrix;
    private int startedSize;
    private int size;

    public int calculateSize(int size) {
        Map<Pair<Integer, Integer>, Integer> indexes = new HashMap<>(Map.of());
        int t = 0, n = 0, counter = 0;
        while (t + n <= size) {
            indexes.put(new Pair<>(t, n), counter);
            if (n + t == size) {
                n = 0;
                t++;
            } else {
                n++;
            }
            counter++;
        }
        return counter;
    }


    public SparseLibraryMatrix(int size) {
        this.startedSize = size;
        this.size = calculateSize(size); //size + 1
        this.matrix = new DMatrixSparseCSC(this.size, this.size);
    }

    public void fillMatrix() {
        GenerateEquation equation = new GenerateEquation(this.startedSize);
        SparseMatrix<MyDouble> sparse = new SparseMatrix<>(equation, new MyDouble(0D));
        sparse.fillMatrix();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (sparse.getSparseMatrix().containsKey(new Pair<>(i, j)))
                    this.matrix.set(i, j, sparse.getSparseMatrix().get(new Pair<>(i, j)).returnValue().doubleValue());
            }
        }
    }

    public ArrayList<Double> solve() {
        ArrayList<Double> toReturn = new ArrayList<>();
        DMatrixSparseCSC result = new DMatrixSparseCSC(this.size, 1);
        result.set(this.size - 1, 0, 1);
        DMatrixSparseCSC calculated = new DMatrixSparseCSC(this.size, 1);
        CommonOps_DSCC.solve(this.matrix, result, calculated);
        for(int i = 0; i < this.size; i++) {
            if(calculated.isAssigned(i, 0)) toReturn.add(calculated.get(i, 0));
            else toReturn.add(0D);
        }
        System.out.println(toReturn);
        return toReturn;
    }

    public DMatrixSparseCSC getMatrix() {
        return this.matrix;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        DecimalFormat df = new DecimalFormat("+0.0000;-0.0000");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.matrix.isAssigned(i, j))
                    result.append(df.format(this.matrix.get(i, j))).append(" ");
                else result.append("+0,0000 ");
            }
            result.append("\n");
        }
        return result.toString();
    }

//    public final DMatrixSparseTriplet matrix;
//
//    public SparseLibraryMatrix(int n, int initLength) {
//        this.matrix = new DMatrixSparseTriplet(n + 1, n + 1, initLength);
//    }
//
//    public void addItem(int row, int col, double value) {
//        this.matrix.set(row, col, value);
//    }
//
//    public Integer countRows() {
//        return this.matrix.numRows;
//    }
//
//    public Integer countColumns() {
//        return this.matrix.numCols;
//    }
//
//    public void addManyItems(NormalMatrix<T> normalMatrix) {
//        T zeroElement = normalMatrix.getMatrix().get(0).get(0).initializeWithZero();
//        for (int i = 0; i < normalMatrix.countRows(); i++) {
//            for (int j = 0; j < normalMatrix.countColumns(); j++) {
//                if (normalMatrix.getMatrix().get(i).get(j).compare(zeroElement) != 0) {
//                    this.addItem(i, j, normalMatrix.getMatrix().get(i).get(j).returnValue().doubleValue());
//                }
//            }
//        }
//    }
//
//
//    public Map<Pair<Integer, Integer>, Double> getSparseMatrix() {
//        Map<Pair<Integer, Integer>, Double> toReturn = new HashMap<>(Map.of());
//        for (int i = 0; i < this.matrix.nz_value.length; i++) {
//            toReturn.put(new Pair<>(this.matrix.nz_rowcol.data[i * 2], this.matrix.nz_rowcol.data[(i * 2) + 1]), this.matrix.nz_value.data[i]);
//        }
//        return toReturn;
//    }

//    public void solve(){
//        this.matrix.
//    }

}