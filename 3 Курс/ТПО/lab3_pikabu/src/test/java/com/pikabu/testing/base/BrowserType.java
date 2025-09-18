package com.pikabu.testing.base;

public enum BrowserType {
    CHROME, FIREFOX;

    public static BrowserType from(String v) {
        if (v == null) return CHROME;
        switch (v.trim().toLowerCase()) {
            case "firefox": return FIREFOX;
            default:        return CHROME;
        }
    }
}
