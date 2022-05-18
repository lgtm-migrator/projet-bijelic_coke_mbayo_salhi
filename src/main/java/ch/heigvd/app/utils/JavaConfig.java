package ch.heigvd.app.utils;

/**
 * Mimics the config.json structure in order map its data
 * into a java object.
 */
public class JavaConfig {
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
    public JavaConfig(String title, String lang, String charset) {
        this.title = title;
        this.lang = lang;
        this.charset = charset;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return
     */
    public String getLang() {
        return lang;
    }

    /**
     * @return
     */
    public String getCharset() {
        return charset;
    }
}
