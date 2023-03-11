package Lesson2.Task_2;

public class ComplexNumbersImpl implements ComplexNumbers {
    double realPart = 0;
    double imagPart = 0;

    public ComplexNumbersImpl() {
    }

    public ComplexNumbersImpl(double realPart) {
        this.realPart = realPart;
    }

    public ComplexNumbersImpl(double realPart, double imagPart) {
        this.realPart = realPart;
        this.imagPart = imagPart;
    }

    // The sum of two complex numbers
    @Override
    public ComplexNumbers add(ComplexNumbers a, ComplexNumbers b) {
        ComplexNumbersImpl aImpl = (ComplexNumbersImpl) a;
        ComplexNumbersImpl bImpl = (ComplexNumbersImpl) b;
        return new ComplexNumbersImpl(aImpl.realPart + bImpl.realPart, aImpl.imagPart + bImpl.imagPart);
    }


    // The difference of two complex numbers
    @Override
    public ComplexNumbers subtract(ComplexNumbers a, ComplexNumbers b) {
        ComplexNumbersImpl aImpl = (ComplexNumbersImpl) a;
        ComplexNumbersImpl bImpl = (ComplexNumbersImpl) b;
        return new ComplexNumbersImpl(aImpl.realPart - bImpl.realPart, aImpl.imagPart - bImpl.imagPart);
    }

    // The product of two complex numbers
    @Override
    public ComplexNumbers multiply(ComplexNumbers a, ComplexNumbers b) {
        ComplexNumbersImpl aImpl = (ComplexNumbersImpl) a;
        ComplexNumbersImpl bImpl = (ComplexNumbersImpl) b;
        ComplexNumbersImpl result = new ComplexNumbersImpl();
        result.realPart = (aImpl.realPart * bImpl.realPart) - (aImpl.imagPart * bImpl.imagPart);
        result.imagPart = (aImpl.imagPart * bImpl.realPart) + (aImpl.realPart * bImpl.imagPart);
        return result;
    }

    // The module of complex number
    @Override
    public double mod(ComplexNumbers a) {
        ComplexNumbersImpl aImpl = (ComplexNumbersImpl) a;
        return Math.sqrt(aImpl.realPart * aImpl.realPart + aImpl.imagPart * aImpl.imagPart);
    }

    @Override
    public String toString() {
        if (this.imagPart == 0)                     // example: (1,2)
            return String.format("(%.1f)", this.realPart);
        else if (this.imagPart >= 0)                // example: (2,0 + 3,0i)
            return String.format("(%.1f + %.1fi)", this.realPart, this.imagPart);
        else    // if (this.imagPart < 0)              example: (3,0 -4,0i)
            return String.format("(%.1f %.1fi)", this.realPart, this.imagPart);
    }
}
