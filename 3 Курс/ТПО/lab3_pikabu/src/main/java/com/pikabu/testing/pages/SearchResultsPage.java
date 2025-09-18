package com.pikabu.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends BasePage {

    private final By root = By.xpath(
            "//main//*[contains(@class,'search') or contains(@class,'results') or contains(@class,'search-results')]" +
                    " | //section[contains(@class,'search') or contains(@class,'results')]" +
                    " | //main//article[contains(@class,'story') or contains(@class,'post')]" +
                    " | //main"
    );

    private final By searchInput = By.xpath(
            "//input[" +
                    "contains(@type,'search') or " +
                    "contains(@name,'q') or contains(@name,'search') or " +
                    "contains(translate(@placeholder,'ПОИСКSEARCH','поискsearch'),'поиск') or " +
                    "contains(translate(@placeholder,'ПОИСКSEARCH','поискsearch'),'search')" +
                    "]"
    );

    private final By resultItems = By.xpath(
            "//article[contains(@class,'story') or contains(@class,'post')]" +
                    " | //div[contains(@class,'search-result') or contains(@class,'result-item')]"
    );

    private final By resultTitleLink = By.xpath(
            ".//a[(contains(@class,'story__title-link') or contains(@class,'title')) and normalize-space()]" +
                    " | .//h1//a | .//h2//a | .//a[normalize-space() and (ancestor::h1 or ancestor::h2)]"
    );

    private final By noResults = By.xpath(
            "//*[contains(@class,'empty') or contains(@class,'not-found') or " +
                    " contains(translate(normalize-space(.),'NO RESULTS','no results'),'no results') or " +
                    " contains(translate(normalize-space(.),'НИЧЕГО','ничего'),'ничего')]"
    );


    public SearchResultsPage(WebDriver driver) { super(driver); }

    public void waitLoaded() {
        try {
            wait.until(d -> {
                String u = d.getCurrentUrl().toLowerCase();
                return u.contains("search") || u.contains("?q=") || u.contains("&q=");
            });
        } catch (Exception ignored) {}

        wait.withTimeout(java.time.Duration.ofSeconds(10)).until(d ->
                !d.findElements(resultItems).isEmpty()
                        || d.findElements(noResults).stream().anyMatch(org.openqa.selenium.WebElement::isDisplayed)
                        || !d.findElements(By.xpath(
                        "//a[normalize-space() and (" +
                                " contains(@class,'story__title-link') or contains(@class,'title') or " +
                                " contains(@href,'/story/') or contains(@href,'/link/')" +
                                ")]"
                )).isEmpty()
        );
    }


    public boolean isPageLoaded() {
        try { waitLoaded(); return true; } catch (Exception e) { return false; }
    }

    public String getSearchQuery() {
        List<WebElement> inputs = driver.findElements(searchInput);
        if (!inputs.isEmpty()) {
            String val = inputs.get(0).getAttribute("value");
            if (val != null && !val.trim().isEmpty()) return val.trim();
        }
        try {
            String url = driver.getCurrentUrl();
            int i = url.indexOf('?'); if (i < 0) i = url.indexOf('&');
            if (i >= 0) {
                String qs = url.substring(i + 1);
                for (String p : qs.split("&")) {
                    String[] kv = p.split("=", 2);
                    if (kv.length == 2 && (kv[0].equalsIgnoreCase("q") || kv[0].contains("search"))) {
                        return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public boolean hasResults() {
        try {
            waitLoaded();
        } catch (Exception ignored) { }
        return !driver.findElements(resultItems).isEmpty();
    }

    public java.util.List<String> getSearchResultTitles() {
        waitLoaded();

        java.util.List<String> titles = new java.util.ArrayList<>();

        for (org.openqa.selenium.WebElement item : driver.findElements(resultItems)) {
            java.util.List<org.openqa.selenium.WebElement> links = item.findElements(resultTitleLink);
            if (!links.isEmpty() && links.get(0).isDisplayed()) {
                String t = links.get(0).getText().trim();
                if (!t.isEmpty()) titles.add(t);
            }
        }

        if (titles.isEmpty()) {
            java.util.List<org.openqa.selenium.WebElement> links = driver.findElements(By.xpath(
                    "//a[normalize-space() and (" +
                            " contains(@class,'story__title-link') or contains(@class,'title') or " +
                            " contains(@href,'/story/') or contains(@href,'/link/')" +
                            ")] | //h1//a[normalize-space()] | //h2//a[normalize-space()]"
            ));
            for (org.openqa.selenium.WebElement a : links) {
                if (a.isDisplayed()) {
                    String t = a.getText().trim();
                    if (!t.isEmpty()) titles.add(t);
                }
            }
        }
        return titles;
    }


    public PostPage openFirstResult() {
        waitLoaded();

        org.openqa.selenium.WebElement link = null;

        for (org.openqa.selenium.WebElement item : driver.findElements(resultItems)) {
            java.util.List<org.openqa.selenium.WebElement> links = item.findElements(resultTitleLink);
            if (!links.isEmpty() && links.get(0).isDisplayed()) { link = links.get(0); break; }
        }
        if (link == null) {
            java.util.List<org.openqa.selenium.WebElement> links = driver.findElements(By.xpath(
                    "//a[normalize-space() and (" +
                            " contains(@class,'story__title-link') or contains(@class,'title') or " +
                            " contains(@href,'/story/') or contains(@href,'/link/')" +
                            ")] | //h1//a[normalize-space()] | //h2//a[normalize-space()]"
            ));
            if (!links.isEmpty()) link = links.get(0);
        }
        if (link == null) return null;

        scrollIntoView(link);
        clickAndMaybeSwitch(link);

        PostPage page = new PostPage(driver);
        try {
            wait.withTimeout(java.time.Duration.ofSeconds(8)).until(d -> page.isPageLoaded());
        } catch (Exception ignored) {}
        return page;
    }


}
