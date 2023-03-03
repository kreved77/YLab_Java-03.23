package Lesson1;

public class Task_3_MultTable {

    public static void main(String[] args) {
        System.out.println("Multiplication table:");      // for numbers [1 - 9]

        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.printf("%d x %d = %2d\n", i, j, i * j);
            }
        }
    }

}
