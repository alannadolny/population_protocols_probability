package Tests;

import Gausses.GaussForSparseMatrix;
import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.MyDouble;
import Variables.MyFloat;
import Variables.MyFractions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestGaussForSparseMatrix {
    private SparseMatrix<MyFloat> matrixF;
    private SparseMatrix<MyDouble> matrixD;
    private SparseMatrix<MyFractions> matrixFr;

    private SparseMatrix<MyFloat> matrixF2;
    private SparseMatrix<MyDouble> matrixD2;
    private SparseMatrix<MyFractions> matrixFr2;
    private GaussForSparseMatrix<MyFloat> gaussF;
    private GaussForSparseMatrix<MyDouble> gaussD;
    private GaussForSparseMatrix<MyFractions> gaussFr;
    private String res = "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+1,0000 \n" +
            "+0,3333 \n" +
            "+0,0000 \n" +
            "+1,0000 \n" +
            "+1,0000 \n" +
            "+1,0000 \n";

    private String res2 = "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+1,0000 \n" +
            "+0,4580 \n" +
            "+0,1765 \n" +
            "+0,0556 \n" +
            "+0,0134 \n" +
            "+0,0022 \n" +
            "+0,0002 \n" +
            "+0,0000 \n" +
            "+1,0000 \n" +
            "+0,8159 \n" +
            "+0,4888 \n" +
            "+0,2240 \n" +
            "+0,0763 \n" +
            "+0,0175 \n" +
            "+0,0022 \n" +
            "+1,0000 \n" +
            "+0,9426 \n" +
            "+0,7688 \n" +
            "+0,4931 \n" +
            "+0,2354 \n" +
            "+0,0763 \n" +
            "+1,0000 \n" +
            "+0,9862 \n" +
            "+0,9213 \n" +
            "+0,7574 \n" +
            "+0,4931 \n" +
            "+1,0000 \n" +
            "+0,9978 \n" +
            "+0,9820 \n" +
            "+0,9213 \n" +
            "+1,0000 \n" +
            "+0,9998 \n" +
            "+0,9978 \n" +
            "+1,0000 \n" +
            "+1,0000 \n" +
            "+1,0000 \n";

    @Before
    public void setUp() {
        GenerateEquation test = new GenerateEquation(3);
        this.matrixF = new SparseMatrix<>(test, new MyFloat(0F));
        this.matrixD = new SparseMatrix<>(test, new MyDouble(0.0));
        this.matrixFr = new SparseMatrix<>(test, new MyFractions(0));
        this.matrixF.fillMatrix();
        this.matrixD.fillMatrix();
        this.matrixFr.fillMatrix();

        GenerateEquation test2 = new GenerateEquation(8);
        this.matrixF2 = new SparseMatrix<>(test2, new MyFloat(0F));
        this.matrixD2 = new SparseMatrix<>(test2, new MyDouble(0.0));
        this.matrixFr2 = new SparseMatrix<>(test2, new MyFractions(0));
        this.matrixF2.fillMatrix();
        this.matrixD2.fillMatrix();
        this.matrixFr2.fillMatrix();

        this.gaussF = new GaussForSparseMatrix<>();
        this.gaussD = new GaussForSparseMatrix<>();
        this.gaussFr = new GaussForSparseMatrix<>();
    }

    @Test
    public void testGaussPGFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.G(this.matrixF, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }

    @Test
    public void testGaussGJFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GJ(this.matrixF, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GS(this.matrixF, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }


    @Test
    public void testGaussPGDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.G(this.matrixD, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }


    @Test
    public void testGaussGJDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GJ(this.matrixD, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GS(this.matrixD, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @Test
    public void testGaussPGFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.G(this.matrixFr, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }


    @Test
    public void testGaussGJFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GJ(this.matrixFr, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GS(this.matrixFr, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @Test
    public void testGauss2PGFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.G(this.matrixF2, "PG");
        assertEquals("Gauss PG", this.res2, res.toString());
    }

    @Test
    public void testGauss2GJFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GJ(this.matrixF2, 100);
        assertEquals("Gauss GJ", this.res2, res.toString());
    }

    @Test
    public void testGauss2GSFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GS(this.matrixF2, 100);
        assertEquals("Gauss GS", this.res2, res.toString());
    }


    @Test
    public void testGauss2PGDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.G(this.matrixD2, "PG");
        assertEquals("Gauss PG", this.res2, res.toString());
    }


    @Test
    public void testGauss2GJDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GJ(this.matrixD2, 100);
        assertEquals("Gauss GJ", this.res2, res.toString());
    }

    @Test
    public void testGauss2GSDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GS(this.matrixD2, 100);
        assertEquals("Gauss GS", this.res2, res.toString());
    }

    @Test
    public void testGauss2PGFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.G(this.matrixFr2, "PG");
        assertEquals("Gauss PG", this.res2, res.toString());
    }


    @Test
    public void testGauss2GJFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GJ(this.matrixFr2, 100);
        assertEquals("Gauss GJ", this.res2, res.toString());
    }

    @Test
    public void testGauss2GSFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GS(this.matrixFr2, 100);
        assertEquals("Gauss GS", this.res2, res.toString());
    }


    @After
    public void tearDown() {
        this.matrixF = null;
        this.matrixD = null;
        this.matrixFr = null;
        this.res = null;
        this.matrixF2 = null;
        this.matrixD2 = null;
        this.matrixFr2 = null;
        this.res2 = null;
        this.gaussF = null;
        this.gaussD = null;
        this.gaussFr = null;
    }
}
