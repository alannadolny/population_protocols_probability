package Matrixes;

import Variables.MyDouble;
import javafx.util.Pair;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.sparse.csc.CommonOps_DSCC;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SparseLibraryMatrix {

    private final DMatrixSparseCSC matrix;
    private final int startedSize;
    private final int size;

    public int calculateSize(int size) {
        int t = 0, n = 0, counter = 0;
        while (t + n <= size) {
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

}