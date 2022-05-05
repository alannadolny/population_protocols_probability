import Gausses.GaussForSparseMatrix;
import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import MonteCarlo.SolveMatrix;
import Variables.MyDouble;

public class Measurements {

    public static void verifyIterativeMethods(int size) {
        GenerateEquation generateEquation = new GenerateEquation(size);
        SparseMatrix<MyDouble> sparseMatrix = new SparseMatrix<>(generateEquation, new MyDouble(0D));
        sparseMatrix.fillMatrix();
        GaussForSparseMatrix<MyDouble> gauss = new GaussForSparseMatrix<>();
        NormalMatrix<MyDouble> resultGJ = gauss.GJ(sparseMatrix,100);

        SolveMatrix monteCarlo = new SolveMatrix(size);
        monteCarlo.solve();


        System.out.println("GJ results: ");
        System.out.println(resultGJ);

        System.out.println("MonteCarlo results: ");
        System.out.println(monteCarlo);

    }

    public static void main(String[] args) {
        verifyIterativeMethods(5);
    }
}
