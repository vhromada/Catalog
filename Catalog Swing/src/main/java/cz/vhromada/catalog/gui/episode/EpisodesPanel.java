package cz.vhromada.catalog.gui.episode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with episodes' data.
 *
 * @author Vladimir Hromada
 */
public class EpisodesPanel extends JPanel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal scroll pane size */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 100;

    /** Vertical data component size */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /** Update property */
    private static final String UPDATE_PROPERTY = "update";

    /** Popup menu */
    private JPopupMenu popupMenu = new JPopupMenu();

    /** Menu item for adding episode */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Picture.ADD.getIcon());

    /** Menu item for updating episode */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Picture.UPDATE.getIcon());

    /** Menu item for removing episode */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Picture.REMOVE.getIcon());

    /** Menu item for duplicating episode */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Picture.DUPLICATE.getIcon());

    /** Menu item for moving up episode */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Picture.UP.getIcon());

    /** Menu item for moving down episode */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Picture.DOWN.getIcon());

    /** List with episodes */
    private JList<String> list = new JList<>();

    /** ScrollPane for list with episodes */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /** Tabbed pane with episode's data */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Facade for episodes */
    private EpisodeFacade episodeFacade;

    /** Data model for list with episodes */
    private EpisodesListDataModel episodesListDataModel;

    /** TO for season */
    private SeasonTO season;

    /**
     * Creates a new instance of EpisodesPanel.
     *
     * @param episodeFacade facade for episodes
     * @param season        TO for season
     * @throws IllegalArgumentException if facade for episodes is null
     *                                  or TO for season is null
     */
    public EpisodesPanel(final EpisodeFacade episodeFacade, final SeasonTO season) {
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");
        Validators.validateArgumentNotNull(season, "TO for season");

        this.episodeFacade = episodeFacade;
        this.season = season;
        initComponents();
    }

    /**
     * Sets a new value to TO for season.
     *
     * @param season new value
     * @throws IllegalArgumentException if TO for season is null
     */
    public void setSeason(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, "TO for season");

        this.season = season;
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
                final EpisodeInfoDialog dialog = new EpisodeInfoDialog();
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final EpisodeTO episode = dialog.getEpisode();
                    episode.setSeason(season);
                    episodeFacade.add(episode);
                    episodesListDataModel.update();
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
                final EpisodeTO episode = episodesListDataModel.getEpisodeAt(list.getSelectedIndex());
                final EpisodeInfoDialog dialog = new EpisodeInfoDialog(episode);
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final EpisodeTO updatedEpisode = dialog.getEpisode();
                    episodeFacade.update(updatedEpisode);
                    episodesListDataModel.update();
                    list.updateUI();
                    ((EpisodeDataPanel) tabbedPane.getComponentAt(0)).updateEpisode(updatedEpisode);
                    firePropertyChange(UPDATE_PROPERTY, false, true);
                }
            }

        });
    }

    /** Performs action for button Remove. */
    private void removeAction() {
        episodeFacade.remove(episodesListDataModel.getEpisodeAt(list.getSelectedIndex()));
        episodesListDataModel.update();
        list.updateUI();
        list.clearSelection();
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button Duplicate. */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        episodeFacade.duplicate(episodesListDataModel.getEpisodeAt(index));
        episodesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button MoveUp. */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        episodeFacade.moveUp(episodesListDataModel.getEpisodeAt(index));
        episodesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button MoveDown. */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        episodeFacade.moveDown(episodesListDataModel.getEpisodeAt(index));
        episodesListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Initializes list. */
    private void initList() {
        episodesListDataModel = new EpisodesListDataModel(episodeFacade, season);
        list.setModel(episodesListDataModel);
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
            final EpisodeTO episode = episodesListDataModel.getEpisodeAt(selectedRow);
            tabbedPane.add("Data", new EpisodeDataPanel(episode));
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
