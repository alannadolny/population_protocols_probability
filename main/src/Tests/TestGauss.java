package Tests;

import Gausses.Gauss;
import Matrixes.NormalMatrix;
import Variables.MyDouble;
import Variables.MyFloat;
import Variables.MyFractions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestGauss {

    private NormalMatrix<MyFloat> matrixF;
    private NormalMatrix<MyFloat> vectorF;
    private NormalMatrix<MyFractions> matrixFr;
    private NormalMatrix<MyFractions> vectorFr;
    private NormalMatrix<MyDouble> matrixD;
    private NormalMatrix<MyDouble> vectorD;
    private Gauss<MyFloat> gaussF;
    private Gauss<MyDouble> gaussD;
    private Gauss<MyFractions> gaussFr;
    private String res = "+1,0000 \n" + "+2,0000 \n" + "-1,0000 \n" + "+1,0000 \n";

    @Before
    public void setUp() {
        List<Number> listOfNumbersToMat = new ArrayList<>();
        Collections.addAll(listOfNumbersToMat, 10, -1, 2, 0, -1, 11, -1, 3, 2, -1, 10, -1, 0, 3, -1, 8);
        List<Number> listOfNumbersToVec = new ArrayList<>();
        Collections.addAll(listOfNumbersToVec, 6, 25, -11, 15);
        List<MyFloat> floatToMat = new ArrayList<>();
        List<MyDouble> doubleToMat = new ArrayList<>();
        List<MyFractions> fractionsToMat = new ArrayList<>();
        List<MyFloat> floatToVec = new ArrayList<>();
        List<MyDouble> doubleToVec = new ArrayList<>();
        List<MyFractions> fractionsToVec = new ArrayList<>();

        listOfNumbersToMat.forEach((n) -> floatToMat.add(new MyFloat(n.floatValue())));
        listOfNumbersToMat.forEach((n) -> doubleToMat.add(new MyDouble(n.doubleValue())));
        listOfNumbersToMat.forEach((n) -> fractionsToMat.add(new MyFractions((long) n.doubleValue())));
        listOfNumbersToVec.forEach((n) -> floatToVec.add(new MyFloat(n.floatValue())));
        listOfNumbersToVec.forEach((n) -> doubleToVec.add(new MyDouble(n.doubleValue())));
        listOfNumbersToVec.forEach((n) -> fractionsToVec.add(new MyFractions((long) n.doubleValue())));

        this.matrixF = new NormalMatrix<>(4, floatToMat);
        this.vectorF = new NormalMatrix<>(1, floatToVec);
        this.matrixD = new NormalMatrix<>(4, doubleToMat);
        this.vectorD = new NormalMatrix<>(1, doubleToVec);
        this.matrixFr = new NormalMatrix<>(4, fractionsToMat);
        this.vectorFr = new NormalMatrix<>(1, fractionsToVec);

        this.gaussF = new Gauss<>();
        this.gaussD = new Gauss<>();
        this.gaussFr = new Gauss<>();
    }

    @Test
    public void testGaussPGFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.G(this.matrixF, this.vectorF, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }

    @Test
    public void testGaussGJFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GJ(this.matrixF, this.vectorF, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GS(this.matrixF, this.vectorF, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @Test
    public void testGaussPGDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.G(this.matrixD, this.vectorD, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }

    @Test
    public void testGaussGJDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GJ(this.matrixD, this.vectorD, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GS(this.matrixD, this.vectorD, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @Test
    public void testGaussPGFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.G(this.matrixFr, this.vectorFr, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }

    @Test
    public void testGaussGJFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GJ(this.matrixFr, this.vectorFr, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GS(this.matrixFr, this.vectorFr, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @After
    public void tearDown() {
        this.matrixF = null;
        this.vectorF = null;
        this.matrixD = null;
        this.vectorD = null;
        this.res = null;
        this.gaussF = null;
        this.gaussD = null;
    }

}
