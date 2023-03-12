package Lesson2.Task_5;

public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulator s = new StatsAccumulatorImpl();    // как то создается )
        s.add(1);
        s.add(2);
        System.out.println(s.getAvg());      // 1.5 - среднее арифметическое элементов
        s.add(0);
        System.out.println(s.getMin());      // 0 - минимальное из переданных значений
        s.add(3);
        s.add(8);
        System.out.println(s.getMax());      // 8 - максимальный из переданных
        System.out.println(s.getCount());    // 5 - количество переданных элементов


        System.out.println("\nNext TEST 0");
        StatsAccumulator s0 = new StatsAccumulatorImpl();   // создается пустым
        System.out.println(s0.getMin());     // Integer.MAX_VALUE - минимальное из переданных значений
        System.out.println(s0.getMax());     // Integer.MIN_VALUE - максимальный из переданных
        System.out.println(s0.getCount());   // 0 - количество переданных элементов
        System.out.println(s0.getAvg());     // 0.0 - среднее арифметическое элементов


        System.out.println("\nNext TEST 1");
        StatsAccumulator s1 = new StatsAccumulatorImpl(1); // создается с 1 аргументом
        System.out.println(s1.getMin());     // 1 - минимальное из переданных значений
        System.out.println(s1.getMax());     // 1 - максимальный из переданных
        System.out.println(s1.getCount());   // 1 - количество переданных элементов
        System.out.println(s1.getAvg());     // 1.0 - среднее арифметическое элементов


        System.out.println("\nNext TEST 2");
        StatsAccumulator s2 = new StatsAccumulatorImpl(11, 12, 13, 14, 15); // создается со списком аргументов
        System.out.println(s2.getMin());     // 11 - минимальное из переданных значений
        System.out.println(s2.getMax());     // 15 - максимальный из переданных
        System.out.println(s2.getCount());   // 5 - количество переданных элементов
        s2.add(-11);
        s2.add(-12);
        s2.add(-13);
        s2.add(-14);
        s2.add(-25);
        System.out.println(s2.getCount());   // 10 - количество переданных элементов
        System.out.println(s2.getMin());     // -25 - минимальное из переданных значений
        System.out.println(s2.getAvg());     // -1.0 - среднее арифметическое элементов
    }
}
