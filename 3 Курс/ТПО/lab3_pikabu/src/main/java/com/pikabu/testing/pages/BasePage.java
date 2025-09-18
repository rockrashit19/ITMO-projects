package com.pikabu.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;
import java.util.function.Function;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Integer.getInteger("explicit.wait.seconds", 10)
        ));
    }

    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected void safeClick(By locator) {
        try {
            WebElement el = waitForElementClickable(locator);
            try { el.click(); } catch (Exception ex) { jsClick(el); }
        } catch (Exception ignored) {}
    }

    protected void safeClick(WebElement el) {
        try { el.click(); } catch (Exception ex) { jsClick(el); }
    }

    protected void clickAndMaybeSwitch(WebElement link) {
        Set<String> before = driver.getWindowHandles();
        try {
            link.click();
        } catch (WebDriverException e) {
            jsClick(link);
        }
        wait.withTimeout(Duration.ofSeconds(3))
                .until(d -> d.getWindowHandles().size() >= before.size());
        if (driver.getWindowHandles().size() > before.size()) {
            for (String h : driver.getWindowHandles()) {
                if (!before.contains(h)) {
                    driver.switchTo().window(h);
                    break;
                }
            }
        }
    }

    protected <T> T until(Function<? super WebDriver, T> condition) {
        return wait.until(condition::apply);
    }

    protected void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }
}
