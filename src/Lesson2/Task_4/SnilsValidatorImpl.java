package Lesson2.Task_4;

public class SnilsValidatorImpl implements SnilsValidator{

    @Override
    public boolean validate(String snils) {
        if (snils == "00000000000")                         // проверка на нули (хотя проверка по алгоритму -> true)
            return false;

        boolean result = false;
//        snils = snils.trim().replaceAll("\\s*","");     // убираем пробелы, если присутствуют
        if (snils.length() == 11) {
            int lastTwoDigits = Integer.parseInt(snils.substring(9));   // число в 2 последних разрядах

            int sumOfNine = 0;                              // контрольное число для первых 9 цифр
            for (int i = 0; i < 9; i++) {
                sumOfNine += (9 - i) * Integer.parseInt(snils.substring(i, i+1));
            }

            while (sumOfNine >= 100) {                      // приведение контрольного числа по условию
//                sumOfNine = (sumOfNine == 100) ? 0 : sumOfNine % 101;         // короткий вариант вместо if'а ниже
                if (sumOfNine == 100) {
                    sumOfNine = 0;
                } else {        // if (sumOfNine > 100)
                    sumOfNine %= 101;
                }
            }

            // вывод для отладки
//            System.out.println(" snils=" + snils + " sumOfNine=" + sumOfNine + " lastTwoDigits=" + lastTwoDigits);

//            result = (sumOfNine == lastTwoDigits);          // короткий вариант вместо if'а ниже
            if (sumOfNine == lastTwoDigits) {
                result = true;
            }
        }
        return result;
    }
}
