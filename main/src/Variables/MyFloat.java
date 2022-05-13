package Variables;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyFloat implements Operations<MyFloat> {
    private Float num;

    public MyFloat(Float num) {
        this.num = num;
    }

    @Override
    public void add(MyFloat a) {
        this.num = this.num + a.num;
    }

    @Override
    public void subtract(MyFloat a) {
        this.num = this.num - a.num;
    }

    @Override
    public void multiply(MyFloat a) {
        this.num = this.num * a.num;
    }

    @Override
    public void reverseSign() {
        this.num *= -1;
    }

    @Override
    public void divide(MyFloat a) {
        this.num = this.num / a.num;
    }

    @Override
    public MyFloat initializeToSparseMatrix(Long a, Long b) {
        return new MyFloat(a.floatValue() / b.floatValue());
    }

    @Override
    public MyFloat initialize(MyFloat a) {
        return new MyFloat(a.num);
    }

    @Override
    public MyFloat absolute() {
        return new MyFloat(Math.abs(this.num));
    }

    @Override
    public MyFloat initializeWithZero() {
        return new MyFloat(0F);
    }

    @Override
    public MyFloat initializeWithOne() {
        return new MyFloat(1F);
    }

    @Override
    public Integer compare(MyFloat a) {
//        System.out.println(a.num);
        return this.num.compareTo(a.num);
    }

    @Override
    public MyFloat initializeWithDouble(Double num) {
        return new MyFloat(num.floatValue());
    }

    @Override
    public MyFloat initizalizeWithInteger(Integer num) {
        return new MyFloat(num.floatValue());
    }

    @Override
    public BigDecimal returnValue() {
        return BigDecimal.valueOf(this.num);
    }
}
