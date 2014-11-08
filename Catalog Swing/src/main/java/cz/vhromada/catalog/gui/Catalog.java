package cz.vhromada.catalog.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.SerieFacade;
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

	/** Menu bar */
	private final JMenuBar menuBar = new JMenuBar();

	/** Menu file */
	private final JMenu fileMenu = new JMenu("File");

	/** Menu item new */
	private final JMenuItem newMenuItem = new JMenuItem("New", Pictures.getPicture("new"));

	/** Menu item save */
	private final JMenuItem saveMenuItem = new JMenuItem("Save", Pictures.getPicture("save"));

	/** Menu item selector */
	private final JMenuItem selectorMenuItem = new JMenuItem("Selector", Pictures.getPicture("catalog"));

	/** Menu item exit */
	private final JMenuItem exitMenuItem = new JMenuItem("Exit", Pictures.getPicture("exit"));

	/** Menu help */
	private final JMenu helpMenu = new JMenu("Help");

	/** Menu item about */
	private final JMenuItem aboutMenuItem = new JMenuItem("About", Pictures.getPicture("about"));

	/** Facade for movies */
	private MovieFacade movieFacade;

	/** Facade for series */
	private SerieFacade serieFacade;

	/** Facade for games */
	private GameFacade gameFacade;

	/** Facade for music */
	private MusicFacade musicFacade;

	/** Facade for programs */
	private ProgramFacade programFacade;

	/** Facade for book categories */
	private BookCategoryFacade bookCategoryFacade;

	/**
	 * Creates a new instance Catalog.
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
		movieFacade = context.getBean(MovieFacade.class);
		serieFacade = context.getBean(SerieFacade.class);
		gameFacade = context.getBean(GameFacade.class);
		musicFacade = context.getBean(MusicFacade.class);
		programFacade = context.getBean(ProgramFacade.class);
		bookCategoryFacade = context.getBean(BookCategoryFacade.class);

		initMenuBar();
		initFileMenu();

		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		aboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				aboutAction();
			}

		});

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

	/** Initializes menu bar. */
	private void initMenuBar() {
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		initMenu(fileMenu, newMenuItem, saveMenuItem, selectorMenuItem, exitMenuItem);
		initMenu(helpMenu, aboutMenuItem);

		setJMenuBar(menuBar);
	}

	/**
	 * Initializes menu.
	 *
	 * @param menu      menu
	 * @param menuItems menu items
	 */
	private void initMenu(final JMenu menu, final JMenuItem... menuItems) {
		for (JMenuItem menuItem : menuItems) {
			menu.add(menuItem);
		}
	}

	/** Initializes file menu. */
	private void initFileMenu() {
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				newAction();
			}

		});

		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				saveAction();
			}

		});

		selectorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		selectorMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				selectorAction();
			}

		});

		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				exitAction();
			}

		});
	}

	/** Performs action for button New. */
	private void newAction() {
	}

	/** Performs action for button Save. */
	private void saveAction() {
		save();
	}

	/** Performs action for button Selector. */
	private void selectorAction() {
		closing();
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Selector().setVisible(true);
			}

		});
		setVisible(false);
		dispose();
	}

	/** Performs action for button Exit. */
	private void exitAction() {
		closing();
		System.exit(0);
	}

	/** Performs action for button About. */
	private void aboutAction() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new AboutDialog().setVisible(true);
			}

		});
	}

	/** Closes form. */
	private void closing() {
		final boolean saved = true;
		if (!saved) {
			final int returnStatus = JOptionPane.showConfirmDialog(this, "Save data?", "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (returnStatus == JOptionPane.YES_OPTION) {
				save();
			}
		}
		context.close();
	}

	/** Saves data. */
	private void save() {
		movieFacade.updatePositions();
		serieFacade.updatePositions();
		gameFacade.updatePositions();
		musicFacade.updatePositions();
		programFacade.updatePositions();
		bookCategoryFacade.updatePositions();
	}

}
