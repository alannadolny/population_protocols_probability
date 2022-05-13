package Variables;

import java.math.BigDecimal;

public interface Operations<T> {
    void add(T a);

    void subtract(T a);

    void multiply(T a);

    void reverseSign();

    void divide(T a);

    T initializeToSparseMatrix(Long a, Long b);

    T initialize(T a);

    T absolute();

    T initializeWithZero();

    T initializeWithOne();

    T initializeWithDouble(Double num);

    T initizalizeWithInteger(Integer num);

    Integer compare(T a);

    BigDecimal returnValue();
}