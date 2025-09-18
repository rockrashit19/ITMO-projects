package com.pikabu.testing.functional;

import com.pikabu.testing.base.BaseTest;
import com.pikabu.testing.base.DriverFactory;
import com.pikabu.testing.pages.MainPage;
import com.pikabu.testing.pages.PostPage;
import com.pikabu.testing.pages.SearchResultsPage;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

public class SearchFunctionalityTest extends BaseTest {
    private MainPage mainPage;

    @BeforeMethod
    public void setUp() {
        mainPage = new MainPage(driver);
        mainPage.open();
    }

    @DataProvider
    public Object[][] searchTermsData() {
        return new Object[][] {
                {"java", "должны найтись результаты по Java"},
                {"программирование", "должны найтись результаты по программированию"},
                {"юмор", "должны найтись юмористические посты"},
                {"игры", "должны найтись посты об играх"},
                {"новости", "должны найтись новостные посты"}
        };
    }

    @Test(priority = 1, dataProvider = "searchTermsData",
            description = "Проверка поиска с валидными запросами")
    public void testValidSearchQueries(String searchTerm, String description) {
        SearchResultsPage searchResults = mainPage.search(searchTerm);
        assertThat(searchResults.isPageLoaded()).isTrue();
        assertThat(searchResults.getSearchQuery().toLowerCase())
                .contains(searchTerm.toLowerCase());

        if (searchResults.hasResults()) {
            var titles = searchResults.getSearchResultTitles();
            assertThat(titles).isNotEmpty();
        }
    }

    @Test(priority = 3, description = "Открытие результата поиска (если есть)")
    public void testOpenSearchResult() {
        SearchResultsPage sr = mainPage.search("интересное");
        assertThat(sr.isPageLoaded()).as("Страница результатов должна загрузиться").isTrue();

        var titles = sr.getSearchResultTitles();

        PostPage post = sr.openFirstResult();

        assertThat(post.isPageLoaded()).isTrue();
        String postTitle = post.getPostTitle();
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        System.out.println("Post title: '" + postTitle + "'");
        System.out.println("Page source contains 'story': " + driver.getPageSource().contains("story"));

        assertThat(postTitle)
                .as("Заголовок поста должен быть непустым. URL: %s", currentUrl)
                .isNotEmpty();
    }


}
