package cz.vhromada.catalog.gui.genre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for choosing genre.
 *
 * @author Vladimir Hromada
 */
public class GenreChooseDialog extends JDialog {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

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
     * List of TO for genre
     */
    private List<GenreTO> genres;

    /**
     * List with genres
     */
    private JList<String> list = new JList<>();

    /**
     * ScrollPane for list with genres
     */
    private JScrollPane listScrollPane = new JScrollPane(list);

    /**
     * Button OK
     */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /**
     * Button Cancel
     */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /**
     * Data model for list for choosing genres
     */
    private GenreChooseListDataModel genreChooseListDataModel;

    /**
     * Creates a new instance of GenreChooseDialog.
     *
     * @param genreFacade facade for genres
     * @param genres      list of TO for genre
     * @throws IllegalArgumentException if facade for genres is null
     *                                  or list of TO for genre is null
     */
    public GenreChooseDialog(final GenreFacade genreFacade, final List<GenreTO> genres) {
        super(new JFrame(), "Choose", true);

        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");
        Validators.validateArgumentNotNull(genres, "List of TO for genre");

        this.genreFacade = genreFacade;
        this.genres = genres;
        initComponents();
        setIconImage(Picture.CHOOSE.getIcon().getImage());
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
     * Returns list of TO for genre.
     *
     * @return list of TO for genre
     * @throws IllegalStateException if list of TO for genre for hasn't been set
     */
    public List<GenreTO> getGenres() {
        Validators.validateFieldNotNull(genres, "List of TO for genre");

        return genres;
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        genreChooseListDataModel = new GenreChooseListDataModel(genreFacade);
        list.setModel(genreChooseListDataModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndices(getSelectedIndexes());
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                selectionChangeAction();
            }

        });

        okButton.setEnabled(list.getSelectedIndices().length > 0);
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
     * Performs action for button OK.
     */
    private void okAction() {
        returnStatus = DialogResult.OK;
        genres.clear();
        final int[] indexes = list.getSelectedIndices();
        for (int index : indexes) {
            genres.add(genreChooseListDataModel.getGenreAt(index));
        }
        close();
    }

    /**
     * Performs action for button Cancel.
     */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        genres = null;
        close();
    }

    /**
     * Performs action for selection change.
     */
    private void selectionChangeAction() {
        final int[] indexes = list.getSelectedIndices();
        okButton.setEnabled(indexes.length > 0);
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


        final GroupLayout.Group components = layout.createParallelGroup()
                .addComponent(listScrollPane, 310, 310, 310)
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

        return layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addComponent(listScrollPane, 300, 300, 300)
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(buttons)
                .addGap(VERTICAL_LONG_GAP_SIZE);
    }

    /**
     * Closes dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

    /**
     * Returns selected indexes.
     *
     * @return selected indexes
     */
    private int[] getSelectedIndexes() {
        final List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genreChooseListDataModel.getSize(); i++) {
            if (genres.contains(genreChooseListDataModel.getGenreAt(i))) {
                indexes.add(i);
            }
        }

        final int[] result = new int[indexes.size()];
        for (int i = 0; i < indexes.size(); i++) {
            result[i] = indexes.get(i);
        }

        return result;
    }

}
