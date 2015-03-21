package cz.vhromada.catalog.gui.commons;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.vhromada.validators.Validators;

/**
 * An abstract class represents panel with data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractDataPanel<T> extends JPanel {

    /**
     * Horizontal picture size
     */
    protected static final int HORIZONTAL_PICTURE_SIZE = 200;

    /**
     * Vertical picture size
     */
    protected static final int VERTICAL_PICTURE_SIZE = 180;

    /**
     * Vertical gap size
     */
    protected static final int VERTICAL_GAP_SIZE = 10;

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal label size
     */
    private static final int HORIZONTAL_LABEL_SIZE = 150;

    /**
     * Horizontal data size
     */
    private static final int HORIZONTAL_DATA_SIZE = 600;

    /**
     * Horizontal button size
     */
    private static final int HORIZONTAL_BUTTON_SIZE = 120;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 10;

    /**
     * Updates data.
     *
     * @param data data
     * @throws IllegalArgumentException if data are null
     */
    public final void updateData(final T data) {
        Validators.validateArgumentNotNull(data, "Data");

        updateComponentData(data);
    }

    /**
     * Updates component data.
     *
     * @param data data
     */
    protected abstract void updateComponentData(final T data);

    /**
     * Initializes data.
     *
     * @param label label
     * @param data  data
     */
    protected static void initData(final JLabel label, final JLabel data) {
        label.setFocusable(false);
        label.setLabelFor(data);
        data.setFocusable(false);
    }

    /**
     * Initializes button.
     *
     * @param button     button
     * @param buttonType button type
     */
    protected void initButton(final JButton button, final WebPageButtonType buttonType) {
        button.addActionListener(new WebPageButtonActionListener(buttonType) {

            @Override
            protected String getDynamicUrlPart() {
                switch (buttonType) {
                    case WIKI_CZ:
                        return getCzWikiUrl();
                    case WIKI_EN:
                        return getEnWikiUrl();
                    case CSFD:
                        return getCsfdUrl();
                    case IMDB:
                        return Integer.toString(getImdbUrl());
                    default:
                        throw new IllegalArgumentException("Unknown button type - " + buttonType);
                }
            }

        });
    }

    /**
     * Returns URL to czech Wikipedia page
     *
     * @return URL to czech Wikipedia page
     */
    protected abstract String getCzWikiUrl();

    /**
     * Returns URL to english Wikipedia page
     *
     * @return URL to english Wikipedia page
     */
    protected abstract String getEnWikiUrl();

    /**
     * Returns URL to ČSFD page
     *
     * @return URL to ČSFD page
     */
    protected abstract String getCsfdUrl();

    /**
     * Returns URL to IMDB page
     *
     * @return URL to IMDB page
     */
    protected abstract int getImdbUrl();

    /**
     * Creates layout.
     */
    protected final void createLayout() {
        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Returns horizontal layout for components.
     *
     * @param layout layout
     * @param group  group
     * @return horizontal layout for components
     */
    protected abstract GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, GroupLayout.Group group);

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout layout
     * @param label  label
     * @param data   data
     * @return horizontal layout for label component with data component
     */
    protected GroupLayout.Group createHorizontalDataComponents(final GroupLayout layout, final JLabel label, final JLabel data) {
        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(data, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE);
    }

    /**
     * Returns horizontal layout for buttons.
     *
     * @param layout  layout
     * @param buttons buttons
     * @return horizontal layout for buttons
     */
    protected GroupLayout.Group createHorizontalButtons(final GroupLayout layout, final JButton... buttons) {
        final GroupLayout.Group result = layout.createSequentialGroup();
        for (int i = 0; i < buttons.length - 1; i++) {
            result.addComponent(buttons[i], HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                    .addGap(HORIZONTAL_GAP_SIZE);
        }

        return result.addComponent(buttons[buttons.length - 1], HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);
    }

    /**
     * Returns vertical layout for components.
     *
     * @param layout layout
     * @param group  group
     * @return vertical layout for components
     */
    protected abstract GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, GroupLayout.Group group);

    /**
     * Returns vertical layout for label component with data component.
     *
     * @param layout layout
     * @param label  label component
     * @param data   data component
     * @return vertical layout for label component with data component
     */
    protected GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
        return layout.createParallelGroup()
                .addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(data, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
    }

    /**
     * Returns vertical layout for buttons.
     *
     * @param layout  layout
     * @param buttons buttons
     * @return vertical layout for buttons
     */
    protected GroupLayout.Group createVerticalButtons(final GroupLayout layout, final JButton... buttons) {
        final GroupLayout.Group result = layout.createParallelGroup();
        for (final JButton button : buttons) {
            result.addComponent(button, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                    CatalogSwingConstants.VERTICAL_BUTTON_SIZE);
        }

        return result;
    }

    /**
     * Returns horizontal layout for components.
     *
     * @param layout layout
     * @return horizontal layout for components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(getHorizontalLayoutWithComponents(layout, layout.createParallelGroup()))
                .addGap(HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group components = layout.createSequentialGroup()
                .addGap(VERTICAL_GAP_SIZE);

        return getVerticalLayoutWithComponents(layout, components)
                .addGap(VERTICAL_GAP_SIZE);
    }

}
