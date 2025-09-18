package com.pikabu.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PostPage extends BasePage {

    private final By articleRoot = By.xpath("//article[contains(@class,'story') and @data-page='true' or contains(@class,'post')]");
    private final By title = By.xpath("//article[contains(@class,'story') or contains(@class,'post')]//h1 | //h1[contains(@class,'story__title')]");
    private final By titleText = By.xpath("//article[contains(@class,'story') or contains(@class,'post')]//h1//a | //a[contains(@class,'story__title-link')]");

    private final By content = By.xpath("//*[contains(@class,'story__content') or contains(@class,'story__content-inner') or contains(@class,'story__typography')]");
    private final By authorLink = By.xpath("//*[contains(@class,'story__user-link') and contains(@class,'user__nick')]");
    private final By ratingNet = By.xpath("//*[contains(@class,'story__rating-count')]");
    private final By upvoteBtn = By.xpath("//*[contains(@class,'story__rating-up')]");
    private final By downvoteBtn = By.xpath("//*[contains(@class,'story__rating-down')]");

    private final By commentsContainer = By.xpath("//section[contains(@class,'comments')]//div[contains(@class,'comments__container')]");
    private final By commentItems = By.xpath("//section[contains(@class,'comments')]//div[contains(@class,'comments__container')]//div[contains(@class,'comment') and @data-id]");
    private final By commentText = By.xpath(".//div[contains(@class,'comment__content')]//*[self::p or self::div or self::span][normalize-space()] | .//div[contains(@class,'comment__content') and normalize-space()]");

    private final By moreCommentsButton = By.xpath("//button[contains(@class,'comment__more')]");
    private final By commentsLink = By.xpath(
            "//a[contains(@href,'#comments') or contains(@href,'?cid=') or @data-tab='comments' or @data-test='comments-link']"
    );

    private final By commentForm = By.xpath("//section[@data-role='answer'] | //form[contains(@class,'comment')] | //form[.//textarea]");

    public PostPage(WebDriver driver) { super(driver); }

    public boolean isPageLoaded() {
        try {
            return until(d -> {
                try {
                    if (!getPostTitle().isBlank()) return true;
                } catch (Exception ignored) {}

                String url = d.getCurrentUrl().toLowerCase();
                if (!url.contains("search") && (url.contains("/story") || url.contains("/post"))) return true;

                try {
                    var root = d.findElements(articleRoot);
                    if (!root.isEmpty() && root.get(0).isDisplayed()) return true;
                } catch (Exception ignored) {}

                return false;
            });
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isContentVisible() {
        try {
            waitForElementVisible(content);
            return true;
        } catch (Exception e) { return false; }
    }

    public String getPostAuthor() {
        try {
            WebElement root = waitForElementVisible(articleRoot);
            String fromAttr = attrOrEmpty(root, "data-author-name");
            if (!fromAttr.isEmpty()) return fromAttr;
            WebElement link = waitForElementVisible(authorLink);
            return link.getText().trim();
        } catch (Exception e) { return ""; }
    }

    public String getPostTitle() {
        try {
            WebElement root = waitForElementVisible(articleRoot);

            List<By> titleSelectors = Arrays.asList(
                    titleText,
                    title,
                    By.xpath("//h1[normalize-space()]"),
                    By.xpath("//title"),
                    By.xpath("//meta[@property='og:title']/@content"),
                    By.xpath("//*[contains(@class,'title') and normalize-space()]"),
                    By.xpath("//article//h1 | //article//h2"),
                    By.xpath("//*[@data-test='story-title'] | //*[@data-test='post-title']")
            );

            for (By selector : titleSelectors) {
                try {
                    List<WebElement> elements = driver.findElements(selector);
                    for (WebElement el : elements) {
                        if (el.isDisplayed()) {
                            String text = el.getText().trim();
                            if (!text.isEmpty()) {
                                System.out.println("Found title with selector: " + selector + " -> '" + text + "'");
                                return text;
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }

            try {
                String titleFromMeta = ((JavascriptExecutor) driver)
                        .executeScript("return document.querySelector('meta[property=\"og:title\"]')?.content || document.title || '';")
                        .toString().trim();
                if (!titleFromMeta.isEmpty()) {
                    System.out.println("Found title from meta/document.title: '" + titleFromMeta + "'");
                    return titleFromMeta;
                }
            } catch (Exception ignored) {}

            System.out.println("No title found with any selector");
            return "";

        } catch (Exception e) {
            System.out.println("Exception in getPostTitle(): " + e.getMessage());
            return "";
        }
    }


    public int getPostRatingNet() {
        try {
            String text = waitForElementVisible(ratingNet).getText();
            return parseCompactNumber(text);
        } catch (Exception e) {
            try {
                WebElement root = waitForElementVisible(articleRoot);
                String dataRating = attrOrEmpty(root, "data-rating");
                if (!dataRating.isEmpty()) return parseIntSafe(dataRating);
            } catch (Exception ignored) {}
            return 0;
        }
    }

    public boolean isUpvoteButtonVisible() { return isVisible(upvoteBtn); }
    public boolean isDownvoteButtonVisible() { return isVisible(downvoteBtn); }
    public PostPage clickUpvote() { safeClick(upvoteBtn); return this; }

    public void openComments() {
        List<WebElement> links = driver.findElements(commentsLink);
        if (!links.isEmpty()) {
            WebElement el = links.get(0);
            scrollIntoView(el);
            clickAndMaybeSwitch(el);
        }

        waitForElementVisible(commentsContainer);

        List<WebElement> moreBtns = driver.findElements(moreCommentsButton);
        if (!moreBtns.isEmpty() && moreBtns.get(0).isDisplayed()) {
            WebElement btn = moreBtns.get(0);
            scrollIntoView(btn);

            int before = driver.findElements(commentItems).size();
            try {
                waitForElementClickable(moreCommentsButton).click();
            } catch (Exception e) {
                jsClick(btn);
            }

            wait.withTimeout(Duration.ofSeconds(5)).until(d -> {
                int now = d.findElements(commentItems).size();
                boolean countGrew = now > before;
                boolean buttonGone = d.findElements(moreCommentsButton).isEmpty();
                boolean buttonHidden = !d.findElements(moreCommentsButton).isEmpty()
                        && !d.findElements(moreCommentsButton).get(0).isDisplayed();
                boolean buttonDisabled = !d.findElements(moreCommentsButton).isEmpty()
                        && "true".equalsIgnoreCase(d.findElements(moreCommentsButton).get(0).getAttribute("disabled"));
                return countGrew || buttonGone || buttonHidden || buttonDisabled;
            });
        }
    }

    public int getCommentsCount() {
        openComments();
        try {
            wait.withTimeout(Duration.ofSeconds(5))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.numberOfElementsToBeMoreThan(commentItems, 0),
                            ExpectedConditions.visibilityOfElementLocated(commentsContainer)
                    ));
        } catch (TimeoutException ignored) {}
        return driver.findElements(commentItems).size();
    }

    public List<String> getCommentTexts() {
        try {
            waitForElementVisible(commentsContainer);
            return driver.findElements(commentItems).stream()
                    .map(el -> {
                        List<WebElement> bodies = el.findElements(commentText);
                        return bodies.isEmpty() ? "" : bodies.get(0).getText().trim();
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public boolean isCommentFormVisible() {
        return isVisible(commentForm);
    }


    private boolean isVisible(By locator) {
        try {
            waitForElementVisible(locator);
            return true;
        } catch (Exception e) { return false; }
    }

    protected void safeClick(By locator) {
        try {
            WebElement el = waitForElementClickable(locator);
            try { el.click(); } catch (Exception ex) { jsClick(el); }
        } catch (Exception ignored) {}
    }

    private static String attrOrEmpty(WebElement el, String name) {
        try { String v = el.getAttribute(name); return v == null ? "" : v.trim(); }
        catch (Exception e) { return ""; }
    }

    private static int parseIntSafe(String s) {
        try { return new BigDecimal(s.trim().replace("\u00A0","").replace(" ","")).intValue(); }
        catch (Exception e) { return 0; }
    }

    private static int parseCompactNumber(String raw) {
        if (raw == null) return 0;
        String s = raw.trim().replace("\u00A0"," ").replace(" ","");
        try {
            if (s.endsWith("K") || s.endsWith("k")) return new BigDecimal(s.substring(0, s.length()-1)).multiply(BigDecimal.valueOf(1_000)).intValue();
            if (s.endsWith("M") || s.endsWith("m")) return new BigDecimal(s.substring(0, s.length()-1)).multiply(BigDecimal.valueOf(1_000_000)).intValue();
            return new BigDecimal(s.replaceAll("[^\\d.]", "")).intValue();
        } catch (Exception e) { return 0; }
    }

    public static class RatingBreakdown {
        public final int pluses;
        public final int minuses;
        public RatingBreakdown(int pluses, int minuses) { this.pluses = pluses; this.minuses = minuses; }
        @Override public String toString() { return "RatingBreakdown{+" + pluses + ", -" + minuses + '}'; }
    }
}
