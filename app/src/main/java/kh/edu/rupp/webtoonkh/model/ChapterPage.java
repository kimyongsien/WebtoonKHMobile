package kh.edu.rupp.webtoonkh.model;

public class ChapterPage {
    private int id;
    private int chapter_id;
    private int page_order;
    private String image_url;

    public int getId() {
        return id;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public int getPage_order() {
        return page_order;
    }

    public String getImage_url() {
        return image_url;
    }
}