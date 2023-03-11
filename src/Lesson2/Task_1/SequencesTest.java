package Lesson2.Task_1;

public class SequencesTest {
    public static void main(String[] args) {

        int n = 5;                          // set the number of elements - from task description A-J
        SequencesImpl sequence = new SequencesImpl();

        sequence.a(n);
        sequence.b(n);
        sequence.c(n);
        sequence.d(n);

        n = 6;
        sequence.e(n);
        sequence.f(n);

        n = 5;
        sequence.g(n);

        n = 7;
        sequence.h(n);

        n = 6;
        sequence.i(n);

        n = 8;
        sequence.j(n);
//        new SequencesImpl().j(n);
    }
}
