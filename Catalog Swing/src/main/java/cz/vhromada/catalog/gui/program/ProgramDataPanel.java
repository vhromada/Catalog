package cz.vhromada.catalog.gui.program;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.WebPageButtonType;

/**
 * A class represents panel with program's data.
 *
 * @author Vladimir Hromada
 */
public class ProgramDataPanel extends AbstractDataPanel<ProgramTO> {

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
     * Label for additional data
     */
    private JLabel dataLabel = new JLabel("Additional data");

    /**
     * Label with additional data
     */
    private JLabel dataData = new JLabel();

    /**
     * Label for count of media
     */
    private JLabel mediaCountLabel = new JLabel("Count of media");

    /**
     * Label with count of media
     */
    private JLabel mediaCountData = new JLabel();

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Label with note
     */
    private JLabel noteData = new JLabel();

    /**
     * Button for showing program's czech Wikipedia page
     */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /**
     * Button for showing program's english Wikipedia page
     */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /**
     * URL to czech Wikipedia page about program
     */
    private String wikiCz;

    /**
     * URL to english Wikipedia page about program
     */
    private String wikiEn;

    /**
     * Creates a new instance of ProgramDataPanel.
     *
     * @param program TO for program
     * @throws IllegalArgumentException if TO for program is null
     */
    public ProgramDataPanel(final ProgramTO program) {
        updateData(program);

        initData(nameLabel, nameData);
        initData(dataLabel, dataData);
        initData(mediaCountLabel, mediaCountData);
        initData(noteLabel, noteData);

        initButton(wikiCzButton, WebPageButtonType.WIKI_CZ);
        initButton(wikiEnButton, WebPageButtonType.WIKI_EN);

        createLayout();
    }

    @Override
    protected void updateComponentData(final ProgramTO data) {
        nameData.setText(data.getName());
        dataData.setText(getAdditionalData(data));
        mediaCountData.setText(Integer.toString(data.getMediaCount()));
        noteData.setText(data.getNote());

        wikiCz = data.getWikiCz();
        wikiEn = data.getWikiEn();

        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
    }

    @Override
    protected String getCzWikiUrl() {
        return wikiCz;
    }

    @Override
    protected String getEnWikiUrl() {
        return wikiEn;
    }

    @Override
    protected String getCsfdUrl() {
        throw new IllegalStateException("Getting URL to ČSFD page is not allowed for programs.");
    }

    @Override
    protected int getImdbUrl() {
        throw new IllegalStateException("Getting URL to IMDB page is not allowed for programs.");
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createHorizontalDataComponents(layout, nameLabel, nameData))
                .addGroup(createHorizontalDataComponents(layout, dataLabel, dataData))
                .addGroup(createHorizontalDataComponents(layout, mediaCountLabel, mediaCountData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
                .addGroup(createHorizontalButtons(layout, wikiCzButton, wikiEnButton));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, dataLabel, dataData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, mediaCountLabel, mediaCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalButtons(layout, wikiCzButton, wikiEnButton));
    }

    /**
     * Returns additional data.
     *
     * @param program TO for program
     * @return additional data
     */
    private static String getAdditionalData(final ProgramTO program) {
        final StringBuilder result = new StringBuilder();
        if (program.hasCrack()) {
            result.append("Crack");
        }
        addToResult(result, program.hasSerialKey(), "serial key");
        if (program.getOtherData() != null && !program.getOtherData().isEmpty()) {
            if (result.length() != 0) {
                result.append(", ");
            }
            result.append(program.getOtherData());
        }

        return result.toString();
    }

    /**
     * Adds data to result.
     *
     * @param result result
     * @param value  value
     * @param data   data
     */
    private static void addToResult(final StringBuilder result, final boolean value, final String data) {
        if (value) {
            if (result.length() == 0) {
                result.append(data.substring(0, 1).toUpperCase());
                result.append(data.substring(1));
            } else {
                result.append(", ");
                result.append(data);
            }
        }
    }

}
