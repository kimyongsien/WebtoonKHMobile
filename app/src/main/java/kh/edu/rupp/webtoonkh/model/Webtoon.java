package kh.edu.rupp.webtoonkh.model;

public class Webtoon {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String cover_url;
    private String description;
    private String created_at;

    public Webtoon() {
    }

    public Webtoon(int id, String title, String author, String genre, String cover_url,
                   String description, String created_at) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.cover_url = cover_url;
        this.description = description;
        this.created_at = created_at;
    }

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
