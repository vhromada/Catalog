package cz.vhromada.catalog.gui.commons;

/**
 * An enumeration represents type of button for showing web page.
 *
 * @author Vladimir Hromada
 */
public enum WebPageButtonType {

    /**
     * Button for czech Wikipedia
     */
    WIKI_CZ("http://cs.wikipedia.org/wiki/"),

    /**
     * Button for english Wikipedia
     */
    WIKI_EN("http://en.wikipedia.org/wiki/"),

    /**
     * Button for ČSFD
     */
    CSFD("http://www.csfd.cz/film/"),

    /**
     * Button for IMDB
     */
    IMDB("http://www.imdb.com/title/tt");

    /**
     * Base url
     */
    private final String baseUrl;

    /**
     * Creates a new instance of WebPageButtonType.
     *
     * @param baseUrl base URL
     */
    WebPageButtonType(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns base URL.
     *
     * @return base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

}
