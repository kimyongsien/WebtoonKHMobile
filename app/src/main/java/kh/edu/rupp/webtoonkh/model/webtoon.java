package com.example.webtoonkh.model;

public class Webtoon {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String cover_url;
    private String description;
    private String created_at;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getCover_url() {
        return cover_url;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }
}