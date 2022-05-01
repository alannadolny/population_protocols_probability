import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.MyFloat;
import Gausses.Gauss;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        //ponizsze testy robilem tylko na MyFloat
        GenerateEquation test = new GenerateEquation(3); // tworzy klase do generowania
        SparseMatrix<MyFloat> matrix = new SparseMatrix<>(test, new MyFloat(0F)); //to juz jest wlasciwa sparse macierz
        SparseMatrix<MyFloat> matrixTemp = new SparseMatrix<>(test, new MyFloat(0F)); //to jest do obliczen w normalnej macierzy

        ArrayList<MyFloat> elementyDoWektora = new ArrayList<>(); // sparse matrix ma juz doklejony wektor, a zwykla z racji ze skopiowalem z projektu nie ma, wiec trzeba dokleic

        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(0F));
        elementyDoWektora.add(new MyFloat(1F));

        NormalMatrix<MyFloat> vector = new NormalMatrix<>(1,elementyDoWektora);


        matrix.fillMatrix(); // to uzupelnia macierz prawdopodobienstwami
        matrixTemp.fillMatrix();

        NormalMatrix<MyFloat> matrix2 = new NormalMatrix<>(10, matrixTemp);

        System.out.println("Sparse matrix: ");
        System.out.println(matrix.toString()); // dalem override na toString i to wyswietla macierz zaokraglona do 2 po przecinku
        System.out.println("Normal matrix: ");
        System.out.println(matrix2.toString());
//
        System.out.println("wektor do rozwiazania normalnej macierzy: ");
        System.out.println(vector.toString());
//
//        //test czy wgl dobrze to wszystko jest zrobione XD
//
        Gauss<MyFloat> mlodyG = new Gauss<>();
        NormalMatrix<MyFloat> result = mlodyG.G(matrix2, vector, "G");
//
        System.out.println("Rozwiazana normalna macierz: ");
        System.out.println(result.toString());
    }
}
