import Gausses.GaussForSparseMatrix;
import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import MonteCarlo.MonteCarlo;
import MonteCarlo.SolveMatrix;
import Variables.MyFloat;
import Gausses.Gauss;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class test {
    public static void main(String[] args) {
//        SolveMatrix matrixxd = new SolveMatrix(3);
//        matrixxd.solve();
//        System.out.println("Wyniki z montecarlo: ");
//        System.out.println(matrixxd.toString());
//
        //MonteCarlo test3 = new MonteCarlo(1, 1, 3); // stworzenie monte carlo do obliczenia z 1 glosem na tak, 1 na nie, a lacznie wszystkich glosujacych bylo 3
        //System.out.println(test3.symulate()); // uruchomienie symulacji, tam ustawilem chyba 1mln iteracji
//        //ponizsze testy robilem tylko na MyFloat
        GenerateEquation test = new GenerateEquation(3); // tworzy klase do generowania
        SparseMatrix<MyFloat> matrixad = new SparseMatrix<>(test, new MyFloat(0F)); //to juz jest wlasciwa sparse macierz
        matrixad.fillMatrix();
//        System.out.println(matrixad.toString());
//
        GaussForSparseMatrix<MyFloat> testowyGauss = new GaussForSparseMatrix<>();
        NormalMatrix<MyFloat> testowyResult = testowyGauss.G(matrixad, "PG"); //here
//        NormalMatrix<MyFloat> testowyResult = testowyGauss.GJ(matrixad,5); //here
        System.out.println(testowyResult.toString());
        System.out.println("================");



//        testowyGauss.G()

        SparseMatrix<MyFloat> matrixTemp = new SparseMatrix<>(test, new MyFloat(0F)); //to jest do obliczen w normalnej macierzy
//
        ArrayList<MyFloat> elementyDoWektora = new ArrayList<>(); // sparse matrix ma juz doklejony wektor, a zwykla z racji ze skopiowalem z projektu nie ma, wiec trzeba dokleic
//
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

        NormalMatrix<MyFloat> vectorxd = new NormalMatrix<>(1, elementyDoWektora);
//
//
//        matrix.fillMatrix(); // to uzupelnia macierz prawdopodobienstwami
        matrixTemp.fillMatrix();
//
        NormalMatrix<MyFloat> matrix2 = new NormalMatrix<>(10, matrixTemp);
//
//        //System.out.println("Sparse matrix: ");
//        // System.out.println(matrix.toString()); // dalem override na toString i to wyswietla macierz zaokraglona do 2 po przecinku
//        //System.out.println("Normal matrix: ");
//        //System.out.println(matrix2.toString());
////
//        //System.out.println("wektor do rozwiazania normalnej macierzy: ");
//        //System.out.println(vector.toString());
////
////        //test czy wgl dobrze to wszystko jest zrobione XD
////
        Gauss<MyFloat> mlodyG = new Gauss<>();
//        NormalMatrix<MyFloat> result = mlodyG.G(matrix2, vectorxd, "G");
        NormalMatrix<MyFloat> result = mlodyG.GS(matrix2, vectorxd, 5);
 //       System.out.println(matrix2.toString());
        System.out.println("Rozwiazana normalna macierz: ");
        System.out.println(result.toString());


        // to co jest powyżej to działa na razie

        MyFloat n1 = new MyFloat(10F);
        MyFloat n2 = new MyFloat(-1F);
        MyFloat n3 = new MyFloat(2F);
        MyFloat n4 = new MyFloat(0F);
        MyFloat n5 = new MyFloat(-1F);
        MyFloat n6 = new MyFloat(11F);
        MyFloat n7 = new MyFloat(-1F);
        MyFloat n8 = new MyFloat(3F);
        MyFloat n9 = new MyFloat(2F);
        MyFloat n10 = new MyFloat(-1F);
        MyFloat n11 = new MyFloat(10F);
        MyFloat n12 = new MyFloat(-1F);
        MyFloat n13 = new MyFloat(0F);
        MyFloat n14 = new MyFloat(3F);
        MyFloat n15 = new MyFloat(-1F);
        MyFloat n16 = new MyFloat(8F);

        MyFloat v1 = new MyFloat(6F);
        MyFloat v2 = new MyFloat(25F);
        MyFloat v3 = new MyFloat(-11F);
        MyFloat v4 = new MyFloat(15F);

        List<MyFloat> toMat = new ArrayList<>();
        Collections.addAll(toMat, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16);
        List<MyFloat> toVec = new ArrayList<>();
        Collections.addAll(toVec, v1, v2, v3, v4);

        NormalMatrix<MyFloat> matrix = new NormalMatrix<>(4, toMat);
        NormalMatrix<MyFloat> vector = new NormalMatrix<>(1, toVec);

        Gauss<MyFloat> gauss= new Gauss<>();

        NormalMatrix<MyFloat> res= gauss.GS(matrix,vector,3);

//        System.out.println(res.toString());

        System.out.println("=======================================");
    }
}
