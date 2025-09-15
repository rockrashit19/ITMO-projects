package com.pikabu.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

//public class SearchResultsPage extends BasePage {
//
//    private final By searchResults = By.xpath("//div[contains(@class, 'search-result') or contains(@class, 'story')]//a[contains(@class, 'title')]");
//    private final By searchQuery = By.xpath("//input[contains(@class, 'search') or @name='q']");
//    private final By noResultsMessage = By.xpath("//div[contains(text(), 'Ничего не найдено') or contains(text(), 'результатов')]");
//    private final By searchFilters = By.xpath("//div[contains(@class, 'filter')]//a | //div[contains(@class, 'tab')]//a");
//    private final By resultCount = By.xpath("//span[contains(text(), 'найдено') or contains(text(), 'результат')]");
//    private final By loadMoreButton = By.xpath("//button[contains(text(), 'Показать ещё') or contains(@class, 'load-more')]");
//
//    public SearchResultsPage(WebDriver driver) {
//        super(driver);
//    }
//
//    public boolean isPageLoaded() {
//        try {
//            // Ждем либо результаты поиска, либо сообщение об их отсутствии
//            wait.until(driver ->
//                    !driver.findElements(searchResults).isEmpty() ||
//                            !driver.findElements(noResultsMessage).isEmpty()
//            );
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public List<String> getSearchResultTitles() {
//        List<WebElement> resultElements = driver.findElements(searchResults);
//        return resultElements.stream()
//                .map(WebElement::getText)
//                .filter(text -> !text.isEmpty())
//                .collect(Collectors.toList());
//    }
//
//    public int getResultsCount() {
//        return driver.findElements(searchResults).size();
//    }
//
//    public boolean hasResults() {
//        return !driver.findElements(searchResults).isEmpty();
//    }
//
//    public boolean isNoResultsMessageVisible() {
//        try {
//            waitForElementVisible(noResultsMessage);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getSearchQuery() {
//        try {
//            WebElement searchField = waitForElementVisible(searchQuery);
//            return searchField.getAttribute("value");
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//    public PostPage openFirstResult() {
//        List<WebElement> results = driver.findElements(searchResults);
//        if (!results.isEmpty()) {
//            results.get(0).click();
//            return new PostPage(driver);
//        }
//        throw new RuntimeException("No search results found");
//    }
//
//    public boolean areFiltersVisible() {
//        return !driver.findElements(searchFilters).isEmpty();
//    }
//
//    public SearchResultsPage clickFilter(int filterIndex) {
//        List<WebElement> filters = driver.findElements(searchFilters);
//        if (filterIndex < filters.size()) {
//            filters.get(filterIndex).click();
//        }
//        return this;
//    }
//
//    public boolean isLoadMoreButtonVisible() {
//        try {
//            waitForElementVisible(loadMoreButton);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}

public class SearchResultsPage extends BasePage {
    _RESULTS_COUNT = (By.CSS_SELECTOR, "span[class^='results-stories__count']")
    _FEED_CONTAINER = (By.CSS_SELECTOR, ".stories-feed__container")
    _STORY_CARDS = (By.CSS_SELECTOR, "article.story")
    _FIRST_STORY_LINK = (By.CSS_SELECTOR, "article.story h2.story__title a.story__title-link")

            # немного XPath по тексту — без альтернатив особо никак
    _SEARCH_BUTTON_X = (By.XPATH, "//button[.//span[normalize-space()='Поиск']]")
    _TAB_BTN_X_TMPL = "//button[@data-tab='{key}']"           # stories | authors | tags | communities
            _PERIOD_BTN_X_TMPL = ("//div[contains(@class,'fieldset-view__legend') and normalize-space()=' Период ']"
                          "/following::div[contains(@class,'filter-tabs-view__host')][1]"
                                  "/button[.//span[normalize-space()='{caption}']]")

    private static final By SEARCH_INPUT   = By.xpath("//input[@type='search' and @placeholder='Искать на Пикабу']");
    private static final By SEARCH_BUTTON  = By.xpath("//button[.//span[normalize-space()='Поиск']]");
    private static final By RESULTS_COUNT  = By.xpath("//span[contains(@class,'results-stories__count')]");
    private static final By FEED_CONTAINER = By.xpath("//div[contains(@class,'stories-feed__container')]");
    private static final By STORY_CARDS    = By.xpath("//article[contains(@class,'story')]");
    private static final By FIRST_STORY_LINK = By.xpath(
            "(//article[contains(@class,'story')]//h2[contains(@class,'story__title')]//a[contains(@class,'story__title-link')])[1]"
    );

    private static final String TAB_BTN_X_TMPL =
            "//button[@data-tab='%s']"; // stories | authors | tags | communities

    
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
}