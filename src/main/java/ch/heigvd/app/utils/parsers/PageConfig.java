package ch.heigvd.app.utils.parsers;

/**
 * Config structure for pages
 */
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

    /**
     * Get page config title
     * @return Page config title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Get page config author
     * @return Page config author
     */
    public String getAuthor(){
        return author;
    }

    /**
     * Get page config date
     * @return Page config date
     */
    public String getDate(){
        return date;
    }
}
