package com.pikabu.testing.domain;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

public class SearchQueryTest {

    @Test
    public void testNormalization() {
        SearchQuery q = new SearchQuery("  java   selenium  ");
        assertThat(q.normalized()).isEqualTo("java selenium");
    }

    @Test
    public void testValidity() {
        assertThat(new SearchQuery("test").isValid()).isTrue();
        assertThat(new SearchQuery("   ").isValid()).isFalse();
        assertThat(new SearchQuery(null).isValid()).isFalse();
    }

    @Test
    public void testToStringIsNormalized() {
        SearchQuery q = new SearchQuery("  a  b ");
        assertThat(q.toString()).isEqualTo("a b");
    }
}
