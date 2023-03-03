package Lesson1;

import java.util.Random;
import java.util.Scanner;

public class Task_4_Guess {

    public static void main(String[] args) {
        int maxNumber = 99;                                     // случайный элемент от 1 до 99 (включительно)
        int number = new Random().nextInt(maxNumber) + 1;

        int maxAttempts = 10;                                   // всего попыток
        int curAttempt = 0;                                     // использовано попыток
        System.out.printf("Я загадал число от 1 до %d. У тебя %d попыток его угадать.\n", maxNumber, maxAttempts);

        try (Scanner scanner = new Scanner(System.in)) {

            while (++curAttempt <= maxAttempts){
                int n = scanner.nextInt();

                if (n == number) {
                    System.out.printf("Ты угадал число с %d попытки!\n", curAttempt);
                } else if (n < number) {
                    System.out.printf("Мое число больше! Осталось %d попыток\n", maxAttempts - curAttempt);
                } else if (n > number) {
                    System.out.printf("Мое число меньше! Осталось %d попыток\n", maxAttempts - curAttempt);
                }
            }

            System.out.println("Ты не угадал");

        } catch (Exception e) {
            System.out.println("Incorrect input.");
        }
    }

}
