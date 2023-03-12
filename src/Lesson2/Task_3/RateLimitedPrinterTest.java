package Lesson2.Task_3;

public class RateLimitedPrinterTest {
    public static void main(String[] args) {
        RateLimitedPrinter rateLimitedPrinter = new RateLimitedPrinter(1000);
        for (int i = 0; i < 1_000_000_000; i++) {
            rateLimitedPrinter.print(String.valueOf(i));
        }

        System.out.println("\nNext TEST");
        RateLimitedPrinter rateLimitedPrinter2 = new RateLimitedPrinter(500);
        for (int i = 0; i < 1_000_000_000; i++) {
            rateLimitedPrinter2.print(String.valueOf(i));
        }
    }
}
