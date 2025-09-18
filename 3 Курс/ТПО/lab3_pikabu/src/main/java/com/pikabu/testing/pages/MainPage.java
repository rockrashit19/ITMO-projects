package com.pikabu.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class MainPage extends BasePage {

    private final By feedContainer = By.xpath("//main | //div[contains(@class,'stories') or contains(@class,'feed')]");
    private final By postItems = By.xpath("//article[contains(@class,'story') or contains(@class,'post')]");
    private final By postTitleLinkInItem = By.xpath(".//a[normalize-space() and (contains(@class,'story__title-link') or contains(@class,'story__title') or ancestor::h1 or ancestor::h2)]");

    private final By searchInput = By.xpath(
            "//input[" +
                    "contains(@type,'search') or " +
                    "contains(@name,'q') or contains(@name,'search') or " +
                    "contains(translate(@placeholder,'ПОИСКSEARCH','поискsearch'),'поиск') or " +
                    "contains(translate(@placeholder,'ПОИСКSEARCH','поискsearch'),'search')" +
                    "]"
    );

    private final By searchToggle = By.xpath(
            "(" +
                    "//button[" +
                    "contains(@class,'search') or contains(@class,'icon') or " +
                    "contains(@aria-label,'Поиск') or contains(@aria-label,'Search') or " +
                    ".//svg" +
                    "] | //a[contains(@href,'search') and not(contains(@href,'?q='))]" +
                    ")[1]"
    );

    private final By searchSubmit = By.xpath(
            "(" +
                    "//form[.//input=(" + rawXpath(searchInput) + ")]//button[@type='submit' or contains(@class,'search') or .//svg]" +
                    ")[1] | " +
                    "(" +
                    "//button[@type='submit' and (contains(@class,'search') or contains(@aria-label,'Поиск') or contains(@aria-label,'Search'))]" +
                    ")[1]"
    );

    private final By cookieAcceptBtn = By.xpath(
            "//button[" +
                    "contains(translate(normalize-space(.),'ЁЙОПРСТУФХЦЧШЩЪЫЬЭЮЯA-Z','ёёйопрстуфхцчшщъыьэюяa-z'), 'принять') or " +
                    "contains(translate(normalize-space(.),'ПОЯНТ','поянт'),'понят') or " +
                    "contains(translate(normalize-space(.),'СОГЛАС','соглас'),'соглас')" +
                    "]"
    );

    private final String baseUrl = System.getProperty("base.url", "https://pikabu.ru");

    public MainPage(WebDriver driver) { super(driver); }

    public MainPage open() {
        driver.get(baseUrl);
        waitForElementVisible(feedContainer);
        return this;
    }

    public int getPostsCount() {
        waitForElementVisible(feedContainer);
        return driver.findElements(postItems).size();
    }

    public PostPage openFirstPost() {
        waitForElementVisible(feedContainer);
        List<WebElement> items = driver.findElements(postItems);
        if (items.isEmpty()) return null;

        WebElement first = items.get(0);
        List<WebElement> links = first.findElements(postTitleLinkInItem);
        if (links.isEmpty()) return null;

        WebElement link = links.get(0);
        scrollIntoView(link);
        clickAndMaybeSwitch(link);

        PostPage page = new PostPage(driver);
        return page.isPageLoaded() ? page : null;
    }

    public SearchResultsPage search(String query) {
        WebElement inp = findSearchInputOrOpen();
        if (inp != null) {
            try {
                scrollIntoView(inp);
                inp.clear();
                inp.sendKeys(query);
                try {
                    inp.sendKeys(Keys.ENTER);
                } catch (Exception ignored) {
                    try {
                        WebElement submit = waitForElementClickable(searchSubmit);
                        submit.click();
                    } catch (Exception e) {
                        String base = System.getProperty("base.url", "https://pikabu.ru");
                        String url = base + "/search?q=" + java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8);
                        driver.navigate().to(url);
                    }
                }
            } catch (Exception e) {
                String base = System.getProperty("base.url", "https://pikabu.ru");
                String url = base + "/search?q=" + java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8);
                driver.navigate().to(url);
            }
        } else {
            String base = System.getProperty("base.url", "https://pikabu.ru");
            String url = base + "/search?q=" + java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8);
            driver.navigate().to(url);
        }

        SearchResultsPage results = new SearchResultsPage(driver);
        results.waitLoaded();
        return results;
    }



    private WebElement findSearchInputOrOpen() {
        List<WebElement> inputs = driver.findElements(searchInput);
        for (WebElement e : inputs) {
            if (isDisplayed(e)) return e;
        }

        List<WebElement> toggles = driver.findElements(searchToggle);
        if (!toggles.isEmpty() && toggles.get(0).isDisplayed()) {
            safeClick(toggles.get(0));
        }

        try {
            var shortWait = wait.withTimeout(Duration.ofSeconds(3));
            shortWait.until(d -> d.findElements(searchInput).stream().anyMatch(this::isDisplayed));
            return driver.findElements(searchInput).stream().filter(this::isDisplayed).findFirst().orElse(null);
        } catch (Exception ignored) {
            return null;
        } finally {
            wait.withTimeout(Duration.ofSeconds(10));
        }
    }

    private void dismissOverlays() {
        List<WebElement> buttons = driver.findElements(cookieAcceptBtn);
        if (!buttons.isEmpty()) {
            try { safeClick(buttons.get(0)); } catch (Exception ignored) {}
        }
    }

    private boolean isDisplayed(WebElement el) {
        try { return el.isDisplayed(); } catch (Exception e) { return false; }
    }

    protected static String rawXpath(By by) {
        String s = by.toString();
        return s.startsWith("By.xpath: ") ? s.substring("By.xpath: ".length()) : s;
    }
}
