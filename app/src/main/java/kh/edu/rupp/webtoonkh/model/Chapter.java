package kh.edu.rupp.webtoonkh.model;

public class Chapter {
    private int id;
    private int webtoon_id;
    private int chapter_number;
    private String title;

    public int getId() {
        return id;
    }

    public int getWebtoon_id() {
        return webtoon_id;
    }

    public int getChapter_number() {
        return chapter_number;
    }

    public String getTitle() {
        return title;
    }
}