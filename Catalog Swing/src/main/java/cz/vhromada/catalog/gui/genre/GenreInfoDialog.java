package cz.vhromada.catalog.gui.genre;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;

/**
 * A class represents dialog for genre.
 *
 * @author Vladimir Hromada
 */
public class GenreInfoDialog extends AbstractInfoDialog<GenreTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Label for name
     */
    private JLabel nameLabel = new JLabel("Name");

    /**
     * Text field for name
     */
    private JTextField nameData = new JTextField();

    /**
     * Creates a new instance of GenreInfoDialog.
     */
    public GenreInfoDialog() {
        init();
    }

    /**
     * Creates a new instance of GenreInfoDialog.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     */
    public GenreInfoDialog(final GenreTO genre) {
        super(genre);

        init();
        this.nameData.setText(genre.getName());
    }

    @Override
    protected void initComponents() {
        initLabelComponent(nameLabel, nameData);

        addInputValidator(nameData);
    }

    @Override
    protected GenreTO processData(final GenreTO objectData) {
        final GenreTO genre = objectData == null ? new GenreTO() : objectData;
        genre.setName(nameData.getText());

        return genre;
    }

    /**
     * Returns true if input is valid: name isn't empty string.
     *
     * @return true if input is valid: name isn't empty string
     */
    @Override
    protected boolean isInputValid() {
        return !nameData.getText().isEmpty();
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createHorizontalComponents(layout, nameLabel, nameData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createVerticalComponents(layout, nameLabel, nameData));
    }

}
