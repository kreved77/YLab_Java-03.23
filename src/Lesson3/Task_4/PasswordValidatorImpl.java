package Lesson3.Task_4;

/**
 Базовая реализация для WrongLoginException
 Альтернативная реализация для WrongPasswordException

 validatePassword не может вернуть значение false из-за исключений, но все же предусмотрел
*/

public class PasswordValidatorImpl implements PasswordValidator {
    String regex = "[A-Za-z0-9_]*";
    int lengthMax = 19;
    @Override
    public boolean validatePassword(String login, String password, String confirmPassword)
            throws WrongLoginException, WrongPasswordException {
        boolean result = false;
        
        if (!login.matches(regex)) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        } else if (login.length() >= lengthMax) {
            throw new WrongLoginException("Логин слишком длинный");

        } else if (!password.matches(regex) || !confirmPassword.matches(regex)) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        } else if (password.length() >= lengthMax || confirmPassword.length() >= lengthMax) {
            throw new WrongPasswordException("Пароль слишком длинный");

        } else if (!password.equals(confirmPassword)) {
            throw new WrongPasswordException("Пароль и подтверждение не совпадают");

        } else {
            result = true;
            printResult(login, password, confirmPassword, result);
        }

        return result;  // always true
    }

    private void printResult(String login, String password, String confirmPassword, boolean result) {
        System.out.println("This set: ");
        System.out.println(
                login + " -> login ok\n"
                + password + " -> password ok\n"
                + confirmPassword + " -> confirmPassword ok"
        );

        if (result) {
            System.out.println(result + " -> passwords are equal\n");
        } else {
            System.out.println(result + " -> passwords are not equal\n");
        }
    }
}
