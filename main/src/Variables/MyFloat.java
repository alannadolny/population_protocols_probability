package Variables;

public class MyFloat implements Operations<MyFloat> {

    private float number;

    public MyFloat(Float number) {
        this.number = number;
    }

    @Override
    public void add(MyFloat a) {
        this.number += a.number;
    }

    @Override
    public void subtract(MyFloat a) {
        this.number -= a.number;
    }

    @Override
    public void multiply(MyFloat a) {
        this.number *= a.number;
    }

    @Override
    public void divide(MyFloat a) {
        this.number /= a.number;
    }
}
