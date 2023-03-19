package Lesson3.Task_4;

// альтернативная реализация для WrongPasswordException

public class WrongLoginException extends Exception {

    public WrongLoginException() {
        super();
    }
    public WrongLoginException(String message) {
        super(message);
    }
}
