package Lesson3.Task_4;

import java.io.IOException;

public interface PasswordValidator {

    /**
     * Проверяет, что в строке содержится валидный пароль
     * @param login
     * @param password
     * @return результат проверки
     */
    boolean validatePassword(String login, String password, String confirmPassword) throws IOException, WrongLoginException, WrongPasswordException;
}
