package cz.vhromada.catalog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import javax.swing.*;

import cz.vhromada.catalog.gui.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents utility class with main method.
 *
 * @author Vladimir Hromada
 */
public final class Main {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/** Creates a new instance of Main. */
	private Main() {
	}

	/**
	 * Main method.
	 *
	 * @param args the command line arguments
	 */
	public static void main(final String... args) {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());

		try {
			final File file = new File("Settings.properties");
			if (file.exists()) {
				final Properties properties = new Properties();
				try (Reader reader = new BufferedReader(new FileReader(file))) {
					properties.load(reader);
				}
				final String os = properties.getProperty("OS");
				if (os != null && "Windows".equals(os)) {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				}
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						new Selector().setVisible(true);
					}

				});
			} else {
				logger.error("There isn't settings file ({}).", file.getAbsolutePath());
				System.exit(1);
			}
		} catch (final IOException ex) {
			logger.error("Error in loading properties file.", ex);
		} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			logger.error("Error in setting look and feel.", ex);
		}
	}

	/** A class represents handler for uncaught exception. */
	private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(final Thread t, final Throwable e) {
			logger.error("Exception in Catalog application.", e);
		}

	}

}
