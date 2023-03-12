package Lesson2.Task_3;

public class RateLimitedPrinter {
    int interval;
    long lastTime = 0;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    public void print(String message) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= lastTime + interval) {
//            System.out.print("time(ms)=" + System.currentTimeMillis() + " -> ");      // check time millis
            System.out.println(message);
            lastTime = currentTime;
        }
    }
}
