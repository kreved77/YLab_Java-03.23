package Lesson2.Task_4;

public class SnilsValidatorTest {

    public static void main(String[] args) {
        System.out.println(new SnilsValidatorImpl().validate("01468870570"));       //false
        System.out.println(new SnilsValidatorImpl().validate("90114404441"));       //true

        System.out.println(new SnilsValidatorImpl().validate("01468870569"));       //true
        System.out.println(new SnilsValidatorImpl().validate("014688"));            //false
        System.out.println(new SnilsValidatorImpl().validate("01468870570555"));    //false
        System.out.println(new SnilsValidatorImpl().validate("99999999899"));       //false

        /*
        кейс когда после первого %101 - остаток от деления равен 100 -> 0 == 00
         */
        System.out.println(new SnilsValidatorImpl().validate("99999999700"));       //true
        System.out.println(new SnilsValidatorImpl().validate("99999999699"));       //true : 99 == 99

        // проверка на нули (хотя проверка по алгоритму -> true)
        SnilsValidator sv = new SnilsValidatorImpl();
        System.out.println(sv.validate("00000000000"));                             //false

    }
}
