package Lesson3.Task_4;

public class WrongPasswordException extends Throwable {
    private String message;

    public WrongPasswordException() {
        this.message = "Недопустимый пароль";
    }
    public WrongPasswordException(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "WrongPasswordException: " + message;
    }

}
