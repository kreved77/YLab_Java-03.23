package Lesson1;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class Task_1_Stars {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter number of rows:");
            int n = scanner.nextInt();
            System.out.println("Enter number of columns:");
            int m = scanner.nextInt();
            System.out.println("Enter symbol:");
            String template = scanner.next();

            System.out.println("Result:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(template + " ");
                }
                System.out.println();
            }
//            printStars(n, m, template);
        } catch (Exception e) {
            System.out.println("Incorrect input.");
        }
    }


// Test DATA
    private static String printStars(int n, int m, String s) {
        StringBuilder result = new StringBuilder();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    result.append(s + " ");
                }
                result.append("\n");
            }

        System.out.println("Result:\n" + result.toString());
        return result.toString();
    }

    @Test
    public void FixedTests() {
        assertEquals(("$ $ $ \n$ $ $ \n"), Task_1_Stars.printStars(2, 3, "$"));
        assertEquals(("% % \n% % \n"), Task_1_Stars.printStars(2, 2, "%"));
        assertEquals(("4 4 4 4 \n4 4 4 4 \n4 4 4 4 \n4 4 4 4 \n"), Task_1_Stars.printStars(4, 4, "4"));
        assertEquals(("! \n! \n! \n! \n! \n"), Task_1_Stars.printStars(5, 1, "!"));
    }
}
