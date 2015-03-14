package cz.vhromada.catalog.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.gui.game.GamesPanel;
import cz.vhromada.catalog.gui.genre.GenresPanel;
import cz.vhromada.catalog.gui.movie.MoviesPanel;
import cz.vhromada.catalog.gui.music.MusicPanel;
import cz.vhromada.catalog.gui.program.ProgramsPanel;
import cz.vhromada.catalog.gui.serie.SeriesPanel;
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

    /** Horizontal component size */
    private static final int HORIZONTAL_COMPONENT_SIZE = 400;

    /** Vertical component size */
    private static final int VERTICAL_COMPONENT_SIZE = 400;

    /** Application context */
    private ConfigurableApplicationContext context;

    /** Menu bar */
    private final JMenuBar menuBar = new JMenuBar();

    /** Menu file */
    private final JMenu fileMenu = new JMenu("File");

    /** Menu item new */
    private final JMenuItem newMenuItem = new JMenuItem("New", Picture.NEW.getIcon());

    /** Menu item save */
    private final JMenuItem saveMenuItem = new JMenuItem("Save", Picture.SAVE.getIcon());

    /** Menu item selector */
    private final JMenuItem selectorMenuItem = new JMenuItem("Selector", Picture.CATALOG.getIcon());

    /** Menu item exit */
    private final JMenuItem exitMenuItem = new JMenuItem("Exit", Picture.EXIT.getIcon());

    /** Menu help */
    private final JMenu helpMenu = new JMenu("Help");

    /** Menu item about */
    private final JMenuItem aboutMenuItem = new JMenuItem("About", Picture.ABOUT.getIcon());

    /** Tabbed pane */
    private final JTabbedPane tabbedPane = new JTabbedPane();

    /** Panel for movies */
    private MoviesPanel moviesPanel;

    /** Panel for series */
    private SeriesPanel seriesPanel;

    /** Panel for games */
    private GamesPanel gamesPanel;

    /** Panel for music */
    private MusicPanel musicPanel;

    /** Panel for programs */
    private ProgramsPanel programsPanel;

    /** Panel for genres */
    private GenresPanel genresPanel;

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

    /** Facade for genres */
    private GenreFacade genreFacade;

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
        setIconImage(Picture.CATALOG.getIcon().getImage());

        this.context = context;
        movieFacade = context.getBean(MovieFacade.class);
        serieFacade = context.getBean(SerieFacade.class);
        gameFacade = context.getBean(GameFacade.class);
        musicFacade = context.getBean(MusicFacade.class);
        programFacade = context.getBean(ProgramFacade.class);
        bookCategoryFacade = context.getBean(BookCategoryFacade.class);
        genreFacade = context.getBean(GenreFacade.class);

        initMenuBar();
        initFileMenu();

        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        aboutMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                aboutAction();
            }

        });

        moviesPanel = new MoviesPanel(movieFacade, genreFacade);
        seriesPanel = new SeriesPanel(serieFacade, context.getBean(SeasonFacade.class), context.getBean(EpisodeFacade.class), genreFacade);
        gamesPanel = new GamesPanel(gameFacade);
        musicPanel = new MusicPanel(musicFacade, context.getBean(SongFacade.class));
        programsPanel = new ProgramsPanel(programFacade);
        genresPanel = new GenresPanel(genreFacade);

        initTabbedPane();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                closing();
            }

        });

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(tabbedPane, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE,
                Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(tabbedPane, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, Short.MAX_VALUE));

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
    private static void initMenu(final JMenu menu, final JMenuItem... menuItems) {
        for (final JMenuItem menuItem : menuItems) {
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

    /** Initializes tabbed pane. */
    private void initTabbedPane() {
        tabbedPane.addTab("Movies", moviesPanel);
        tabbedPane.addTab("Series", seriesPanel);
        tabbedPane.addTab("Games", gamesPanel);
        tabbedPane.addTab("Music", musicPanel);
        tabbedPane.addTab("Programs", programsPanel);
        tabbedPane.addTab("Genres", genresPanel);
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                moviesPanel.clearSelection();
                seriesPanel.clearSelection();
                gamesPanel.clearSelection();
                musicPanel.clearSelection();
                programsPanel.clearSelection();
                genresPanel.clearSelection();
            }

        });
    }

    /** Performs action for button New. */
    private void newAction() {
        moviesPanel.newData();
        seriesPanel.newData();
        gamesPanel.newData();
        musicPanel.newData();
        programsPanel.newData();
        genresPanel.newData();
    }

    /** Performs action for button Save. */
    private void saveAction() {
        save();
    }

    /** Performs action for button Selector. */
    private void selectorAction() {
        closing();
        SwingUtilities.invokeLater(new Runnable() {

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
    private static void aboutAction() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new AboutDialog().setVisible(true);
            }

        });
    }

    /** Closes form. */
    private void closing() {
        final boolean saved = moviesPanel.isSaved() && seriesPanel.isSaved() && gamesPanel.isSaved() && musicPanel.isSaved() && programsPanel.isSaved()
                && genresPanel.isSaved();
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
        moviesPanel.save();
        seriesPanel.save();
        gamesPanel.save();
        musicPanel.save();
        programsPanel.save();
        genresPanel.save();
    }

}
