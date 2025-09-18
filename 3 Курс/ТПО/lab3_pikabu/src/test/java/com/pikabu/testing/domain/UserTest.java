package com.pikabu.testing.domain;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.assertj.core.api.Assertions.*;

public class UserTest {
    private User user;
    private String validUsername = "wertygjh";
    private String validPassword = "password1";

    @BeforeMethod
    public void setUp() {
        user = new User("testuser", "test@example.com", "password123");
    }

    @Test
    public void testValidUsernameValidation() {
        user.setUsername("abcd");
        assertThat(user.isUsernameValid()).isTrue();

        user.setUsername("a".repeat(16));
        assertThat(user.isUsernameValid()).isTrue();

        user.setUsername("user_123");
        assertThat(user.isUsernameValid()).isTrue();

        user.setUsername("юзер123");
        assertThat(user.isUsernameValid()).isTrue();
    }

    @Test
    public void testInvalidUsernameValidation() {
        user.setUsername("abc");
        assertThat(user.isUsernameValid()).isFalse();

        user.setUsername("a".repeat(17));
        assertThat(user.isUsernameValid()).isFalse();

        user.setUsername("user@123");
        assertThat(user.isUsernameValid()).isFalse();

        user.setUsername("");
        assertThat(user.isUsernameValid()).isFalse();

        user.setUsername(null);
        assertThat(user.isUsernameValid()).isFalse();

        user.setUsername("   ");
        assertThat(user.isUsernameValid()).isFalse();
    }

    @DataProvider
    public Object[][] emailTestData() {
        return new Object[][] {
                {"test@example.com", true},
                {"user.name@domain.co.uk", true},
                {"user+tag@example.org", true},
                {"user_name@example-domain.com", true},
                {"invalid-email", false},
                {"@example.com", false},
                {"user@", false},
                {"user@.com", false},
                {"user..name@example.com", false},
                {"", false},
                {null, false}
        };
    }

    @Test(dataProvider = "emailTestData")
    public void testEmailValidation(String email, boolean expected) {
        user.setEmail(email);
        assertThat(user.isEmailValid()).isEqualTo(expected);
    }

    @Test
    public void testPasswordValidation() {
        user.setPassword("12345a");
        assertThat(user.isPasswordValid()).isTrue();

        user.setPassword("very_long_secure_password_123");
        assertThat(user.isPasswordValid()).isTrue();

        user.setPassword("12345");
        assertThat(user.isPasswordValid()).isFalse();

        user.setPassword("very_long_secure_password_123abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcd");
        assertThat(user.isPasswordValid()).isTrue();

        user.setPassword("");
        assertThat(user.isPasswordValid()).isFalse();

        user.setPassword(null);
        assertThat(user.isPasswordValid()).isFalse();
    }

    @Test
    public void testPasswordStrengthEvaluation() {
        user.setPassword("12345qwe");
        assertThat(user.getPasswordStrength()).isEqualTo(User.PasswordStrength.MEDIUM);

        user.setPassword("Password123");
        assertThat(user.getPasswordStrength()).isEqualTo(User.PasswordStrength.MEDIUM);

        user.setPassword("MySecure123!");
        assertThat(user.getPasswordStrength()).isEqualTo(User.PasswordStrength.STRONG);

        user.setPassword(null);
        assertThat(user.getPasswordStrength()).isEqualTo(User.PasswordStrength.WEAK);

        user.setPassword("12345");
        assertThat(user.getPasswordStrength()).isEqualTo(User.PasswordStrength.WEAK);
    }

    @Test
    public void testLoginFunctionality() {
        user.setUsername(validUsername);
        user.setPassword(validPassword);

        assertThat(user.login()).isTrue();
        assertThat(user.isLoggedIn()).isTrue();

        User invalidUser = new User("ab", "invalid-email", "123");
        assertThat(invalidUser.login()).isFalse();
        assertThat(invalidUser.isLoggedIn()).isFalse();
    }

    @Test
    public void testLogoutFunctionality() {
        user.setUsername(validUsername);
        user.setPassword(validPassword);
        user.login();
        assertThat(user.isLoggedIn()).isTrue();

        user.logout();
        assertThat(user.isLoggedIn()).isFalse();

        user.logout();
        assertThat(user.isLoggedIn()).isFalse();
    }

    @Test
    public void testUserCreationWithValidData() {
        User newUser = new User("newuser", "new@example.com", "newpass123");

        assertThat(newUser.getUsername()).isEqualTo("newuser");
        assertThat(newUser.getEmail()).isEqualTo("new@example.com");
        assertThat(newUser.getPassword()).isEqualTo("newpass123");
        assertThat(newUser.isLoggedIn()).isFalse();
    }
}
