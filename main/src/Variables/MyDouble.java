package Variables;

public class MyDouble implements Operations<MyDouble>{

    private double number;

    public MyDouble(double number) {
        this.number = number;
    }

    @Override
    public void add(MyDouble a) {
        this.number += a.number;
    }

    @Override
    public void subtract(MyDouble a) {
        this.number -= a.number;
    }

    @Override
    public void multiply(MyDouble a) {
        this.number *= a.number;
    }

    @Override
    public void divide(MyDouble a) {
        this.number /= a.number;
    }
}
