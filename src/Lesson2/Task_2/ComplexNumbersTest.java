package Lesson2.Task_2;

public class ComplexNumbersTest {
    public static void main(String[] args) {
        test1();        // basic test - with answers
        test2();        // use Integer.MAX_VALUE
        test3();
        test4();        // nums with no real part, only imaginary: -3i , -7i
    }

    private static void test1() {
        // Create a complex number  - call its class constructor.
        ComplexNumbers c1 = new ComplexNumbersImpl(2, 3);
        ComplexNumbers c2 = new ComplexNumbersImpl(5, 7);

            // (2+3i)+(5+7i) = 7+10i
            // (2+3i)-(5+7i) = -3-4i
            // (2+3i)*(5+7i) = -11+29i
            // |(2+3i)| = √13 = 3.6055
            // |(5+7i)| = √74 = 8.6023


        System.out.println("\n Next TEST");
        System.out.println("1st complex number = " + c1.toString());
        System.out.println("2nd complex number = " + c2.toString());

        // Get sum
        ComplexNumbers sum = new ComplexNumbersImpl();
        sum = sum.add(c1, c2);
        System.out.println("The sum of two complex numbers: " + sum.toString());

        // Get difference
        ComplexNumbers diff = new ComplexNumbersImpl();
        diff = diff.subtract(c1, c2);
        System.out.println("The difference of two complex numbers: " + diff.toString());

        // Get product
        System.out.println("The product of two complex numbers: " + new ComplexNumbersImpl().multiply(c1, c2).toString());

        // Get module
        System.out.println("The module of complex number " + c1.toString() + " is " + new ComplexNumbersImpl().mod(c1));
        System.out.println("The module of complex number " + c2.toString() + " is " + new ComplexNumbersImpl().mod(c2));
    }


    private static void test2() {
        ComplexNumbers c11 = new ComplexNumbersImpl(Integer.MAX_VALUE / 2, Integer.MAX_VALUE /3);
        ComplexNumbers c12 = new ComplexNumbersImpl(1.5, 1.5);

        System.out.println("\n Next TEST");
        System.out.println("1st complex number = " + c11.toString());
        System.out.println("2nd complex number = " + c12.toString());

        System.out.println("The sum of two complex numbers is " + new ComplexNumbersImpl().add(c11, c12).toString());
        System.out.println("The difference of two complex numbers is " + new ComplexNumbersImpl().subtract(c11, c12).toString());
        System.out.println("The product of two complex numbers is " + new ComplexNumbersImpl().multiply(c11, c12).toString());
        System.out.println("The module of complex number " + c11.toString() + " is " + new ComplexNumbersImpl().mod(c11));
        System.out.println("The module of complex number " + c12.toString() + " is " + new ComplexNumbersImpl().mod(c12));
    }


    private static void test3() {
        ComplexNumbers c1 = new ComplexNumbersImpl(1.2);
        ComplexNumbers c2 = new ComplexNumbersImpl(34, 5);

        System.out.println("\n Next TEST");
        System.out.println("1st complex number = " + c1.toString());
        System.out.println("2nd complex number = " + c2.toString());

        System.out.println("The sum of two complex numbers: " + new ComplexNumbersImpl().add(c1, c2).toString());
        System.out.println("The difference of two complex numbers: " + new ComplexNumbersImpl().subtract(c1, c2).toString());
        System.out.println("The product of two complex numbers: " + new ComplexNumbersImpl().multiply(c1, c2).toString());
        System.out.println("The module of complex number " + c1.toString() + " is " + new ComplexNumbersImpl().mod(c1));
        System.out.println("The module of complex number " + c2.toString() + " is " + new ComplexNumbersImpl().mod(c2));
    }


    private static void test4() {
        ComplexNumbers c1 = new ComplexNumbersImpl(0, -3);
        ComplexNumbers c2 = new ComplexNumbersImpl(0, -7);

        System.out.println("\n Next TEST");
        System.out.println("1st complex number = " + c1.toString());
        System.out.println("2nd complex number = " + c2.toString());

        System.out.println("The sum of two complex numbers: " + new ComplexNumbersImpl().add(c1, c2).toString());
        System.out.println("The difference of two complex numbers: " + new ComplexNumbersImpl().subtract(c1, c2).toString());
        System.out.println("The product of two complex numbers: " + new ComplexNumbersImpl().multiply(c1, c2).toString());
        System.out.println("The module of complex number " + c1.toString() + " is " + new ComplexNumbersImpl().mod(c1));
        System.out.println("The module of complex number " + c2.toString() + " is " + new ComplexNumbersImpl().mod(c2));
    }
}
