package com.pikabu.testing.domain;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

public class PostTest {

    @Test
    public void testValidPost() {
        Post p = new Post("Заголовок", "Автор", 10, 5);
        assertThat(p.isValid()).isTrue();
        assertThat(p.getCommentsCount()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testInvalidPost_NoTitle() {
        Post p = new Post("", "Автор", 0, 0);
        assertThat(p.isValid()).isFalse();
    }

    @Test
    public void testInvalidPost_NoAuthor() {
        Post p = new Post("Заголовок", "   ", 0, 0);
        assertThat(p.isValid()).isFalse();
    }

    @Test
    public void testEqualityDependsOnTitleAndAuthor() {
        Post a = new Post("One", "User", 1, 0);
        Post b = new Post("One", "User", 100, 999);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
