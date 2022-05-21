package ch.heigvd.app.utils.parsers;

/**
 * Mimics the config.json structure in order map its data
 * into a java object.
 */
public class SiteConfig {
    private final String title;
    private final String lang;
    private final String charset;

    /**
     * Constructor for test purpose
     *
     * @param title   a title
     * @param lang    a language (ex: fr)
     * @param charset a charset (ex: utf-8)
     */
    public SiteConfig(String title, String lang, String charset) {
        this.title = title;
        this.lang = lang;
        this.charset = charset;
    }

    /**
     * Get config title
     * @return Config title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get config lang
     * @return Config lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * Get config charset
     * @return Config charset
     */
    public String getCharset() {
        return charset;
    }
}