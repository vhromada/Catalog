package cz.vhromada.catalog.gui.commons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import cz.vhromada.validators.Validators;

/**
 * An abstract class represents dialog for adding or updating data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractInfoDialog<T> extends JDialog {

    /**
     * Horizontal long component size
     */
    protected static final int HORIZONTAL_LONG_COMPONENT_SIZE = 310;

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
    private static final int HORIZONTAL_LABEL_DIALOG_SIZE = 100;

    /**
     * Horizontal data size
     */
    private static final int HORIZONTAL_DATA_DIALOG_SIZE = 200;

    /**
     * Horizontal button size
     */
    private static final int HORIZONTAL_BUTTON_SIZE = 96;

    /**
     * Horizontal size of gap between label and data
     */
    private static final int HORIZONTAL_DATA_GAP_SIZE = 10;

    /**
     * Horizontal button gap size
     */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 32;

    /**
     * Horizontal size of gap between button
     */
    private static final int HORIZONTAL_BUTTONS_GAP_SIZE = 54;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /**
     * Vertical long gap size
     */
    private static final int VERTICAL_LONG_GAP_SIZE = 20;

    /**
     * Return status
     */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /**
     * Data
     */
    private T data;

    /**
     * Button OK
     */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /**
     * Button Cancel
     */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /**
     * Creates a new instance of AbstractInfoDialog.
     */
    public AbstractInfoDialog() {
        this("Add", Picture.ADD);

        okButton.setEnabled(false);
    }

    /**
     * Creates a new instance of AbstractInfoDialog.
     *
     * @param data data
     * @throws IllegalArgumentException if data are null
     */
    public AbstractInfoDialog(final T data) {
        this("Update", Picture.UPDATE);

        Validators.validateArgumentNotNull(data, "Data");

        this.data = data;
    }

    /**
     * Creates a new instance of AbstractInfoDialog.
     *
     * @param name    name
     * @param picture picture
     */
    private AbstractInfoDialog(final String name, final Picture picture) {
        super(new JFrame(), name, true);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setIconImage(picture.getIcon().getImage());

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                okAction();
            }

        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                cancelAction();
            }

        });
    }

    /**
     * Returns return status.
     *
     * @return return status
     */
    public DialogResult getReturnStatus() {
        return returnStatus;
    }

    /**
     * Returns data.
     *
     * @return datae
     * @throws IllegalStateException if data haven't been set
     */
    public T getData() {
        Validators.validateFieldNotNull(data, "Data");

        return data;
    }

    /**
     * Initializes components.
     */
    protected abstract void initComponents();

    /**
     * Returns object with filled data.
     *
     * @param objectData object for filling data
     * @return object with filled data
     */
    protected abstract T processData(final T objectData);

    /**
     * Returns horizontal layout with added components.
     *
     * @param layout horizontal layout
     * @param group  group in vertical layout
     * @return horizontal layout with added components
     */
    protected abstract GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group);

    /**
     * Returns vertical layout with added components.
     *
     * @param layout vertical layout
     * @param group  group in vertical layout
     * @return vertical layout with added components
     */
    protected abstract GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group);

    /**
     * Initializes.
     */
    protected final void init() {
        initComponents();
        createLayout();
    }

    /**
     * Initializes label with component.
     *
     * @param label     label
     * @param component component
     */
    protected static void initLabelComponent(final JLabel label, final JComponent component) {
        label.setLabelFor(component);
        label.setFocusable(false);
    }

    /**
     * Adds validator to input.
     *
     * @param input input
     */
    protected final void addInputValidator(final JTextComponent input) {
        input.getDocument().addDocumentListener(new InputValidator(okButton) {

            @Override
            public boolean isInputValid() {
                return AbstractInfoDialog.this.isInputValid();
            }

        });
    }

    /**
     * Adds validator to spinner.
     *
     * @param spinner spinner
     */
    protected final void addSpinnerValidator(final JSpinner spinner) {
        spinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                okButton.setEnabled(isInputValid());
            }

        });
    }

    /**
     * Returns true if input is valid.
     *
     * @return true if input is valid
     */
    protected boolean isInputValid() {
        return true;
    }

    /**
     * Sets enabled to button OK.
     *
     * @param enabled true if button should be enabled
     */
    protected final void setOkButtonEnabled(final boolean enabled) {
        okButton.setEnabled(enabled);
    }

    /**
     * Creates layout.
     */
    protected final void createLayout() {
        final GroupLayout layout = new GroupLayout(getRootPane());
        getRootPane().setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));

        pack();
        setLocationRelativeTo(getRootPane());
    }

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout         layout
     * @param labelComponent label component
     * @param dataComponent  data component
     * @return horizontal layout for label component with data component
     */
    protected final GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent labelComponent, final JComponent dataComponent) {
        return layout.createSequentialGroup()
                .addComponent(labelComponent, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
                .addGap(HORIZONTAL_DATA_GAP_SIZE)
                .addComponent(dataComponent, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
    }

    /**
     * Returns horizontal layout for selectable component.
     *
     * @param layout    layout
     * @param component component
     * @return horizontal layout for selectable component
     */
    protected final GroupLayout.Group createHorizontalSelectableComponent(final GroupLayout layout, final JComponent component) {
        return layout.createSequentialGroup()
                .addGap(110)
                .addComponent(component, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
    }

    /**
     * Returns vertical layout for label component with data component.
     *
     * @param layout         layout
     * @param labelComponent label component
     * @param dataComponent  data component
     * @return vertical layout for label component with data component
     */
    protected final GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent labelComponent, final JComponent dataComponent) {
        return layout.createParallelGroup()
                .addComponent(labelComponent, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(dataComponent, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
    }

    /**
     * Performs action for button OK.
     */
    private void okAction() {
        returnStatus = DialogResult.OK;
        data = processData(data);
        close();
    }

    /**
     * Performs action for button Cancel.
     */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        data = null;
        close();
    }

    /**
     * Closes dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
                .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTONS_GAP_SIZE)
                .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE);


        final GroupLayout.Group components = getHorizontalLayoutWithComponents(layout, layout.createParallelGroup())
                .addGroup(buttons);

        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(components)
                .addGap(HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        final GroupLayout.Group components = layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE);

        return getVerticalLayoutWithComponents(layout, components)
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(buttons)
                .addGap(VERTICAL_LONG_GAP_SIZE);
    }

}
