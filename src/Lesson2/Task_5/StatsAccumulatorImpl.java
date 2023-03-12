package Lesson2.Task_5;

public class StatsAccumulatorImpl implements StatsAccumulator {
    int count = 0;
    int sum = 0;
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;

    public StatsAccumulatorImpl() {
    }

    public StatsAccumulatorImpl(int... nums) {
        for (int n : nums) {
            this.add(n);
        }
    }

    @Override
    public void add(int value) {
        this.count += 1;
        this.sum += value;
        if (value < min) {        // обновление min и max после проверок
            min = value;
        }
        if (value > max) {
            max = value;
        }
    }

    @Override
    public int getMin() {
        return this.min;
    }

    @Override
    public int getMax() {
        return this.max;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Double getAvg() {
        if (getCount() == 0) {        // проверка делителя на 0
            return 0.0;
        }
        return (this.sum + 0.0) / getCount();
    }
}
