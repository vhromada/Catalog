package cz.vhromada.catalog.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import cz.vhromada.validators.Validators;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * A class represents catalog.
 *
 * @author Vladimir Hromada
 */
public class Catalog extends JFrame {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Application context */
	private ConfigurableApplicationContext context;

	/**
	 * Creates a new form Catalog.
	 *
	 * @param context application context
	 * @throws IllegalArgumentException if application context is null
	 */
	public Catalog(final ConfigurableApplicationContext context) {
		Validators.validateArgumentNotNull(context, "Application context");

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Catalog");
		setIconImage(Pictures.getPicture("catalog").getImage());

		this.context = context;

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				closing();
			}

		});

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		pack();
		setLocationRelativeTo(getRootPane());
		setExtendedState(MAXIMIZED_BOTH);
	}

	/** Closes form. */
	private void closing() {
		context.close();
	}

}
