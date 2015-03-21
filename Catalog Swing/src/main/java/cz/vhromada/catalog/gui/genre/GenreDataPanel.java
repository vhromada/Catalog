package cz.vhromada.catalog.gui.genre;

import javax.swing.GroupLayout;
import javax.swing.JLabel;

import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;

/**
 * A class represents panel with genre's data.
 *
 * @author Vladimir Hromada
 */
public class GenreDataPanel extends AbstractDataPanel<GenreTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Label for name
     */
    private JLabel nameLabel = new JLabel("Name");

    /**
     * Label with name
     */
    private JLabel nameData = new JLabel();

    /**
     * Creates a new instance of GenreDataPanel.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     */
    public GenreDataPanel(final GenreTO genre) {
        updateData(genre);

        initData(nameLabel, nameData);

        createLayout();
    }

    @Override
    protected void updateComponentData(final GenreTO data) {
        nameData.setText(data.getName());
    }

    @Override
    protected String getCzWikiUrl() {
        throw new IllegalStateException("Getting URL to czech Wikipedia page is not allowed for genres.");
    }

    @Override
    protected String getEnWikiUrl() {
        throw new IllegalStateException("Getting URL to english Wikipedia page is not allowed for genres.");
    }

    @Override
    protected String getCsfdUrl() {
        throw new IllegalStateException("Getting URL to ČSFD page is not allowed for genres.");
    }

    @Override
    protected int getImdbUrl() {
        throw new IllegalStateException("Getting URL to IMDB page is not allowed for genres.");
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createHorizontalDataComponents(layout, nameLabel, nameData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createVerticalComponents(layout, nameLabel, nameData));
    }

}
