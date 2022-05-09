import Gausses.Gauss;
import Gausses.GaussForSparseMatrix;
import Gausses.OptimizedGaussForSparseMatrix;
import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseLibraryMatrix;
import Matrixes.SparseMatrix;
import MonteCarlo.SolveMatrix;
import Variables.MyDouble;
import Variables.MyFloat;
import Variables.MyFractions;
import Variables.Operations;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


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
            if (initialValue.compare(el) == -1) {
                initialValue = el;
            }
        }
        return initialValue;
    }

    public void getIterationNumber(int size, double expectedPrecision, T el, String type) throws IOException {
        int iteration = 1;
        boolean gj = false, gs = false;
        while (true) {
            SparseLibraryMatrix library = new SparseLibraryMatrix(size);
            library.fillMatrix();
            ArrayList<Double> resultFromLibrary = library.solve();

            GenerateEquation generateEquation = new GenerateEquation(size);
            SparseMatrix<T> sparseMatrixGJ = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGS = new SparseMatrix<>(generateEquation, el);
            sparseMatrixGJ.fillMatrix();
            sparseMatrixGS.fillMatrix();
            GaussForSparseMatrix<T> gauss = new GaussForSparseMatrix<>();
            NormalMatrix<T> resultGJ = gauss.GJ(sparseMatrixGJ, iteration);
            NormalMatrix<T> resultGS = gauss.GS(sparseMatrixGS, iteration);

            ArrayList<T> normsGJ = new ArrayList<>();
            ArrayList<T> normsGS = new ArrayList<>();

            for (int j = 0; j < resultGJ.getMatrix().size(); j++) {
                for (int k = 0; k < resultGJ.getMatrix().get(j).size(); k++) {
                    T tempGJ = resultGJ.getMatrix().get(j).get(k).initialize(resultGJ.getMatrix().get(j).get(k));
                    tempGJ.subtract(tempGJ.initializeWithDouble(resultFromLibrary.get(j)));
                    normsGJ.add(tempGJ.absolute());

                    T tempGS = resultGJ.getMatrix().get(j).get(k).initialize(resultGS.getMatrix().get(j).get(k));
                    tempGS.subtract(tempGS.initializeWithDouble(resultFromLibrary.get(j)));
                    normsGS.add(tempGS.absolute());
                }
            }


            if (this.getMaximumNorm(normsGJ).returnValue().compareTo(new BigDecimal(String.valueOf(expectedPrecision))) < 0 && !gj) {
//                this.saveResults("GJ - precision: " + expectedPrecision + " in iteration: " + iteration, "compareJacobiWithSeidelSparseMatrix1" + type, "C:/Users/gruby/population_protocols_probability/data/");
                this.saveResults("GJ - precision: " + expectedPrecision + " in iteration: " + iteration, "compareJacobiWithSeidelSparseMatrix1" + type, "data/");
                gj = true;
            }
            if (this.getMaximumNorm(normsGS).returnValue().compareTo(new BigDecimal(String.valueOf(expectedPrecision))) < 0 && !gs) {
//                this.saveResults("GS - precision: " + expectedPrecision + " in iteration: " + iteration, "compareJacobiWithSeidelSparseMatrix2" + type, "C:/Users/gruby/population_protocols_probability/data/");
                this.saveResults("GS - precision: " + expectedPrecision + " in iteration: " + iteration, "compareJacobiWithSeidelSparseMatrix2" + type, "data/");
                gs = true;
            }
            if (gj && gs) break;
            iteration++;
        }
    }

    public void compareJacobiWithSeidelNormalMatrix(ArrayList<Integer> values, T el, String type) throws IOException {

        StringBuilder results = new StringBuilder();

        for (int i : values) {

            ArrayList<T> normsGJ = new ArrayList<>();
            ArrayList<T> normsGS = new ArrayList<>();

            SparseLibraryMatrix library = new SparseLibraryMatrix(i);
            library.fillMatrix();
            ArrayList<Double> resultFromLibrary = library.solve();

            GenerateEquation generateEquation = new GenerateEquation(i);
            SparseMatrix<T> sparseMatrixGJ = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGS = new SparseMatrix<>(generateEquation, el);
            sparseMatrixGJ.fillMatrix();
            sparseMatrixGS.fillMatrix();
            Gauss<T> gauss = new Gauss<>();

            NormalMatrix<T> normalMatrixGJ = new NormalMatrix<>(sparseMatrixGJ.generateIndexes().size(), sparseMatrixGJ);
            NormalMatrix<T> normalMatrixGS = new NormalMatrix<>(sparseMatrixGS.generateIndexes().size(), sparseMatrixGS);

            ArrayList<T> vector = new ArrayList<>();
            ArrayList<T> vector2 = new ArrayList<>();
            for (int k = 0; k < sparseMatrixGJ.generateIndexes().size() - 1; k++) {
                vector.add(el.initializeWithZero());
                vector2.add(el.initializeWithZero());
            }

            vector.add(el.initializeWithOne());
            vector2.add(el.initializeWithOne());

            NormalMatrix<T> vectorGJ = new NormalMatrix<>(1, vector);
            NormalMatrix<T> vectorGS = new NormalMatrix<>(1, vector2);

            long start = System.currentTimeMillis();
            NormalMatrix<T> resultGJ = gauss.GJ(normalMatrixGJ, vectorGJ, 100);
            long stop = System.currentTimeMillis();
            long timeForGJ = stop - start;

            start = System.currentTimeMillis();
            NormalMatrix<T> resultGS = gauss.GS(normalMatrixGS, vectorGS, 100);
            stop = System.currentTimeMillis();
            long timeForGS = stop - start;

            for (int j = 0; j < resultGJ.getMatrix().size(); j++) {
                for (int k = 0; k < resultGJ.getMatrix().get(j).size(); k++) {
                    T tempGJ = resultGJ.getMatrix().get(j).get(k).initialize(resultGJ.getMatrix().get(j).get(k));
                    tempGJ.subtract(tempGJ.initializeWithDouble(resultFromLibrary.get(j)));
                    normsGJ.add(tempGJ.absolute());

                    T tempGS = resultGJ.getMatrix().get(j).get(k).initialize(resultGS.getMatrix().get(j).get(k));
                    tempGS.subtract(tempGS.initializeWithDouble(resultFromLibrary.get(j)));
                    normsGS.add(tempGS.absolute());
                }
            }

            results.append("GS - ").append(i).append(": ").append(this.getMaximumNorm(normsGS).returnValue()).append(", time: ").append(timeForGS).append("\n");
            results.append("GJ - ").append(i).append(": ").append(this.getMaximumNorm(normsGJ).returnValue()).append(", time: ").append(timeForGJ).append("\n");

        }
//        this.saveResults(results.toString(), "compareJacobiWithSeidelNormalMatrix" + type, "C:/Users/gruby/population_protocols_probability/data/");
        this.saveResults(results.toString(), "compareJacobiWithSeidelNormalMatrix" + type, "data/");
    }

    public void compareJacobiWithSeidelSparseMatrix(ArrayList<Integer> values, T el, String type) throws IOException {

        StringBuilder results = new StringBuilder();

        for (int i : values) {

            SparseLibraryMatrix library = new SparseLibraryMatrix(i);
            library.fillMatrix();
            ArrayList<Double> resultFromLibrary = library.solve();

            ArrayList<T> normsGJ = new ArrayList<>();
            ArrayList<T> normsGS = new ArrayList<>();

            GenerateEquation generateEquation = new GenerateEquation(i);
            SparseMatrix<T> sparseMatrixGJ = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGS = new SparseMatrix<>(generateEquation, el);
            sparseMatrixGJ.fillMatrix();
            sparseMatrixGS.fillMatrix();
            GaussForSparseMatrix<T> gauss = new GaussForSparseMatrix<>();

            long start = System.currentTimeMillis();
            NormalMatrix<T> resultGJ = gauss.GJ(sparseMatrixGJ, 100);
            long stop = System.currentTimeMillis();
            long timeForGJ = stop - start;

            start = System.currentTimeMillis();
            NormalMatrix<T> resultGS = gauss.GS(sparseMatrixGS, 100);
            stop = System.currentTimeMillis();
            long timeForGS = stop - start;

            for (int j = 0; j < resultGJ.getMatrix().size(); j++) {
                for (int k = 0; k < resultGJ.getMatrix().get(j).size(); k++) {
                    T tempGJ = resultGJ.getMatrix().get(j).get(k).initialize(resultGJ.getMatrix().get(j).get(k));
                    tempGJ.subtract(tempGJ.initializeWithDouble(resultFromLibrary.get(j)));
                    normsGJ.add(tempGJ.absolute());

                    T tempGS = resultGJ.getMatrix().get(j).get(k).initialize(resultGS.getMatrix().get(j).get(k));
                    tempGS.subtract(tempGS.initializeWithDouble(resultFromLibrary.get(j)));
                    normsGS.add(tempGS.absolute());
                }
            }

            results.append("GS - ").append(i).append(": ").append(this.getMaximumNorm(normsGS).returnValue()).append(", time: ").append(timeForGS).append("\n");
            results.append("GJ - ").append(i).append(": ").append(this.getMaximumNorm(normsGJ).returnValue()).append(", time: ").append(timeForGJ).append("\n");

        }
//        this.saveResults(results.toString(), "compareJacobiWithSeidelSparseMatrix" + type, "C:/Users/gruby/population_protocols_probability/data/");
        this.saveResults(results.toString(), "compareJacobiWithSeidelSparseMatrix" + type, "data/");
    }


    public void compareSparseMatrixWithNormalMatrix(ArrayList<Integer> values, T el, String type) throws IOException {

        StringBuilder results = new StringBuilder();

        for (int i : values) {

            SparseLibraryMatrix library = new SparseLibraryMatrix(i);
            library.fillMatrix();
            ArrayList<Double> resultFromLibrary = library.solve();

            GenerateEquation generateEquation = new GenerateEquation(i);
            SparseMatrix<T> sparseMatrixForNormal = new SparseMatrix<>(generateEquation, el);
            sparseMatrixForNormal.fillMatrix();
            NormalMatrix<T> normalMatrixPG = new NormalMatrix<>(sparseMatrixForNormal.generateIndexes().size(), sparseMatrixForNormal);
            SparseMatrix<T> sparseMatrixPG = new SparseMatrix<>(generateEquation, el);
            sparseMatrixPG.fillMatrix();

            ArrayList<T> vector = new ArrayList<>();
            for (int k = 0; k < sparseMatrixForNormal.generateIndexes().size() - 1; k++) {
                vector.add(el.initializeWithZero());
            }

            vector.add(el.initializeWithOne());
            NormalMatrix<T> vectorForNormal = new NormalMatrix<>(1, vector);

            GaussForSparseMatrix<T> gauss = new GaussForSparseMatrix<>();
            Gauss<T> gaussForNormal = new Gauss<>();

            ArrayList<T> normsForNormalMatrix = new ArrayList<>();
            ArrayList<T> normsForSpareMatrix = new ArrayList<>();

            long start = System.currentTimeMillis();
            NormalMatrix<T> resultForSparseMatrix = gauss.G(sparseMatrixPG, "PG");
            long stop = System.currentTimeMillis();
            long timeForSparse = stop - start;

            start = System.currentTimeMillis();
            NormalMatrix<T> resultNormalMatrix = gaussForNormal.G(normalMatrixPG, vectorForNormal, "PG");
            stop = System.currentTimeMillis();
            long timeForNormal = stop - start;

            System.out.println("Sparse: " + timeForSparse);
            System.out.println("Normal: " + timeForNormal);

            for (int j = 0; j < normalMatrixPG.getMatrix().size(); j++) {
                for (int k = 0; k < resultNormalMatrix.getMatrix().get(j).size(); k++) {
                    T tempNormal = resultNormalMatrix.getMatrix().get(j).get(k).initialize(resultNormalMatrix.getMatrix().get(j).get(k));
                    tempNormal.subtract(tempNormal.initializeWithDouble(resultFromLibrary.get(j)));
                    normsForNormalMatrix.add(tempNormal.absolute());

                    T tempGS = resultForSparseMatrix.getMatrix().get(j).get(k).initialize(resultForSparseMatrix.getMatrix().get(j).get(k));
                    tempGS.subtract(tempGS.initializeWithDouble(resultFromLibrary.get(j)));
                    normsForSpareMatrix.add(tempGS.absolute());
                }
            }

            results.append("Normal matrix - ").append(i).append(": ").append(this.getMaximumNorm(normsForNormalMatrix).returnValue()).append(", time: ").append(timeForNormal).append("\n");
            results.append("Sparse matrix - ").append(i).append(": ").append(this.getMaximumNorm(normsForSpareMatrix).returnValue()).append(", time: ").append(timeForSparse).append("\n");

        }
//        this.saveResults(results.toString(), "compareSparseMatrixWithNormalMatrix" + type, "C:/Users/gruby/population_protocols_probability/data/");
        this.saveResults(results.toString(), "compareSparseMatrixWithNormalMatrix" + type, "data");
    }

    public void verifyIterativeMethods(ArrayList<Integer> values, T el, String type) throws IOException {

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

        //       this.saveResults(results.toString(), "verifyIterativeMethod" + type, "C:/Users/gruby/population_protocols_probability/data/");
        this.saveResults(results.toString(), "verifyIterativeMethod" + type, "data/");
    }


    public static void main(String[] args) throws IOException {
        Measurements<MyDouble> measurementsDouble = new Measurements<>();
        Measurements<MyFloat> measurementsFloat = new Measurements<>();
        Measurements<MyFractions> measurementsFractions = new Measurements<>();

        // temp

        GenerateEquation eq = new GenerateEquation(3);
        SparseMatrix<MyDouble> sparse = new SparseMatrix<>(eq, new MyDouble(0D));
        sparse.fillMatrix();
        OptimizedGaussForSparseMatrix<MyDouble> optimized = new OptimizedGaussForSparseMatrix<>();
        optimized.G(sparse, "PG");

        //sizes:
        // 100 -> 5151
        // 90 -> 4186
        // 80 -> 3321
        // 70 -> 2556
        // 60 -> 1891
        // 50 -> 1326
        // 40 -> 861
        // 30 -> 496
        // 20 -> 231
        // 10 -> 66

        // Double
//        ArrayList<Integer> toCalculate = new ArrayList<>();
//        Collections.addAll(toCalculate, 10, 20, 30, 40, 50);
//        measurementsDouble.verifyIterativeMethods(toCalculate, new MyDouble(0D), "Double");
//        measurementsDouble.compareSparseMatrixWithNormalMatrix(toCalculate, new MyDouble(0D), "Double");
//        measurementsDouble.compareJacobiWithSeidelSparseMatrix(toCalculate, new MyDouble(0D), "Double");
//        measurementsDouble.compareJacobiWithSeidelNormalMatrix(toCalculate, new MyDouble(0D), "Double");
//        for (int i = 10; i <= 30; i += 10) {
//            measurementsDouble.getIterationNumber(i, 0.000001, new MyDouble(0D), "Double-6" + i);
//            measurementsDouble.getIterationNumber(i, 0.00000000001, new MyDouble(0D), "Double-10" + i);
//            measurementsDouble.getIterationNumber(i, 0.00000000000001, new MyDouble(0D), "Double-16" + i);
//        }

        // Float
//        measurementsFloat.verifyIterativeMethods(toCalculate, new MyFloat(0F), "Floats");
//        measurementsFloat.compareSparseMatrixWithNormalMatrix(toCalculate, new MyFloat(0F), "Floats");
//        measurementsFloat.compareJacobiWithSeidelSparseMatrix(toCalculate, new MyFloat(0F), "Floats");
//        measurementsFloat.compareJacobiWithSeidelNormalMatrix(toCalculate, new MyFloat(0F), "Floats");
//        for (int i = 10; i <= 50 ; i += 10) {
//            measurementsFloat.getIterationNumber(i, 0.000001, new MyFloat(0F), "Floats-6" + i);
////            measurementsFloat.getIterationNumber(i, 0.00000000001, new MyFloat(0F), "Floats-10" + i);
////            measurementsFloat.getIterationNumber(i, 0.00000000000001, new MyFloat(0F), "Floats-16" + i);
//        }

        //Fractions
//        measurementsFractions.verifyIterativeMethods(toCalculate, new MyFractions(0), "Fractions");
//        measurementsFractions.compareSparseMatrixWithNormalMatrix(toCalculate, new MyFractions(0), "Fractions");
//        measurementsFractions.compareJacobiWithSeidelSparseMatrix(toCalculate, new MyFractions(0), "Fractions");
//        measurementsFractions.compareJacobiWithSeidelNormalMatrix(toCalculate, new MyFractions(0), "Fractions");
//        for (int i = 10; i <= 50; i += 10) {
//            measurementsFractions.getIterationNumber(i, 0.000001, new MyFractions(0), "Fractions-6" + i);
//            measurementsFractions.getIterationNumber(i, 0.0000000001, new MyFractions(0), "Fractions-10" + i);
//            measurementsFractions.getIterationNumber(i, 0.00000000000001, new MyFractions(0), "Fractions-16" + i);
//        }
    }
}
