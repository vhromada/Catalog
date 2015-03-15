package cz.vhromada.catalog.gui.commons;

import java.awt.Component;
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
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.vhromada.validators.Validators;

/**
 * An abstract class represents panel with data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractInnerDataPanel<T> extends JPanel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal scroll pane size
     */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 200;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 5;

    /**
     * Vertical data component size
     */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /**
     * Update property
     */
    private static final String UPDATE_PROPERTY = "update";

    /**
     * Popup menu
     */
    private JPopupMenu popupMenu = new JPopupMenu();

    /**
     * Menu item for adding data
     */
    private JMenuItem addPopupMenuItem = new JMenuItem("Add", Picture.ADD.getIcon());

    /**
     * Menu item for updating data
     */
    private JMenuItem updatePopupMenuItem = new JMenuItem("Update", Picture.UPDATE.getIcon());

    /**
     * Menu item for removing data
     */
    private JMenuItem removePopupMenuItem = new JMenuItem("Remove", Picture.REMOVE.getIcon());

    /**
     * Menu item for duplicating data
     */
    private JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Picture.DUPLICATE.getIcon());

    /**
     * Menu item for moving up data
     */
    private JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Picture.UP.getIcon());

    /**
     * Menu item for moving down data
     */
    private JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Picture.DOWN.getIcon());

    /**
     * List with data
     */
    private JList<String> list = new JList<>();

    /**
     * ScrollPane for list with data
     */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /**
     * Tabbed pane with data
     */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * Data model for list
     */
    private AbstractListDataModel<T> listDataModel;

    /**
     * Creates a new instance of AbstractInnerDataPanel.
     *
     * @param listDataModel data model for list
     * @throws IllegalArgumentException if data model for list is null
     */
    public AbstractInnerDataPanel(final AbstractListDataModel<T> listDataModel) {
        Validators.validateArgumentNotNull(listDataModel, "Data model for list");

        this.listDataModel = listDataModel;
        initComponents();
    }

    /**
     * Returns info dialog.
     *
     * @param add  true if dialog is for adding data
     * @param data data
     * @return info dialog
     */
    protected abstract AbstractInfoDialog<T> getInfoDialog(final boolean add, final T data);

    /**
     * Adds data.
     *
     * @param data data
     */
    protected abstract void addData(final T data);

    /**
     * Updates data.
     *
     * @param data data
     */
    protected abstract void updateData(final T data);

    /**
     * Removes data.
     *
     * @param data data
     */
    protected abstract void removeData(final T data);

    /**
     * Duplicates data.
     *
     * @param data data
     */
    protected abstract void duplicatesData(final T data);

    /**
     * Moves up data.
     *
     * @param data data
     */
    protected abstract void moveUpData(final T data);

    /**
     * Moves down data.
     *
     * @param data data
     */
    protected abstract void moveDownData(final T data);

    /**
     * Updates data panel.
     *
     * @param dataPanel data panel
     * @param data      data
     */
    protected abstract void updateDataPanel(final Component dataPanel, final T data);

    /**
     * Returns data panel.
     *
     * @param data data
     * @return data panel
     */
    protected abstract JPanel getDataPanel(final T data);

    /**
     * Updates data on change.
     *
     * @param dataPanel tabbed pane with data
     * @param data      data
     */
    protected void updateDataOnChange(final JTabbedPane dataPanel, final T data) {
    }

    /**
     * Updates model.
     *
     * @param data data
     */
    protected final void updateModel(final T data) {
        listDataModel.update();
        list.updateUI();
        updateDataPanel(tabbedPane.getComponentAt(0), data);
        firePropertyChange(UPDATE_PROPERTY, false, true);
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

        list.setModel(listDataModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setComponentPopupMenu(popupMenu);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                listValueChangedAction();
            }

        });

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

    /**
     * Performs action for button Add.
     */
    private void addAction() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final AbstractInfoDialog<T> dialog = getInfoDialog(true, null);
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    addData(dialog.getData());
                    listDataModel.update();
                    list.updateUI();
                    list.setSelectedIndex(list.getModel().getSize() - 1);
                    firePropertyChange(UPDATE_PROPERTY, false, true);
                }
            }

        });
    }

    /**
     * Performs action for button Update.
     */
    private void updateAction() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final AbstractInfoDialog<T> dialog = getInfoDialog(false, listDataModel.getObjectAt(list.getSelectedIndex()));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    final T data = dialog.getData();
                    updateData(data);
                    updateModel(data);
                }
            }

        });
    }

    /**
     * Performs action for button Remove.
     */
    private void removeAction() {
        removeData(listDataModel.getObjectAt(list.getSelectedIndex()));
        listDataModel.update();
        list.updateUI();
        list.clearSelection();
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /**
     * Performs action for button Duplicate.
     */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        duplicatesData(listDataModel.getObjectAt(index));
        listDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /**
     * Performs action for button MoveUp.
     */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        moveUpData(listDataModel.getObjectAt(index));
        listDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
    }

    /**
     * Performs action for button MoveDown.
     */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        moveDownData(listDataModel.getObjectAt(index));
        listDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        firePropertyChange(UPDATE_PROPERTY, false, true);
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
            final T data = listDataModel.getObjectAt(selectedRow);
            tabbedPane.add("Data", getDataPanel(data));
            updateDataOnChange(tabbedPane, data);
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
                .addGap(HORIZONTAL_GAP_SIZE)
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
