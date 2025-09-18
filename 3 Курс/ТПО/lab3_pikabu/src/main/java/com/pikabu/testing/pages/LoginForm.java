package com.pikabu.testing.pages;

import org.openqa.selenium.*;
import java.util.List;
import java.time.Duration;

public class LoginForm extends BasePage {

    private final By form        = By.xpath("//form[@id='signin-form' or .//input[@name='password' or @type='password']]");

    private final By usernameInp = By.xpath(
            "(" +
                    "//form[@id='signin-form'] | //form[.//input[@name='password' or @type='password']]" +
                    ")//input[" +
                    "(@name='username' or @name='login' or @type='email' or @type='text')" +
                    " and not(@name='password')" +
                    "]"
    );

    private final By passwordInp = By.xpath(
            "(" +
                    "//form[@id='signin-form'] | //form[.//input[@name='password' or @type='password']]" +
                    ")//input[" +
                    "@name='password' or @type='password' or " +
                    "contains(translate(@placeholder,'ПАРОЛЬPASSWORD','парольpassword'),'пароль') or " +
                    "@autocomplete='current-password' or @autocomplete='password'" +
                    "]"
    );

    private final By submitBtn   = By.xpath(
            "(" +
                    "//form[@id='signin-form'] | //form[.//input[@name='password' or @type='password']]" +
                    ")//button[@type='submit' or contains(@class,'button')][1]"
    );

    private final By errorInline = By.xpath(
            "(" +
                    "//form[@id='signin-form'] | //form[.//input[@name='password' or @type='password']]" +
                    ")//*[contains(@class,'auth__error') or contains(@class,'error')][normalize-space()]"
    );
    private final By errorToast = By.xpath("//*[contains(@class,'toast') or contains(@class,'alert') or contains(@class,'notification')][normalize-space()]");

    public LoginForm(WebDriver driver) { super(driver); }

    public boolean isVisible() {
        var list = driver.findElements(form);
        return !list.isEmpty() && list.get(0).isDisplayed();
    }

    public LoginForm typeUsername(String value) {
        WebElement el = driver.findElement(usernameInp);
        el.clear(); el.sendKeys(value);
        return this;
    }

    public LoginForm typePassword(String value) {
        WebElement el = driver.findElement(passwordInp);
        el.clear(); el.sendKeys(value);
        return this;
    }

    public void submit() {
        safeClick(submitBtn);
        wait.withTimeout(java.time.Duration.ofSeconds(5)).until(d -> {
            boolean hasErr = !d.findElements(errorInline).isEmpty() || !d.findElements(errorToast).isEmpty();
            return hasErr || !d.getCurrentUrl().toLowerCase().contains("login");
        });

        wait.withTimeout(java.time.Duration.ofSeconds(Integer.getInteger("explicit.wait.seconds", 10)));
    }

    public boolean hasError() {
        return driver.findElements(errorInline).stream().anyMatch(e -> e.isDisplayed() && !e.getText().trim().isEmpty())
                || driver.findElements(errorToast).stream().anyMatch(e -> e.isDisplayed() && !e.getText().trim().isEmpty());
    }

    public String errorText() {
        String txt = firstText(errorInline);
        if (!txt.isBlank()) return txt;
        return firstText(errorToast);
    }

    private String firstText(By by) {
        List<WebElement> els = driver.findElements(by);
        return els.stream()
                .filter(e -> e.isDisplayed() && !e.getText().trim().isEmpty())
                .map(e -> e.getText().trim())
                .findFirst().orElse("");
    }

    public boolean submitAndWaitForError(int seconds) {
        safeClick(submitBtn);
        try {
            wait.withTimeout(Duration.ofSeconds(seconds)).until(d -> {
                boolean errVisible = hasError();
                boolean leftLoginUrl = !d.getCurrentUrl().toLowerCase().contains("login");
                return errVisible || leftLoginUrl;
            });
        } catch (Exception ignored) {
        } finally {
            wait.withTimeout(Duration.ofSeconds(Integer.getInteger("explicit.wait.seconds", 10)));
        }
        return hasError();
    }

    public String getErrorText() {
        return errorText();
    }
}
