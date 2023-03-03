package Lesson1;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Task_2_Pell {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the N-th Pell number:");      // 0 <= n <= 30
            int n = scanner.nextInt();

            verQueue(n);
            verArray(n);

        } catch (Exception e) {
            System.out.println("Incorrect input.");
        }
    }

    private static void verQueue(int n) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(0);                                             // n = 0
        queue.add(1);                                             // n = 1

        while (n-- > 0) {
            queue.add(queue.poll() + 2 * queue.peek());           // n > 1: 2*[n-1]+[n-2]
        }
        System.out.println("[verQueue] Result = " + queue.element());
    }

    private static void verArray(int n) {
        int[] array = new int[n+1];

        for (int i = 0; i <= n; i++) {
            if (i <= 1) array[i] = i;                                   // n[0] = 0; n[1] = 1
            if (i > 1)  array[i] = 2 * array[i-1] + array[i-2];         // n > 1: 2*[n-1]+[n-2]
        }
        System.out.printf("[verArray] The %d-th Pell number is %d.\n", n, array[n]);
    }

}
