package Lesson3.Task_4;

import java.io.IOException;

public class PasswordValidatorTest {
    public static void main(String[] args) throws IOException, WrongLoginException, WrongPasswordException {
        PasswordValidator passwordValidator = new PasswordValidatorImpl();

        passwordValidator.validatePassword("", "", "");                                 // true
        passwordValidator.validatePassword("AZaz09_", "AZaz09_", "AZaz09_");            // true
        passwordValidator.validatePassword("User_admin", "Password_my", "Password_my"); // true

        // bad login
//        passwordValidator.validatePassword("$tr@ngeC#ar$", "AZaz09_", "AZaz09_");         // false login
//        passwordValidator.validatePassword("IncorrectLengthLogin", "AZaz09_", "AZaz09_"); // false login

        // bad password
//        passwordValidator.validatePassword("AZaz09_", "$tr@ngeC#ar$", "AZaz09_");         // false password
//        passwordValidator.validatePassword("AZaz09_", "IncorrectLengthPass1", "AZaz09_"); // false password

        // bad confirmPassword
//        passwordValidator.validatePassword("AZaz09_", "AZaz09_", "$tr@ngeC#ar$");         // false confirmPassword
//        passwordValidator.validatePassword("AZaz09_", "AZaz09_", "IncorrectLengthPass2"); // false confirmPassword

        // bad equals
//        passwordValidator.validatePassword("AZaz09_", "1111111111", "55555555555");   // false password + confirmPassword
//        passwordValidator.validatePassword("User_1", "Password_old", "Password_new"); // false password + confirmPassword

    }
}
