package cz.vhromada.catalog.gui.season;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.Picture;
import cz.vhromada.catalog.gui.episode.EpisodesPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with seasons' data.
 *
 * @author Vladimir Hromada
 */
public class SeasonsPanel extends JPanel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal scroll pane size */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 200;

    /** Vertical data component size */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /** Update property */
    private static final String UPDATE_PROPERTY = "update";

    /** Popup menu */
    private JPopupMenu popupMenu = new JPopupMenu();

    /** Menu item for adding season */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Picture.ADD.getIcon());

    /** Menu item for updating season */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Picture.UPDATE.getIcon());

    /** Menu item for removing season */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Picture.REMOVE.getIcon());

    /** Menu item for duplicating season */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Picture.DUPLICATE.getIcon());

    /** Menu item for moving up season */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Picture.UP.getIcon());

    /** Menu item for moving down season */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Picture.DOWN.getIcon());

    /** List with seasons */
    private JList<Integer> list = new JList<>();

    /** ScrollPane for list with seasons */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /** Tabbed pane with season's data */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Facade for seasons */
    private SeasonFacade seasonFacade;

    /** Facade for episodes */
    private EpisodeFacade episodeFacade;

    /** Data model for list with seasons */
    private SeasonsListDataModel seasonsListDataModel;

    /** TO for serie */
    private SerieTO serie;

    /**
     * Creates a new instance of SeasonsPanel.
     *
     * @param seasonFacade  facade for seasons
     * @param episodeFacade facade for episodes
     * @param serie        TO for serie
     * @throws IllegalArgumentException if facade for seasons is null
     *                                  or facade for episodes is null
     *                                  or TO for serie is null
     */
    public SeasonsPanel(final SeasonFacade seasonFacade, final EpisodeFacade episodeFacade, final SerieTO serie) {
        Validators.validateArgumentNotNull(seasonFacade, "Facade for seasons");
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");
        Validators.validateArgumentNotNull(serie, "TO for serie");

        this.seasonFacade = seasonFacade;
        this.episodeFacade = episodeFacade;
        this.serie = serie;
        initComponents();
    }

    /**
     * Sets a new value to TO for serie.
     *
     * @param serie new value
     * @throws IllegalArgumentException if TO for serie is null
     */
    public void setSerie(final SerieTO serie) {
        Validators.validateArgumentNotNull(serie, "TO for serie");

        this.serie = serie;
    }

    /** Initializes components. */
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
        popupMenu.add(moveDownPopupMenuItem);

        initList();

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

    /** Performs action for button Add. */
    private void addAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final SeasonInfoDialog dialog = new SeasonInfoDialog();
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final SeasonTO season = dialog.getSeason();
                    season.setSerie(serie);
                    seasonFacade.add(season);
                    seasonsListDataModel.update();
                    list.updateUI();
                    list.setSelectedIndex(list.getModel().getSize() - 1);
                    firePropertyChange(UPDATE_PROPERTY, false, true);
                }
            }

        });
    }

    /** Performs action for button Update. */
    private void updateAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final SeasonTO season = seasonsListDataModel.getSeasonAt(list.getSelectedIndex());
                final SeasonInfoDialog dialog = new SeasonInfoDialog(season);
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final SeasonTO updatedSeason = dialog.getSeason();
                    seasonFacade.update(updatedSeason);
                    seasonsListDataModel.update();
                    list.updateUI();
                    ((SeasonDataPanel) tabbedPane.getComponentAt(0)).updateSeason(updatedSeason);
                    firePropertyChange(UPDATE_PROPERTY, false, true);
                }
            }

        });
    }

    /** Performs action for button Remove. */
    private void removeAction() {
        seasonFacade.remove(seasonsListDataModel.getSeasonAt(list.getSelectedIndex()));
        seasonsListDataModel.update();
        list.updateUI();
        list.clearSelection();
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button Duplicate. */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        seasonFacade.duplicate(seasonsListDataModel.getSeasonAt(index));
        seasonsListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button MoveUp. */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        seasonFacade.moveUp(seasonsListDataModel.getSeasonAt(index));
        seasonsListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button MoveDown. */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        seasonFacade.moveDown(seasonsListDataModel.getSeasonAt(index));
        seasonsListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Initializes list. */
    private void initList() {
        seasonsListDataModel = new SeasonsListDataModel(seasonFacade, serie);
        list.setModel(seasonsListDataModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setComponentPopupMenu(popupMenu);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                listValueChangedAction();
            }

        });
    }

    /** Performs action for change of list value. */
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
            final SeasonTO season = seasonsListDataModel.getSeasonAt(selectedRow);
            tabbedPane.add("Data", new SeasonDataPanel(season, episodeFacade));
            final EpisodesPanel episodesPanel = new EpisodesPanel(episodeFacade, season);
            episodesPanel.addPropertyChangeListener(UPDATE_PROPERTY, new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (Boolean.TRUE.equals(evt.getNewValue())) {
                        seasonsListDataModel.update();
                        list.updateUI();
                        final SeasonTO newSeason = seasonsListDataModel.getSeasonAt(selectedRow);
                        ((SeasonDataPanel) tabbedPane.getComponentAt(0)).updateSeason(newSeason);
                        episodesPanel.setSeason(newSeason);
                        firePropertyChange(UPDATE_PROPERTY, false, true);
                    }
                }

            });
            tabbedPane.add("Episodes", episodesPanel);
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
        return layout.createSequentialGroup()
                .addComponent(listScrollPane, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE)
                .addGap(5)
                .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        return layout.createParallelGroup()
                .addComponent(listScrollPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbedPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE);
    }

}
