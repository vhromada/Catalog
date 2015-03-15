package cz.vhromada.catalog.gui.movie;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.catalog.gui.commons.StatsTableCellRenderer;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with movies' data.
 *
 * @author Vladimir Hromada
 */
public class MoviesPanel extends JPanel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal scroll pane size
     */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 200;

    /**
     * Vertical data component size
     */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /**
     * Vertical size for scroll pane for table with stats
     */
    private static final int VERTICAL_STATS_SCROLL_PANE_SIZE = 45;

    /**
     * Popup menu
     */
    private JPopupMenu popupMenu = new JPopupMenu();

    /**
     * Menu item for adding movie
     */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Picture.ADD.getIcon());

    /**
     * Menu item for updating movie
     */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Picture.UPDATE.getIcon());

    /**
     * Menu item for removing movie
     */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Picture.REMOVE.getIcon());

    /**
     * Menu item for duplicating movie
     */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Picture.DUPLICATE.getIcon());

    /**
     * Menu item for moving up movie
     */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Picture.UP.getIcon());

    /**
     * Menu item for moving down movie
     */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Picture.DOWN.getIcon());

    /**
     * List with movies
     */
    private JList<String> list = new JList<>();

    /**
     * ScrollPane for list with movies
     */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /**
     * Tabbed pane with movie's data
     */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * Table with with movies' stats
     */
    private JTable statsTable = new JTable();

    /**
     * ScrollPane for table with movies' stats
     */
    private JScrollPane statsTableScrollPane = new JScrollPane(statsTable);

    /**
     * Facade for movies
     */
    private MovieFacade movieFacade;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * Data model for list with movies
     */
    private MoviesListDataModel moviesListDataModel;

    /**
     * Data model for table with stats for movies
     */
    private MoviesStatsTableDataModel moviesStatsTableDataModel;

    /**
     * True if data is saved
     */
    private boolean saved;

    /**
     * Creates a new instance of MoviesPanel.
     *
     * @param movieFacade facade for movies
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for movies is null
     *                                  or facade for genres is null
     */
    public MoviesPanel(final MovieFacade movieFacade, final GenreFacade genreFacade) {
        Validators.validateArgumentNotNull(movieFacade, "Facade for movies");
        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.movieFacade = movieFacade;
        this.genreFacade = genreFacade;
        this.saved = true;
        initComponents();
    }

    /**
     * Creates new data.
     */
    public void newData() {
        movieFacade.newData();
        moviesListDataModel.update();
        list.clearSelection();
        list.updateUI();
        moviesStatsTableDataModel.update();
        statsTable.updateUI();
        saved = true;
    }

    /**
     * Clears selection.
     */
    public void clearSelection() {
        list.clearSelection();
    }

    /**
     * Saves.
     */
    public void save() {
        saved = true;
    }

    /**
     * Returns true if data is saved.
     *
     * @return true if data is saved
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        initPopupMenu(addPopupMenuItem, updatePopupMenuItem, removePopupMenuItem, duplicatePopupMenuItem, moveUpPopupMenuItem, moveDownPopupMenuItem);

        addPopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
        addPopupMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                addAction();
            }

        });

        updatePopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        updatePopupMenuItem.setEnabled(false);
        updatePopupMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                updateAction();
            }

        });

        removePopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        removePopupMenuItem.setEnabled(false);
        removePopupMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                removeAction();
            }

        });

        duplicatePopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        duplicatePopupMenuItem.setEnabled(false);
        duplicatePopupMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                duplicateAction();
            }

        });

        moveUpPopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
        moveUpPopupMenuItem.setEnabled(false);
        moveUpPopupMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                moveUpAction();
            }

        });

        moveDownPopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        moveDownPopupMenuItem.setEnabled(false);
        moveDownPopupMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                moveDownAction();
            }

        });

        initList();

        moviesStatsTableDataModel = new MoviesStatsTableDataModel(movieFacade);
        statsTable.setModel(moviesStatsTableDataModel);
        statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        statsTable.setEnabled(false);
        statsTable.setRowSelectionAllowed(false);
        statsTable.setDefaultRenderer(Integer.class, new StatsTableCellRenderer());
        statsTable.setDefaultRenderer(String.class, new StatsTableCellRenderer());

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Initializes popup menu.
     *
     * @param menuItems popup menu items
     */
    private void initPopupMenu(final JMenuItem... menuItems) {
        for (JMenuItem menuItem : menuItems) {
            popupMenu.add(menuItem);
        }
    }

    /**
     * Performs action for button Add.
     */
    private void addAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final MovieInfoDialog dialog = new MovieInfoDialog(genreFacade);
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    movieFacade.add(dialog.getMovie());
                    moviesListDataModel.update();
                    list.updateUI();
                    list.setSelectedIndex(list.getModel().getSize() - 1);
                    moviesStatsTableDataModel.update();
                    statsTable.updateUI();
                    saved = false;
                }
            }

        });
    }

    /**
     * Performs action for button Update.
     */
    private void updateAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final int index = list.getSelectedIndex();
                final MovieInfoDialog dialog = new MovieInfoDialog(genreFacade, moviesListDataModel.getMovieAt(index));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final MovieTO movie = dialog.getMovie();
                    movieFacade.update(movie);
                    moviesListDataModel.update();
                    list.updateUI();
                    ((MovieDataPanel) tabbedPane.getComponentAt(0)).updateMovie(movie);
                    moviesStatsTableDataModel.update();
                    statsTable.updateUI();
                    saved = false;
                }
            }

        });
    }

    /**
     * Performs action for button Remove.
     */
    private void removeAction() {
        movieFacade.remove(moviesListDataModel.getMovieAt(list.getSelectedIndex()));
        moviesListDataModel.update();
        list.updateUI();
        list.clearSelection();
        moviesStatsTableDataModel.update();
        statsTable.updateUI();
        saved = false;
    }

    /**
     * Performs action for button Duplicate.
     */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        movieFacade.duplicate(moviesListDataModel.getMovieAt(index));
        moviesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        moviesStatsTableDataModel.update();
        statsTable.updateUI();
        saved = false;
    }

    /**
     * Performs action for button MoveUp.
     */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        movieFacade.moveUp(moviesListDataModel.getMovieAt(index));
        moviesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        saved = false;
    }

    /**
     * Performs action for button MoveDown.
     */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        movieFacade.moveDown(moviesListDataModel.getMovieAt(index));
        moviesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        saved = false;
    }

    /**
     * Initializes list.
     */
    private void initList() {
        moviesListDataModel = new MoviesListDataModel(movieFacade);
        list.setModel(moviesListDataModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setComponentPopupMenu(popupMenu);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                listValueChangedAction();
            }

        });
    }

    /**
     * Performs action for change of list value.
     */
    private void listValueChangedAction() {
        final boolean isSelectedRow = list.getSelectedIndices().length == 1;
        final int selectedRow = list.getSelectedIndex();
        final boolean validRowIndex = selectedRow >= 0;
        final boolean validSelection = isSelectedRow && validRowIndex;
        removePopupMenuItem.setEnabled(validSelection);
        updatePopupMenuItem.setEnabled(validSelection);
        duplicatePopupMenuItem.setEnabled(validSelection);
        tabbedPane.removeAll();
        if (validSelection) {
            tabbedPane.add("Data", new MovieDataPanel(moviesListDataModel.getMovieAt(selectedRow)));
        }
        if (isSelectedRow && selectedRow > 0) {
            moveUpPopupMenuItem.setEnabled(true);
        } else {
            moveUpPopupMenuItem.setEnabled(false);
        }
        if (validSelection && selectedRow < list.getModel().getSize() - 1) {
            moveDownPopupMenuItem.setEnabled(true);
        } else {
            moveDownPopupMenuItem.setEnabled(false);
        }
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group data = layout.createSequentialGroup()
                .addComponent(listScrollPane, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE)
                .addGap(5)
                .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        return layout.createParallelGroup()
                .addGroup(data)
                .addComponent(statsTableScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group data = layout.createParallelGroup()
                .addComponent(listScrollPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbedPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE);

        return layout.createSequentialGroup()
                .addGroup(data)
                .addGap(2)
                .addComponent(statsTableScrollPane, VERTICAL_STATS_SCROLL_PANE_SIZE, VERTICAL_STATS_SCROLL_PANE_SIZE, VERTICAL_STATS_SCROLL_PANE_SIZE);
    }

}
