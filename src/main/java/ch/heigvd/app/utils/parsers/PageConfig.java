package ch.heigvd.app.utils.parsers;

public class PageConfig {

    private final String title;
    private final String author;
    private final String date;

    /**
     * Constructor for test purpose
     *
     * @param title  a title
     * @param author name
     * @param date   the date
     */
    public PageConfig(String title, String author, String date) {
        this.title = title;
        this.author = author;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getDate(){
        return date;
    }
}
