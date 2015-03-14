package cz.vhromada.catalog.gui.genre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstant2;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.InputValidator;
import cz.vhromada.catalog.gui.Picture;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for genre.
 *
 * @author Vladimir Hromada
 */
public class GenreInfoDialog extends JDialog {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size in dialog */
    private static final int HORIZONTAL_LABEL_DIALOG_SIZE = 100;

    /** Horizontal data size in dialog */
    private static final int HORIZONTAL_DATA_DIALOG_SIZE = 200;

    /** Horizontal button size */
    private static final int HORIZONTAL_BUTTON_SIZE = 96;

    /** Horizontal button gap size */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 32;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Vertical long gap size */
    private static final int VERTICAL_LONG_GAP_SIZE = 20;

    /** Return status */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /** TO for genre */
    private GenreTO genre;

    /** Label for name */
    private JLabel nameLabel = new JLabel("Name");

    /** Text field for name */
    private JTextField nameData = new JTextField();

    /** Button OK */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /** Button Cancel */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /** Creates a new instance of GenreInfoDialog. */
    public GenreInfoDialog() {
        this("Add", Picture.ADD);

        nameData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of GenreInfoDialog.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     */
    public GenreInfoDialog(final GenreTO genre) {
        this("Update", Picture.UPDATE);

        Validators.validateArgumentNotNull(genre, "TO for genre");

        this.genre = genre;
        this.nameData.setText(genre.getName());
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of GenreInfoDialog.
     *
     * @param name    name
     * @param picture picture
     */
    private GenreInfoDialog(final String name, final Picture picture) {
        super(new JFrame(), name, true);

        initComponents();
        setIconImage(picture.getIcon().getImage());
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
     * Returns TO for genre.
     *
     * @return TO for genre
     * @throws IllegalStateException if TO for genre hasn't been set
     */
    public GenreTO getGenre() {
        Validators.validateFieldNotNull(genre, "TO for genre");

        return genre;
    }

    /** Initializes components. */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        initLabelComponent(nameLabel, nameData);

        nameData.getDocument().addDocumentListener(new InputValidator(okButton) {

            @Override
            public boolean isInputValid() {
                return GenreInfoDialog.this.isInputValid();
            }

        });

        okButton.setEnabled(false);
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

        final GroupLayout layout = new GroupLayout(getRootPane());
        getRootPane().setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));

        pack();
        setLocationRelativeTo(getRootPane());
    }

    /**
     * Initializes label with component.
     *
     * @param label     label
     * @param component component
     */
    private static void initLabelComponent(final JLabel label, final JComponent component) {
        label.setLabelFor(component);
        label.setFocusable(false);
    }

    /** Performs action for button OK. */
    private void okAction() {
        returnStatus = DialogResult.OK;
        if (genre == null) {
            genre = new GenreTO();
        }
        genre.setName(nameData.getText());
        close();
    }

    /** Performs action for button Cancel. */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        genre = null;
        close();
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
                .addGap(54)
                .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE);

        final GroupLayout.Group componentsGroup = layout.createParallelGroup()
                .addGroup(createHorizontalComponents(layout, nameLabel, nameData))
                .addGroup(buttons);

        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(componentsGroup)
                .addGap(HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout layout
     * @param label  label component
     * @param data   data component
     * @return horizontal layout for label component with data component
     */
    private GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
                .addGap(10)
                .addComponent(data, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
    }


    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(okButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(buttons)
                .addGap(VERTICAL_LONG_GAP_SIZE);
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

    /**
     * Returns true if input is valid: name isn't empty string.
     *
     * @return true if input is valid: name isn't empty string
     */
    private boolean isInputValid() {
        return !nameData.getText().isEmpty();
    }

    /** Closes dialog. */
    private void close() {
        setVisible(false);
        dispose();
    }

}
