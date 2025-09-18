package com.pikabu.testing.domain;

import java.util.regex.Pattern;

public class User {
    private String username;
    private String email;
    private String password;
    private boolean isLoggedIn;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isLoggedIn = false;
    }

    public boolean isUsernameValid() {
        if (username == null || username.trim().isEmpty()) return false;
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_а-яА-Я]{4,16}$");
        return pattern.matcher(username).matches();
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_\\-]+(?:\\.[A-Za-z0-9+_\\-]+)*@" +
                    "(?:[A-Za-z0-9](?:[A-Za-z0-9\\-]{0,61}[A-Za-z0-9])?\\.)+" +
                    "[A-Za-z]{2,}$"
    );

    public boolean isEmailValid() {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public boolean isPasswordValid() {
        if (password == null) return false;
        if (password.length() < 6) return false;

        boolean hasLetter = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) break;
        }
        return hasLetter && hasDigit;
    }

    public PasswordStrength getPasswordStrength() {
        if (password == null || password.length() < 6) return PasswordStrength.WEAK;

        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score++;

        if (score <= 2) return PasswordStrength.WEAK;
        if (score <= 4) return PasswordStrength.MEDIUM;
        return PasswordStrength.STRONG;
    }

    public boolean login() {
        if (isUsernameValid() && isPasswordValid()) {
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    public void logout() { this.isLoggedIn = false; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isLoggedIn() { return isLoggedIn; }

    public enum PasswordStrength { WEAK, MEDIUM, STRONG }
}
