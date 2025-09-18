package com.pikabu.testing.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver create(BrowserType type, boolean headless) {
        switch (type) {
            case FIREFOX: {
                FirefoxOptions fo = new FirefoxOptions();
                if (headless) fo.addArguments("-headless");
                return new FirefoxDriver(fo);
            }
            case CHROME:
            default: {
                ChromeOptions co = new ChromeOptions();
                if (headless) co.addArguments("--headless=new");
                return new ChromeDriver(co);
            }
        }
    }
}
