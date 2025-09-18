package com.pikabu.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    private final By inlineFormRoot = By.xpath(
            "//form[@id='signin-form' and not(ancestor::*[contains(@class,'auth-modal') or contains(@class,'modal') or contains(@class,'popup') or @role='dialog'])]"
    );

    private final By authModal = By.xpath("//*[contains(@class,'auth-modal') or contains(@class,'modal') or @role='dialog']" +
            "[.//form[@id='signin-form']]");
    private final By modalCloseBtn = By.xpath(
            "//*[contains(@class,'auth-modal') or contains(@class,'modal') or @role='dialog']" +
                    "//button[@data-role='close' or contains(@class,'close') or @aria-label='Close' or @aria-label='Закрыть']"
    );

    private final LoginForm form;
    private final String baseUrl = System.getProperty("base.url", "https://pikabu.ru");

    public LoginPage(WebDriver driver) {
        super(driver);
        this.form = new LoginForm(driver);
    }

    public LoginPage open() {
        new MainPage(driver).open();

        var modals = driver.findElements(authModal);
        if (!modals.isEmpty()) {
            var closeBtns = driver.findElements(modalCloseBtn);
            if (!closeBtns.isEmpty() && closeBtns.get(0).isDisplayed()) {
                try { closeBtns.get(0).click(); } catch (Exception e) { jsClick(closeBtns.get(0)); }
            }
        }

        waitForElementVisible(inlineFormRoot);
        return this;
    }

    public LoginForm getForm() { return form; }

    public boolean isOpened() {
        try {
            waitForElementVisible(inlineFormRoot);
            return form.isVisible();
        } catch (Exception e) { return false; }
    }
}
