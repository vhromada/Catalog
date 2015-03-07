package cz.vhromada.catalog.gui.songs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.Pictures;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with songs' data.
 *
 * @author Vladimir Hromada
 */
public class SongsPanel extends JPanel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal scroll pane size */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 300;

    /** Vertical data component size */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /** Update property */
    private static final String UPDATE_PROPERTY = "update";

    /** Popup menu */
    private JPopupMenu popupMenu = new JPopupMenu();

    /** Menu item for adding song */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Pictures.getPicture("add"));

    /** Menu item for updating song */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Pictures.getPicture("update"));

    /** Menu item for removing song */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Pictures.getPicture("remove"));

    /** Menu item for duplicating song */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Pictures.getPicture("duplicate"));

    /** Menu item for moving up song */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Pictures.getPicture("up"));

    /** Menu item for moving down song */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Pictures.getPicture("down"));

    /** List with songs */
    private JList<String> list = new JList<>();

    /** ScrollPane for list with songs */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /** Tabbed pane with song's data */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Facade for songs */
    private SongFacade songFacade;

    /** Data model for list with songs */
    private SongsListDataModel songsListDataModel;

    /** TO for music */
    private MusicTO music;

    /**
     * Creates a new instance of SongsPanel.
     *
     * @param songFacade facade for songs
     * @param music      TO for music
     * @throws IllegalArgumentException if facade for songs is null
     *                                  or TO for music is null
     */
    public SongsPanel(final SongFacade songFacade, final MusicTO music) {
        Validators.validateArgumentNotNull(songFacade, "Facade for songs");
        Validators.validateArgumentNotNull(music, "TO for music");

        this.songFacade = songFacade;
        this.music = music;
        initComponents();
    }

    /**
     * Sets a new value to TO for music.
     *
     * @param music TO for music
     * @throws IllegalArgumentException if TO for music is null
     */
    public void setMusic(final MusicTO music) {
        Validators.validateArgumentNotNull(music, "TO for music");

        this.music = music;
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
        for (final JMenuItem menuItem : menuItems) {
            popupMenu.add(menuItem);
        }
    }

    /** Performs action for button Add. */
    private void addAction() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final SongInfoDialog dialog = new SongInfoDialog();
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final SongTO song = dialog.getSongTO();
                    song.setMusic(music);
                    songFacade.add(song);
                    songsListDataModel.update();
                    list.updateUI();
                    list.setSelectedIndex(list.getModel().getSize() - 1);
                    firePropertyChange(UPDATE_PROPERTY, false, true);
                }
            }

        });
    }

    /** Performs action for button Update. */
    private void updateAction() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final SongTO song = songsListDataModel.getSongAt(list.getSelectedIndex());
                final SongInfoDialog dialog = new SongInfoDialog(song);
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final SongTO updatedSongTO = dialog.getSongTO();
                    songFacade.update(updatedSongTO);
                    songsListDataModel.update();
                    list.updateUI();
                    ((SongDataPanel) tabbedPane.getComponentAt(0)).updateSongTO(updatedSongTO);
                    firePropertyChange(UPDATE_PROPERTY, false, true);
                }
            }

        });
    }

    /** Performs action for button Remove. */
    private void removeAction() {
        songFacade.remove(songsListDataModel.getSongAt(list.getSelectedIndex()));
        songsListDataModel.update();
        list.updateUI();
        list.clearSelection();
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button Duplicate. */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        songFacade.duplicate(songsListDataModel.getSongAt(index));
        songsListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button MoveUp. */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        songFacade.moveUp(songsListDataModel.getSongAt(index));
        songsListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Performs action for button MoveDown. */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        songFacade.moveDown(songsListDataModel.getSongAt(index));
        songsListDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /** Initializes list. */
    private void initList() {
        songsListDataModel = new SongsListDataModel(songFacade, music);
        list.setModel(songsListDataModel);
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
            final SongTO song = songsListDataModel.getSongAt(selectedRow);
            tabbedPane.add("Data", new SongDataPanel(song));
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
