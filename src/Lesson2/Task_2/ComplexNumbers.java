package Lesson2.Task_2;

public interface ComplexNumbers {

    /**
     * @param a комплексное число
     * @param b комплексное число
     * @return Возвращает сумму чисел - комплексное число
     */
    ComplexNumbers add(ComplexNumbers a, ComplexNumbers b);

    /**
     * @return Возвращает разность чисел - комплексное число
     */
    ComplexNumbers subtract(ComplexNumbers a, ComplexNumbers b);

    /**
     * @return Возвращает произведение чисел - комплексное число
     */
    ComplexNumbers multiply(ComplexNumbers a, ComplexNumbers b);

    /**
     * @param a комплексное число
     * @return Возвращает модуль числа
     */
    double mod(ComplexNumbers a);

    /**
     * @return Возвращает текстовое представление комплексного числа
     */
    String toString();
}
