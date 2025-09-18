package com.pikabu.testing.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.time.Duration;

public abstract class BaseTest {
    protected WebDriver driver;

    @Parameters({"browser","headless"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional String browserParam,
            @Optional("false") String headlessParam
    ) {
        String sysBrowser = System.getProperty("browser");
        BrowserType type = BrowserType.from(browserParam != null ? browserParam : sysBrowser);
        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", headlessParam != null ? headlessParam : "false")
        );

        driver = DriverFactory.create(type, headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(300));
        try { driver.manage().window().maximize(); } catch (Exception ignored) {}
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
