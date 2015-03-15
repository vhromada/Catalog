package cz.vhromada.catalog.gui.serie;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

import cz.vhromada.catalog.commons.Constants;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.InputValidator;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.catalog.gui.genre.GenreChooseDialog;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for serie.
 *
 * @author Vladimir Hromada
 */
public class SerieInfoDialog extends JDialog {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal label size in dialog
     */
    private static final int HORIZONTAL_LABEL_DIALOG_SIZE = 100;

    /**
     * Horizontal data size in dialog
     */
    private static final int HORIZONTAL_DATA_DIALOG_SIZE = 200;

    /**
     * Horizontal genres button size
     */
    private static final int HORIZONTAL_GENRES_BUTTON_SIZE = 310;

    /**
     * Horizontal button size
     */
    private static final int HORIZONTAL_BUTTON_SIZE = 96;

    /**
     * Horizontal button gap size
     */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 32;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /**
     * Vertical gap size
     */
    private static final int VERTICAL_GAP_SIZE = 10;

    /**
     * Vertical long gap size
     */
    private static final int VERTICAL_LONG_GAP_SIZE = 20;

    /**
     * Return status
     */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * TO for serie
     */
    private SerieTO serie;

    /**
     * Label for czech name
     */
    private JLabel czechNameLabel = new JLabel("Czech name");

    /**
     * Text field for czech name
     */
    private JTextField czechNameData = new JTextField();

    /**
     * Label for original name
     */
    private JLabel originalNameLabel = new JLabel("Original name");

    /**
     * Text field for original name
     */
    private JTextField originalNameData = new JTextField();

    /**
     * Label for ČSFD
     */
    private JLabel csfdLabel = new JLabel("\u010cSFD");

    /**
     * Text field for ČSFD
     */
    private JTextField csfdData = new JTextField();

    /**
     * Check box for IMDB code
     */
    private JCheckBox imdbCodeLabel = new JCheckBox("IMDB code");

    /**
     * Spinner for IMDB code
     */
    private JSpinner imdbCodeData = new JSpinner(new SpinnerNumberModel(1, 1, Constants.MAX_IMDB_CODE, 1));

    /**
     * Label for czech Wikipedia
     */
    private JLabel wikiCzLabel = new JLabel("Czech Wikipedia");

    /**
     * Text field for czech Wikipedia
     */
    private JTextField wikiCzData = new JTextField();

    /**
     * Label for english Wikipedia
     */
    private JLabel wikiEnLabel = new JLabel("English Wikipedia");

    /**
     * Text field for english Wikipedia
     */
    private JTextField wikiEnData = new JTextField();

    /**
     * Label for picture
     */
    private JLabel pictureLabel = new JLabel("Picture");

    /**
     * Text field for picture
     */
    private JTextField pictureData = new JTextField();

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Text field for note
     */
    private JTextField noteData = new JTextField();

    /**
     * Button for genres
     */
    private JButton genresButton = new JButton("Genres", Picture.CHOOSE.getIcon());

    /**
     * Button OK
     */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /**
     * Button Cancel
     */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /**
     * List of TO for genre
     */
    private List<GenreTO> genres = new ArrayList<>();

    /**
     * Creates a new instance of SerieInfoDialog.
     *
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    public SerieInfoDialog(final GenreFacade genreFacade) {
        this("Add", Picture.ADD, genreFacade);

        imdbCodeData.setEnabled(imdbCodeLabel.isSelected());
        okButton.setEnabled(false);
        czechNameData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of SerieInfoDialog.
     *
     * @param genreFacade facade for genres
     * @param serie       TO for serie
     * @throws IllegalArgumentException if facade for genres is null
     *                                  or TO for serie is null
     */
    public SerieInfoDialog(final GenreFacade genreFacade, final SerieTO serie) {
        this("Update", Picture.UPDATE, genreFacade);

        Validators.validateArgumentNotNull(serie, "TO for serie");

        this.serie = serie;
        this.czechNameData.setText(serie.getCzechName());
        this.originalNameData.setText(serie.getOriginalName());
        this.csfdData.setText(serie.getCsfd());
        final int imdbCode = serie.getImdbCode();
        if (imdbCode > 0) {
            this.imdbCodeLabel.setSelected(true);
            this.imdbCodeData.setValue(serie.getImdbCode());
        } else {
            this.imdbCodeLabel.setSelected(false);
            this.imdbCodeData.setEnabled(false);
        }
        this.wikiCzData.setText(serie.getWikiCz());
        this.wikiEnData.setText(serie.getWikiEn());
        this.pictureData.setText(serie.getPicture());
        this.noteData.setText(serie.getNote());
        this.genres = serie.getGenres();
        this.okButton.setEnabled(true);
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of SerieInfoDialog.
     *
     * @param name        name
     * @param picture     picture
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    private SerieInfoDialog(final String name, final Picture picture, final GenreFacade genreFacade) {
        super(new JFrame(), name, true);

        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.genreFacade = genreFacade;
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
     * Returns TO for serie.
     *
     * @return TO for serie
     * @throws IllegalStateException if TO for serie hasn't been set
     */
    public SerieTO getSerie() {
        Validators.validateFieldNotNull(serie, "TO for serie");

        return serie;
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        final DocumentListener inputValidator = new InputValidator(okButton) {

            @Override
            public boolean isInputValid() {
                return SerieInfoDialog.this.isInputValid();
            }

        };
        initLabelTextFieldWithValidator(czechNameLabel, czechNameData, inputValidator);
        initLabelTextFieldWithValidator(originalNameLabel, originalNameData, inputValidator);
        initLabelComponent(csfdLabel, csfdData);
        initLabelComponent(wikiCzLabel, wikiCzData);
        initLabelComponent(wikiEnLabel, wikiEnData);
        initLabelComponent(pictureLabel, pictureData);
        initLabelComponent(noteLabel, noteData);

        imdbCodeLabel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                imdbCodeData.setEnabled(imdbCodeLabel.isSelected());
            }

        });

        genresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                genresAction();
            }

        });

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
     * Initializes label with text field and input validator.
     *
     * @param label          label
     * @param textField      text field
     * @param inputValidator input validator
     */
    private void initLabelTextFieldWithValidator(final JLabel label, final JTextField textField,
            final DocumentListener inputValidator) {
        initLabelComponent(label, textField);
        textField.getDocument().addDocumentListener(inputValidator);
    }

    /**
     * Initializes label with component.
     *
     * @param label     label
     * @param component component
     */
    private void initLabelComponent(final JLabel label, final JComponent component) {
        label.setLabelFor(component);
        label.setFocusable(false);
    }

    /**
     * Performs action for button OK.
     */
    private void okAction() {
        returnStatus = DialogResult.OK;
        if (serie == null) {
            serie = new SerieTO();
        }
        serie.setCzechName(czechNameData.getText());
        serie.setOriginalName(originalNameData.getText());
        serie.setCsfd(csfdData.getText());
        serie.setImdbCode(imdbCodeLabel.isSelected() ? (Integer) imdbCodeData.getValue() : -1);
        serie.setWikiCz(wikiCzData.getText());
        serie.setWikiEn(wikiEnData.getText());
        serie.setPicture(pictureData.getText());
        serie.setNote(noteData.getText());
        serie.setGenres(genres);
        close();
    }

    /**
     * Performs action for button Cancel.
     */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        serie = null;
        close();
    }

    /**
     * Performs action for button Genres.
     */
    private void genresAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final GenreChooseDialog dialog = new GenreChooseDialog(genreFacade, new ArrayList<>(genres));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    genres.clear();
                    genres.addAll(dialog.getGenres());
                    okButton.setEnabled(isInputValid());
                }
            }

        });
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
                .addGroup(createHorizontalComponents(layout, czechNameLabel, czechNameData))
                .addGroup(createHorizontalComponents(layout, originalNameLabel, originalNameData))
                .addGroup(createHorizontalComponents(layout, csfdLabel, csfdData))
                .addGroup(createHorizontalComponents(layout, imdbCodeLabel, imdbCodeData))
                .addGroup(createHorizontalComponents(layout, wikiCzLabel, wikiCzData))
                .addGroup(createHorizontalComponents(layout, wikiEnLabel, wikiEnData))
                .addGroup(createHorizontalComponents(layout, pictureLabel, pictureData))
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData))
                .addComponent(genresButton, HORIZONTAL_GENRES_BUTTON_SIZE, HORIZONTAL_GENRES_BUTTON_SIZE, HORIZONTAL_GENRES_BUTTON_SIZE)
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
                .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, czechNameLabel, czechNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, originalNameLabel, originalNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, csfdLabel, csfdData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, imdbCodeLabel, imdbCodeData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiCzLabel, wikiCzData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiEnLabel, wikiEnData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, pictureLabel, pictureData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(genresButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
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
                .addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(data, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
    }

    /**
     * Returns true if input is valid: czech name isn't empty string, original name isn't empty string, genres isn't empty collection.
     *
     * @return true if input is valid: czech name isn't empty string, original name isn't empty string, genres isn't empty collection
     */
    private boolean isInputValid() {
        return !czechNameData.getText().isEmpty() && !originalNameData.getText().isEmpty() && !genres.isEmpty();
    }

    /**
     * Closes dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

}
