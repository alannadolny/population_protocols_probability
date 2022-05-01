import javafx.util.Pair;

public class test {
    public static void main(String[] args) {
        GenerateEquation test = new GenerateEquation(3);
        SparseMatrix matrix = new SparseMatrix(test);
        //System.out.println(test.transitionFunction.get(new Pair<>("N", "U")));
        //System.out.println(test.GenerateVoters(1, 1));
        //System.out.println(matrix.getSparseMatrix());
        matrix.fillMatrix();
        //System.out.println(matrix.getSparseMatrix());
    }
}
