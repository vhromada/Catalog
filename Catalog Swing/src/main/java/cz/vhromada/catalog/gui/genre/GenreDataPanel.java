package cz.vhromada.catalog.gui.genre;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstant2;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with genre's data.
 *
 * @author Vladimir Hromada
 */
public class GenreDataPanel extends JPanel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size */
    private static final int HORIZONTAL_LABEL_SIZE = 150;

    /** Horizontal data size */
    private static final int HORIZONTAL_DATA_SIZE = 600;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 10;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Label for name */
    private JLabel nameLabel = new JLabel("Name");

    /** Label with name */
    private JLabel nameData = new JLabel();

    /**
     * Creates a new instance of GenreDataPanel.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     */
    public GenreDataPanel(final GenreTO genre) {
        Validators.validateArgumentNotNull(genre, "TO for genre");

        initData(nameLabel, nameData, genre.getName());
        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Updates TO for genre.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     */
    public void updateGenre(final GenreTO genre) {
        Validators.validateArgumentNotNull(genre, "TO for genre");

        nameData.setText(genre.getName());
    }

    /**
     * Initializes data.
     *
     * @param label label
     * @param data  data
     * @param text  text for data
     */
    private static void initData(final JLabel label, final JLabel data, final String text) {
        label.setFocusable(false);
        label.setLabelFor(data);
        data.setText(text);
        data.setFocusable(false);
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(createHorizontalDataComponents(layout, nameLabel, nameData))
                .addGap(HORIZONTAL_GAP_SIZE);

    }

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout layout
     * @param label  label
     * @param data   data
     * @return horizontal layout for label component with data component
     */
    private GroupLayout.Group createHorizontalDataComponents(final GroupLayout layout, final JLabel label, final JLabel data) {
        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(data, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        return layout.createSequentialGroup()
                .addGap(5)
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(5);
    }

    /**
     * Returns vertical layout for label component with data component.
     *
     * @param layout layout
     * @param label  label component
     * @param data   data component
     * @return vertical layout for label component with data component
     */
    private GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
        return layout.createParallelGroup()
                .addComponent(label, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(data, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE);
    }

}
