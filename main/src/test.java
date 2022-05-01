import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.MyFloat;

public class test {
    public static void main(String[] args) {
        GenerateEquation test = new GenerateEquation(3); // tworzy klase do generowania
        SparseMatrix<MyFloat> matrix = new SparseMatrix<>(test, new MyFloat(0F)); //to juz jest sama, wlasciwa macierz
        SparseMatrix<MyFloat> matrixTemp = new SparseMatrix<>(test, new MyFloat(0F)); //to juz jest sama, wlasciwa macierz

        matrix.fillMatrix(); // to uzupelnia macierz
        matrixTemp.fillMatrix();

        NormalMatrix<MyFloat> matrix2 = new NormalMatrix<>(10, matrixTemp);

        System.out.println("Sparse matrix: ");
        System.out.println(matrix.toString()); // dalem override na toString i to wyswietla macierz zaokraglona do 2 po przecinku
        System.out.println("Normal matrix: ");
        System.out.println(matrix2.toString());
    }
}
