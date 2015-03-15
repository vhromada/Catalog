package cz.vhromada.catalog.gui.serie;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.catalog.gui.commons.StatsTableCellRenderer;
import cz.vhromada.catalog.gui.season.SeasonsPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with series' data.
 *
 * @author Vladimir Hromada
 */
public class SeriesPanel extends JPanel {

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
     * Menu item for adding serie
     */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Picture.ADD.getIcon());

    /**
     * Menu item for updating serie
     */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Picture.UPDATE.getIcon());

    /**
     * Menu item for removing serie
     */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Picture.REMOVE.getIcon());

    /**
     * Menu item for duplicating serie
     */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Picture.DUPLICATE.getIcon());

    /**
     * Menu item for moving up serie
     */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Picture.UP.getIcon());

    /**
     * Menu item for moving down serie
     */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Picture.DOWN.getIcon());

    /**
     * List with series
     */
    private JList<String> list = new JList<>();

    /**
     * ScrollPane for list with series
     */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /**
     * Tabbed pane with serie's data
     */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * Table with with series' stats
     */
    private JTable statsTable = new JTable();

    /**
     * ScrollPane for table with series' stats
     */
    private JScrollPane statsTableScrollPane = new JScrollPane(statsTable);

    /**
     * Facade for series
     */
    private SerieFacade serieFacade;

    /**
     * Facade for seasons
     */
    private SeasonFacade seasonFacade;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * Data model for list with series
     */
    private SeriesListDataModel seriesListDataModel;

    /**
     * Data model for table with stats for series
     */
    private SeriesStatsTableDataModel seriesStatsTableDataModel;

    /**
     * True if data is saved
     */
    private boolean saved;

    /**
     * Creates a new instance of SeriesPanel.
     *
     * @param serieFacade   facade for series
     * @param seasonFacade  facade for episodes
     * @param episodeFacade facade for episodes
     * @param genreFacade   facade for genres
     * @throws IllegalArgumentException if facade for series is null
     *                                  or facade for seasons is null
     *                                  or facade for episodes is null
     *                                  or facade for genres is null
     */
    public SeriesPanel(final SerieFacade serieFacade, final SeasonFacade seasonFacade, final EpisodeFacade episodeFacade, final GenreFacade genreFacade) {
        Validators.validateArgumentNotNull(serieFacade, "Facade for series");
        Validators.validateArgumentNotNull(seasonFacade, "Facade for seasons");
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");
        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.serieFacade = serieFacade;
        this.seasonFacade = seasonFacade;
        this.episodeFacade = episodeFacade;
        this.genreFacade = genreFacade;
        this.saved = true;
        initComponents();
    }

    /**
     * Creates new data.
     */
    public void newData() {
        serieFacade.newData();
        seriesListDataModel.update();
        list.clearSelection();
        list.updateUI();
        tabbedPane.removeAll();
        seriesStatsTableDataModel.update();
        statsTable.updateUI();
        saved = true;
    }

    /**
     * Clears selection.
     */
    public void clearSelection() {
        list.clearSelection();
        tabbedPane.removeAll();
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

        seriesStatsTableDataModel = new SeriesStatsTableDataModel(serieFacade);
        statsTable.setModel(seriesStatsTableDataModel);
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
                final SerieInfoDialog dialog = new SerieInfoDialog(genreFacade);
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    serieFacade.add(dialog.getSerie());
                    seriesListDataModel.update();
                    list.updateUI();
                    list.setSelectedIndex(list.getModel().getSize() - 1);
                    seriesStatsTableDataModel.update();
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
                final SerieInfoDialog dialog = new SerieInfoDialog(genreFacade, seriesListDataModel.getSerieAt(index));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final SerieTO serie = dialog.getSerie();
                    serieFacade.update(serie);
                    seriesListDataModel.update();
                    list.updateUI();
                    ((SerieDataPanel) tabbedPane.getComponentAt(0)).updateSerie(serie);
                    seriesStatsTableDataModel.update();
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
        serieFacade.remove(seriesListDataModel.getSerieAt(list.getSelectedIndex()));
        seriesListDataModel.update();
        list.updateUI();
        list.clearSelection();
        seriesStatsTableDataModel.update();
        statsTable.updateUI();
        saved = false;
    }

    /**
     * Performs action for button Duplicate.
     */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        serieFacade.duplicate(seriesListDataModel.getSerieAt(index));
        seriesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        seriesStatsTableDataModel.update();
        statsTable.updateUI();
        saved = false;
    }

    /**
     * Performs action for button MoveUp.
     */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        serieFacade.moveUp(seriesListDataModel.getSerieAt(index));
        seriesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        saved = false;
    }

    /**
     * Performs action for button MoveDown.
     */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        serieFacade.moveDown(seriesListDataModel.getSerieAt(index));
        seriesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        saved = false;
    }

    /**
     * Initializes list.
     */
    private void initList() {
        seriesListDataModel = new SeriesListDataModel(serieFacade);
        list.setModel(seriesListDataModel);
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
            final SerieTO serie = seriesListDataModel.getSerieAt(selectedRow);
            tabbedPane.add("Data", new SerieDataPanel(serie, seasonFacade, episodeFacade));
            final SeasonsPanel seasonsPanel = new SeasonsPanel(seasonFacade, episodeFacade, serie);
            seasonsPanel.addPropertyChangeListener("update", new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (Boolean.TRUE.equals(evt.getNewValue())) {
                        seriesListDataModel.update();
                        list.updateUI();
                        final SerieTO newSerie = seriesListDataModel.getSerieAt(selectedRow);
                        ((SerieDataPanel) tabbedPane.getComponentAt(0)).updateSerie(newSerie);
                        seasonsPanel.setSerie(newSerie);
                        seriesStatsTableDataModel.update();
                        statsTable.updateUI();
                        saved = false;
                    }
                }

            });
            tabbedPane.add("Seasons", seasonsPanel);
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
