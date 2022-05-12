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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;


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
                this.saveResults("GJ - precision: " + expectedPrecision + " in iteration: " + iteration, "compareJacobiWithSeidelSparseMatrix1" + type, "data/");
                gj = true;
            }
            if (this.getMaximumNorm(normsGS).returnValue().compareTo(new BigDecimal(String.valueOf(expectedPrecision))) < 0 && !gs) {
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
            OptimizedGaussForSparseMatrix<T> gauss = new OptimizedGaussForSparseMatrix<>();

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
        this.saveResults(results.toString(), "compareSparseMatrixWithNormalMatrix" + type, "data/");
    }

    public void verifyIterativeMethods(ArrayList<Integer> values, T el, String type, Integer iterationsNumber, Integer symulationsNumber) throws IOException {

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
            NormalMatrix<T> resultGJ = gauss.GJ(sparseMatrixGJ, iterationsNumber);
            NormalMatrix<T> resultGS = gauss.GS(sparseMatrixGS, iterationsNumber);
            SolveMatrix monteCarlo = new SolveMatrix(i, symulationsNumber);
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
        this.saveResults(results.toString(), "verifyIterativeMethod" + type + iterationsNumber, "data/");
    }

    public void compareSlowerGaussesWithFaster(ArrayList<Integer> values, T el) throws IOException {
        StringBuilder results = new StringBuilder();
        for (int i : values) {
            GenerateEquation generateEquation = new GenerateEquation(i);
            SparseMatrix<T> sparseMatrixGJSlower = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGJFaster = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGSSlower = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixGSFaster = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixPGSlower = new SparseMatrix<>(generateEquation, el);
            SparseMatrix<T> sparseMatrixPGFaster = new SparseMatrix<>(generateEquation, el);
            sparseMatrixGJSlower.fillMatrix();
            sparseMatrixGJFaster.fillMatrix();
            sparseMatrixGSSlower.fillMatrix();
            sparseMatrixGSFaster.fillMatrix();
            sparseMatrixPGSlower.fillMatrix();
            sparseMatrixPGFaster.fillMatrix();

            GaussForSparseMatrix<T> gaussForSparse1 = new GaussForSparseMatrix<>();
            OptimizedGaussForSparseMatrix<T> gaussForSparse2 = new OptimizedGaussForSparseMatrix<>();

            long start = System.currentTimeMillis();
            gaussForSparse2.G(sparseMatrixPGSlower, "PG");
            long stop = System.currentTimeMillis();

            results.append("sparse matrix PG - slower: ").append(stop - start).append("\n");

            start = System.currentTimeMillis();
            gaussForSparse1.G(sparseMatrixPGFaster, "PG");
            stop = System.currentTimeMillis();

            results.append("sparse matrix PG - faster: ").append(stop - start).append("\n");

            start = System.currentTimeMillis();
            gaussForSparse1.GJ(sparseMatrixGJSlower, 100);
            stop = System.currentTimeMillis();

            results.append("sparse matrix GJ - slower: ").append(stop - start).append("\n");

            start = System.currentTimeMillis();
            gaussForSparse2.GJ(sparseMatrixGJFaster, 100);
            stop = System.currentTimeMillis();

            results.append("sparse matrix GJ - faster: ").append(stop - start).append("\n");

            start = System.currentTimeMillis();
            gaussForSparse1.GS(sparseMatrixGSSlower, 100);
            stop = System.currentTimeMillis();

            results.append("sparse matrix GS - slower: ").append(stop - start).append("\n");

            start = System.currentTimeMillis();
            gaussForSparse2.GS(sparseMatrixGSFaster, 100);
            stop = System.currentTimeMillis();

            results.append("sparse matrix GS - faster: ").append(stop - start).append("\n");
        }
        this.saveResults(results.toString(), "compareSlowerGaussesWithFaster", "data/");
    }

    public static void main(String[] args) throws IOException {
        Measurements<MyDouble> measurementsDouble = new Measurements<>();
        Measurements<MyFloat> measurementsFloat = new Measurements<>();
        Measurements<MyFractions> measurementsFractions = new Measurements<>();

        // Double
        ArrayList<Integer> toCalculate = new ArrayList<>();
        Collections.addAll(toCalculate, 200);
//
//        measurementsDouble.compareSparseMatrixWithNormalMatrix(toCalculate, new MyDouble(0D), "Double");
        measurementsDouble.compareJacobiWithSeidelSparseMatrix(toCalculate, new MyDouble(0D), "Double");
//        measurementsDouble.compareJacobiWithSeidelNormalMatrix(toCalculate, new MyDouble(0D), "Double");

        // Float
//        measurementsFloat.compareSparseMatrixWithNormalMatrix(toCalculate, new MyFloat(0F), "Floats");
//        measurementsFloat.compareJacobiWithSeidelSparseMatrix(toCalculate, new MyFloat(0F), "Floats");
//        measurementsFloat.compareJacobiWithSeidelNormalMatrix(toCalculate, new MyFloat(0F), "Floats");


//        toCalculate = new ArrayList<>();
//        Collections.addAll(toCalculate, 20, 40, 60, 80, 100);

        //Fractions
//        measurementsFractions.compareSparseMatrixWithNormalMatrix(toCalculate, new MyFractions(0), "Fractions");
//        measurementsFractions.compareJacobiWithSeidelSparseMatrix(toCalculate, new MyFractions(0), "Fractions");
//        measurementsFractions.compareJacobiWithSeidelNormalMatrix(toCalculate, new MyFractions(0), "Fractions");
//        measurementsDouble.compareSlowerGaussesWithFaster(toCalculate, new MyDouble(0D));

//        ArrayList<ArrayList<Integer>> listWithIterations = new ArrayList<>();
//        ArrayList<Integer> first = new ArrayList<>();
//        first.add(50);
//        ArrayList<Integer> second = new ArrayList<>();
//        second.add(100);
//        ArrayList<Integer> third = new ArrayList<>();
//        third.add(150);
//        ArrayList<Integer> fourth = new ArrayList<>();
//        fourth.add(200);
//        Collections.addAll(listWithIterations, first, second, third, fourth);
//        for (int i = 0; i < listWithIterations.size(); i++) {
//            measurementsDouble.verifyIterativeMethods(listWithIterations.get(i), new MyDouble(0D), "Double", 100 * (10 * i), 100_000 * (10 * i));
//            measurementsFloat.verifyIterativeMethods(listWithIterations.get(i), new MyFloat(0F), "Float", 100 * (10 * i), 100_000 * (10 * i));
//        }

//        listWithIterations = new ArrayList<>();
//        first = new ArrayList<>();
//        first.add(10);
//        second = new ArrayList<>();
//        second.add(20);
//        third = new ArrayList<>();
//        third.add(30);
//        Collections.addAll(listWithIterations, first, second, third);
//        for (int i = 0; i < listWithIterations.size(); i++) {
//            measurementsFractions.verifyIterativeMethods(listWithIterations.get(i), new MyFractions(0), "Fraction", 100 * (10 * i), 100_000 * (10 * i));
//        }
    }
}