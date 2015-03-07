package cz.vhromada.catalog.gui.music;

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

import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.Pictures;
import cz.vhromada.catalog.gui.StatsTableCellRenderer;
import cz.vhromada.catalog.gui.song.SongsPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with music data.
 *
 * @author Vladimir Hromada
 */
public class MusicPanel extends JPanel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal scroll pane size */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 300;

    /** Vertical data component size */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /** Vertical size for scroll pane for table with stats */
    private static final int VERTICAL_STATS_SCROLL_PANE_SIZE = 45;

    /** Popup menu */
    private JPopupMenu popupMenu = new JPopupMenu();

    /** Menu item for adding music */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Pictures.getPicture("add"));

    /** Menu item for updating music */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Pictures.getPicture("update"));

    /** Menu item for removing music */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Pictures.getPicture("remove"));

    /** Menu item for duplicating music */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Pictures.getPicture("duplicate"));

    /** Menu item for moving up music */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Pictures.getPicture("up"));

    /** Menu item for moving down music */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Pictures.getPicture("down"));

    /** List with music */
    private JList<String> list = new JList<>();

    /** ScrollPane for list with music */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /** Tabbed pane with music data */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Table with with music stats */
    private JTable statsTable = new JTable();

    /** ScrollPane for table with music stats */
    private JScrollPane statsTableScrollPane = new JScrollPane(statsTable);

    /** Facade for music */
    private MusicFacade musicFacade;

    /** Facade for songs */
    private SongFacade songFacade;

    /** Data model for list with music */
    private MusicListDataModel musicListDataModel;

    /** Data model for table with stats for music */
    private MusicStatsTableDataModel musicStatsTableDataModel;

    /** True if data is saved */
    private boolean saved;

    /**
     * Creates a new instance of MusicPanel.
     *
     * @param musicFacade facade for music
     * @param songFacade facade for songs
     * @throws IllegalArgumentException if facade for music is null
     *                                  or facade for songs is null
     */
    public MusicPanel(final MusicFacade musicFacade, final SongFacade songFacade) {
        Validators.validateArgumentNotNull(musicFacade, "Facade for music");
        Validators.validateArgumentNotNull(songFacade, "Facade for songs");

        this.musicFacade = musicFacade;
        this.songFacade = songFacade;
        this.saved = true;
        initComponents();
    }

    /** Creates new data. */
    public void newData() {
        musicFacade.newData();
        musicListDataModel.update();
        list.clearSelection();
        list.updateUI();
        tabbedPane.removeAll();
        musicStatsTableDataModel.update();
        statsTable.updateUI();
        saved = true;
    }

    /** Clears selection. */
    public void clearSelection() {
        list.clearSelection();
        tabbedPane.removeAll();
    }

    /** Saves. */
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

        initList();

        musicStatsTableDataModel = new MusicStatsTableDataModel(musicFacade);
        statsTable.setModel(musicStatsTableDataModel);
        statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        statsTable.setEnabled(false);
        statsTable.setRowSelectionAllowed(false);
        statsTable.setDefaultRenderer(Integer.class, new StatsTableCellRenderer());

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
                final MusicInfoDialog dialog = new MusicInfoDialog();
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    musicFacade.add(dialog.getMusic());
                    musicListDataModel.update();
                    list.updateUI();
                    list.setSelectedIndex(list.getModel().getSize() - 1);
                    musicStatsTableDataModel.update();
                    statsTable.updateUI();
                    saved = false;
                }
            }

        });
    }

    /** Performs action for button Update. */
    private void updateAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final int index = list.getSelectedIndex();
                final MusicInfoDialog dialog = new MusicInfoDialog(musicListDataModel.getMusicAt(index));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final MusicTO music = dialog.getMusic();
                    musicFacade.update(music);
                    musicListDataModel.update();
                    list.updateUI();
                    ((MusicDataPanel) tabbedPane.getComponentAt(0)).updateMusic(music);
                    musicStatsTableDataModel.update();
                    statsTable.updateUI();
                    saved = false;
                }
            }

        });
    }

    /** Performs action for button Remove. */
    private void removeAction() {
        musicFacade.remove(musicListDataModel.getMusicAt(list.getSelectedIndex()));
        musicListDataModel.update();
        list.updateUI();
        list.clearSelection();
        musicStatsTableDataModel.update();
        statsTable.updateUI();
        saved = false;
    }

    /** Performs action for button Duplicate. */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        musicFacade.duplicate(musicListDataModel.getMusicAt(index));
        musicListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        musicStatsTableDataModel.update();
        statsTable.updateUI();
        saved = false;
    }

    /** Performs action for button MoveUp. */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        musicFacade.moveUp(musicListDataModel.getMusicAt(index));
        musicListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        saved = false;
    }

    /** Performs action for button MoveDown. */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        musicFacade.moveDown(musicListDataModel.getMusicAt(index));
        musicListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        saved = false;
    }

    /** Initializes list. */
    private void initList() {
        musicListDataModel = new MusicListDataModel(musicFacade);
        list.setModel(musicListDataModel);
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
            final MusicTO music = musicListDataModel.getMusicAt(selectedRow);
            tabbedPane.add("Data", new MusicDataPanel(music, songFacade));
            final SongsPanel songsPanel = new SongsPanel(songFacade, music);
            songsPanel.addPropertyChangeListener("update", new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if (Boolean.TRUE.equals(evt.getNewValue())) {
                        musicListDataModel.update();
                        list.updateUI();
                        final MusicTO newMusic = musicListDataModel.getMusicAt(selectedRow);
                        ((MusicDataPanel) tabbedPane.getComponentAt(0)).updateMusic(newMusic);
                        songsPanel.setMusic(newMusic);
                        musicStatsTableDataModel.update();
                        statsTable.updateUI();
                        saved = false;
                    }
                }

            });
            tabbedPane.add("Songs", songsPanel);
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
