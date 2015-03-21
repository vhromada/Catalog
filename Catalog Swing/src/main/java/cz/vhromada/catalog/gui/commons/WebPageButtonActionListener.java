package cz.vhromada.catalog.gui.commons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import cz.vhromada.validators.Validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class represents action listener for button fow showing web page.
 *
 * @author Vladimir Hromada
 */
public abstract class WebPageButtonActionListener implements ActionListener {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(WebPageButtonActionListener.class);

    /**
     * Type pf button
     */
    private WebPageButtonType buttonType;

    /**
     * Creates a new instance of WebPageButtonActionListener.
     *
     * @param buttonType type of button
     * @throws IllegalArgumentException if type of button is null
     */
    public WebPageButtonActionListener(final WebPageButtonType buttonType) {
        Validators.validateArgumentNotNull(buttonType, "Type of button");

        this.buttonType = buttonType;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String url = buttonType.getBaseUrl() + getDynamicUrlPart();
        try {
            Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
        } catch (final IOException ex) {
            logger.error("Error in showing page {}.", url, ex);
        }
    }

    /**
     * Returns dynamic part of URL.
     *
     * @return dynamic part of URL
     */
    protected abstract String getDynamicUrlPart();

}
