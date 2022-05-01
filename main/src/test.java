
public class test {
    public static void main(String[] args) {
        GenerateEquation test = new GenerateEquation(3); // tworzy klase do generowania
        SparseMatrix matrix = new SparseMatrix(test); //to juz jest sama, wlasciwa macierz
        matrix.fillMatrix(); // to uzupelnia macierz
        System.out.println(matrix.toString()); // dalem override na toString i to wyswietla macierz zaokraglona do 2 po przecinku
    }
}
