package Variables;

import java.math.BigInteger;

public class MyFraction implements Operations<MyFraction> {

    private BigInteger numerator;
    private BigInteger denumerator;

    public MyFraction(BigInteger numerator, BigInteger denumerator) {
        this.numerator = numerator;
        this.denumerator = denumerator;
    }

    @Override
    public void add(MyFraction a) {
        this.numerator = this.numerator.multiply(a.denumerator).add(a.numerator.multiply(this.denumerator));
        this.denumerator = this.denumerator.multiply(a.denumerator);
        simplifyFraction();

    }

    @Override
    public void subtract(MyFraction a) {
        this.numerator = this.numerator.multiply(a.denumerator).subtract(a.numerator.multiply(this.denumerator));
        this.denumerator = this.denumerator.multiply(a.denumerator);
        simplifyFraction();

    }

    @Override
    public void multiply(MyFraction a) {
        this.numerator = a.numerator.multiply(this.numerator);
        this.denumerator = a.denumerator.multiply(this.denumerator);
        simplifyFraction();
    }

    @Override
    public void divide(MyFraction a) {
        this.numerator = this.numerator.multiply(a.denumerator);
        this.denumerator = this.denumerator.multiply(a.numerator);
        simplifyFraction();
    }

    public void simplifyFraction() {
        BigInteger nwd = this.numerator.gcd(this.denumerator);
        this.numerator = this.numerator.divide(nwd);
        this.denumerator = this.denumerator.divide(nwd);
    }

}
