package com.pikabu.testing.domain;

public class SearchQuery {
    private String raw;

    public SearchQuery(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public boolean isValid() {
        return raw != null && !raw.trim().isEmpty();
    }

    public String normalized() {
        return raw == null ? "" : raw.trim().replaceAll("\\s+", " ");
    }

    @Override public String toString() { return normalized(); }
}
