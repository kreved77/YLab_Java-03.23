package Lesson2.Task_3;

public class RateLimitedPrinter {
    private final int interval;
    private long lastTime = 0;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public long getLastTime() {
        return lastTime;
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
