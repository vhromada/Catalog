package cz.vhromada.catalog.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.vhromada.catalog.commons.SwingConstants;

/**
 * A class represents main screen for selecting options.
 *
 * @author Vladimir Hromada
 */
public class Selector extends JFrame {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Horizontal button size */
	private static final int HORIZONTAL_BUTTON_SIZE = 130;

	/** Horizontal gap size */
	private static final int HORIZONTAL_GAP_SIZE = 60;

	/** Vertical gap size */
	private static final int VERTICAL_GAP_SIZE = 40;

	/** Button Catalog */
	private JButton catalogButton = new JButton("Catalog");

	/** Button Exit */
	private JButton exitButton = new JButton("Exit");

	/** Creates a new instance of Selector. */
	public Selector() {
		setTitle("Catalog - Selector");
		setIconImage(Pictures.getPicture("catalog").getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		catalogButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				catalogAction();
			}

		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}

		});

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(createHorizontalLayout(layout));
		layout.setVerticalGroup(createVerticalLayout(layout));

		catalogButton.requestFocusInWindow();
		pack();
		setLocationRelativeTo(getRootPane());
	}

	/** Performs action for button Catalog. */
	private void catalogAction() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new LoadingDialog().setVisible(true);
			}

		});
		setVisible(false);
		dispose();
	}

	/**
	 * Returns horizontal layout of components.
	 *
	 * @param layout layout
	 * @return horizontal layout of components
	 */
	private GroupLayout.SequentialGroup createHorizontalLayout(final GroupLayout layout) {
		final GroupLayout.ParallelGroup buttons = layout.createParallelGroup()
				.addGap(SwingConstants.HORIZONTAL_BUTTON_GAP_SIZE)
				.addComponent(catalogButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, Short.MAX_VALUE)
				.addComponent(exitButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, Short.MAX_VALUE);

		return layout.createSequentialGroup()
				.addGap(HORIZONTAL_GAP_SIZE)
				.addGroup(buttons)
				.addGap(HORIZONTAL_GAP_SIZE);
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout layout
	 * @return vertical layout of components
	 */
	private GroupLayout.SequentialGroup createVerticalLayout(final GroupLayout layout) {
		return layout.createSequentialGroup()
				.addGap(VERTICAL_GAP_SIZE)
				.addComponent(catalogButton, SwingConstants.VERTICAL_BUTTON_SIZE, SwingConstants.VERTICAL_BUTTON_SIZE, Short.MAX_VALUE)
				.addGap(VERTICAL_GAP_SIZE)
				.addComponent(exitButton, SwingConstants.VERTICAL_BUTTON_SIZE, SwingConstants.VERTICAL_BUTTON_SIZE, Short.MAX_VALUE)
				.addGap(VERTICAL_GAP_SIZE);
	}

}
