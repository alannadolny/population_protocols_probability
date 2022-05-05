import Gausses.GaussForSparseMatrix;
import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import MonteCarlo.SolveMatrix;
import Variables.MyDouble;
import Variables.MyFractions;
import Variables.Operations;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Measurements<T extends Operations<T>> {

    public void saveResults(String result, String filename, String filePath) throws IOException {
        Path fileName = Path.of(
                filePath + filename + ".txt"
        );
        Files.writeString(fileName, result);
    }

    public T getMaximumNorm(ArrayList<T> list) {
        T initialValue = list.get(0);
        for (T el : list) {
            if(initialValue.compare(el) == -1) {
                initialValue = el;
            }
        }
        return initialValue;
    }

    public void verifyIterativeMethods(ArrayList<Integer> values, T el) throws IOException {

        StringBuilder results = new StringBuilder();

        for (int i : values) {

            ArrayList<T> normsGJ = new ArrayList<>();
            ArrayList<T> normsGS = new ArrayList<>();

            GenerateEquation generateEquation = new GenerateEquation(i);
            SparseMatrix<T> sparseMatrixGJ = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGS = new SparseMatrix<>(generateEquation, el);
            sparseMatrixGJ.fillMatrix();
            sparseMatrixGS.fillMatrix();
            GaussForSparseMatrix<T> gauss = new GaussForSparseMatrix<>();
            NormalMatrix<T> resultGJ = gauss.GJ(sparseMatrixGJ, 100);
            NormalMatrix<T> resultGS = gauss.GS(sparseMatrixGS, 100);
            SolveMatrix monteCarlo = new SolveMatrix(i);
            monteCarlo.solve();

            for (int j = 0; j < resultGJ.getMatrix().size(); j++) {
                for (int k = 0; k < resultGJ.getMatrix().get(j).size(); k++) {
                    T tempGJ = resultGJ.getMatrix().get(j).get(k).initialize(resultGJ.getMatrix().get(j).get(k));
                    tempGJ.subtract(tempGJ.initializeWithDouble(monteCarlo.getResult().get(j)));
                    normsGJ.add(tempGJ.absolute());

                    T tempGS = resultGJ.getMatrix().get(j).get(k).initialize(resultGS.getMatrix().get(j).get(k));
                    tempGS.subtract(tempGS.initializeWithDouble(monteCarlo.getResult().get(j)));
                    normsGS.add(tempGS.absolute());
                }
            }

            results.append("GS - ").append(i).append(": ").append(this.getMaximumNorm(normsGS).returnValue()).append("\n");
            results.append("GJ - ").append(i).append(": ").append(this.getMaximumNorm(normsGJ).returnValue()).append("\n");

        }

        this.saveResults(results.toString(), "tests", "C:/Users/gruby/population_protocols_probability/data/");
    }


    public static void main(String[] args) throws IOException {
        Measurements<MyFractions> measure = new Measurements<>();
        ArrayList<Integer> tests = new ArrayList<>();
        tests.add(10);
        measure.verifyIterativeMethods(tests, new MyFractions(0));
    }
}
