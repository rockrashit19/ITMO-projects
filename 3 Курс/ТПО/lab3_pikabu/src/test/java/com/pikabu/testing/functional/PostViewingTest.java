package com.pikabu.testing.functional;

import com.pikabu.testing.base.BaseTest;
import com.pikabu.testing.pages.MainPage;
import com.pikabu.testing.pages.PostPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

public class PostViewingTest extends BaseTest {
    private MainPage mainPage;
    private PostPage postPage;

    @BeforeMethod
    public void setUpTest() {
        mainPage = new MainPage(driver);
        mainPage.open();

        if (mainPage.getPostsCount() > 0) {
            postPage = mainPage.openFirstPost();
        }
    }

    @Test(priority = 1, description = "Комментарии: чтение/форма")
    public void testComments() {
        if (postPage != null) {
            int cnt = postPage.getCommentsCount();
            if (cnt > 0) {
                var texts = postPage.getCommentTexts();
                assertThat(texts.size()).isGreaterThan(0);
            }
            postPage.isCommentFormVisible();
        }
    }

    @Test(priority = 5, description = "Проверка загрузки страницы поста")
    public void testPostPageLoads() {
        if (postPage != null) {
            assertThat(postPage.isPageLoaded()).isTrue();
            assertThat(postPage.getPostTitle()).isNotEmpty();
        }
    }

    @Test(priority = 2, description = "Проверка элементов поста")
    public void testPostElements() {
        if (postPage != null) {
            assertThat(postPage.isContentVisible()).isTrue();
            var author = postPage.getPostAuthor();
            if (!author.isEmpty()) assertThat(author.length()).isGreaterThan(1);
        }
    }

    @Test(priority = 3, description = "Проверка рейтинговой системы")
    public void testPostRating() {
        if (postPage != null) {
            boolean hasUpvote = postPage.isUpvoteButtonVisible();
            boolean hasDownvote = postPage.isDownvoteButtonVisible();

            if (hasUpvote || hasDownvote) {
                int ratingNet = postPage.getPostRatingNet();
                assertThat(ratingNet).isNotNull();
            }
        }
    }

    @Test(priority = 4, description = "Проверка попытки голосования без ожидания модалок")
    public void testVotingAttempt() {
        if (postPage != null && postPage.isUpvoteButtonVisible()) {
            postPage.clickUpvote();
            assertThat(postPage.isPageLoaded()).isTrue();
        }
    }
}
