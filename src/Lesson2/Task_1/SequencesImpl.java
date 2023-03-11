package Lesson2.Task_1;

public class SequencesImpl implements Sequences {
    private final int startNum = 1;                 // set the first number of sequence

    private String methodName() {                   // auto print the methodName (for cases E - J)
        return Thread.currentThread().getStackTrace()[2].getMethodName().toUpperCase() + ". ";
    }

    @Override
    public void a(int n) {
        int curNum = startNum + 1;                  // custom first number
        System.out.print("A. ");
        for (int i = 1; i < n; i++) {
            System.out.printf("%3d, ", curNum * i);
        }
        System.out.println(curNum * n + "...");     // вариант с выводом после последнего числа - не запятой, а 3-х точек
    }

    @Override
    public void b(int n) {
        int curNum = startNum;
        System.out.print("B. ");
        for (int i = 1; i <= n; i++) {
            System.out.printf("%3d, ", curNum);
            curNum += 2;
        }
        System.out.println("...");
    }

    @Override
    public void c(int n) {
        System.out.print("C. ");
        for (int i = startNum; i <= n; i++) {       // begin iterate from startNum
            System.out.printf("%3d, ", i * i);
        }
        System.out.println("...");
    }

    @Override
    public void d(int n) {
        System.out.print("D. ");
        for (int i = startNum; i <= n; i++) {
            System.out.printf("%3d, ", (int) Math.pow(i, 3));
        }
        System.out.println("...");
    }

    @Override
    public void e(int n) {
        int curNum = startNum;
        System.out.print(methodName());     // get methodName and print
        for (int i = 1; i <= n; i++) {
            System.out.printf("%3d, ", curNum);
            curNum *= -1;
        }
        System.out.println("...");
    }

    @Override
    public void f(int n) {
        System.out.print(methodName());
        for (int i = startNum; i <= n; i++) {
            if (i % 2 == 0) {
                System.out.printf("%3d, ", i * -1);
            } else {
                System.out.printf("%3d, ", i);
            }
        }
        System.out.println("...");
    }


    @Override
    public void g(int n) {
        System.out.print(methodName());
        for (int i = startNum; i <= n; i++) {
            if (i % 2 == 0) {
                System.out.printf("%3d, ", (int) Math.pow(i, 2) * -1);
            } else {
                System.out.printf("%3d, ", (int) Math.pow(i, 2));
            }
        }
        System.out.println("...");
    }

    @Override
    public void h(int n) {
        int curNum = startNum;
        System.out.print(methodName());
        for (int i = 1; i <= n; i += 2) {
            if (i == n) {                                     // if n is even - sequence ends with "0", otherwise - number
                System.out.printf("%3d, ", curNum);           // print: num,
            } else {
                System.out.printf("%3d, %3d, ", curNum, 0);   // print: num, 0,
            }
            curNum++;
        }
        System.out.println("...");
    }

    @Override
    public void i(int n) {
        int curNum = startNum;
        System.out.print(methodName());
        for (int i = 1; i <= n; i++) {
            curNum = curNum * i;
            System.out.printf("%3d, ", curNum);
        }
        System.out.println("...");
    }

    @Override
    public void j(int n) {
        System.out.print(methodName());
        int prevNum = 0;
        int curNum = startNum;
        int sum;
        for (int i = 1; i <= n; i++) {
            System.out.printf("%3d, ", curNum);
            sum = curNum + prevNum;
            prevNum = curNum;                   // move frame right after every step
            curNum = sum;
        }
        System.out.println("...");
    }
}
