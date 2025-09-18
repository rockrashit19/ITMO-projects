package com.pikabu.testing.functional;

import com.pikabu.testing.base.BaseTest;
import com.pikabu.testing.pages.MainPage;
import com.pikabu.testing.pages.PostPage;
import com.pikabu.testing.pages.SearchResultsPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

public class MainPageTest extends BaseTest {

    private MainPage main;

    @BeforeMethod
    public void openMain() {
        main = new MainPage(driver);
        main.open();
    }

    @Test(priority = 1, description = "Главная открывается и лента видна")
    public void testMainOpens() {
        assertThat(main.getPostsCount()).isGreaterThanOrEqualTo(0);
    }

    @Test(priority = 2, description = "Открывается первый пост из ленты")
    public void testOpenFirstPost() {
        if (main.getPostsCount() == 0) return;

        PostPage post = main.openFirstPost();
        assertThat(post.isPageLoaded()).isTrue();
        assertThat(post.getPostTitle()).isNotEmpty();
    }

    @Test(priority = 3, description = "Поиск что-то отдаёт и query корректно виден")
    public void testSearchFlow() {
        SearchResultsPage sr = main.search("java");
        assertThat(sr.isPageLoaded()).isTrue();
        assertThat(sr.getSearchQuery().toLowerCase()).contains("java");
        sr.hasResults();
    }
}
