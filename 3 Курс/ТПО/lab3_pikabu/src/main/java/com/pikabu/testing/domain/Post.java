package com.pikabu.testing.domain;

import java.util.Objects;

public class Post {
    private String id;
    private String title;
    private String author;
    private int    ratingNet;
    private int    commentsCount;

    public Post(String title, String author, int ratingNet, int commentsCount) {
        this.title = title;
        this.author = author;
        this.ratingNet = ratingNet;
        this.commentsCount = commentsCount;
    }

    public boolean isValid() {
        return title != null && !title.trim().isEmpty()
                && author != null && !author.trim().isEmpty()
                && ratingNet >= Integer.MIN_VALUE
                && commentsCount >= 0;
    }

    public String getId() { return id; }
    public Post setId(String id) { this.id = id; return this; }
    public String getTitle() { return title; }
    public Post setTitle(String title) { this.title = title; return this; }
    public String getAuthor() { return author; }
    public Post setAuthor(String author) { this.author = author; return this; }
    public int getRatingNet() { return ratingNet; }
    public Post setRatingNet(int ratingNet) { this.ratingNet = ratingNet; return this; }
    public int getCommentsCount() { return commentsCount; }
    public Post setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; return this; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(title, post.title) &&
                Objects.equals(author, post.author);
    }

    @Override public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override public String toString() {
        return "Post{title='" + title + "', author='" + author + "', ratingNet=" + ratingNet +
                ", comments=" + commentsCount + '}';
    }
}
