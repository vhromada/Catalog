package cz.vhromada.catalog.gui.game;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.WebPageButtonType;

/**
 * A class represents panel with game's data.
 *
 * @author Vladimir Hromada
 */
public class GameDataPanel extends AbstractDataPanel<GameTO> {

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
     * Button for showing game's czech Wikipedia page
     */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /**
     * Button for showing game's english Wikipedia page
     */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /**
     * URL to czech Wikipedia page about game
     */
    private String wikiCz;

    /**
     * URL to english Wikipedia page about game
     */
    private String wikiEn;

    /**
     * Creates a new instance of GameDataPanel.
     *
     * @param game TO for game
     * @throws IllegalArgumentException if TO for game is null
     */
    public GameDataPanel(final GameTO game) {
        updateData(game);

        initData(nameLabel, nameData);
        initData(dataLabel, dataData);
        initData(mediaCountLabel, mediaCountData);
        initData(noteLabel, noteData);

        initButton(wikiCzButton, WebPageButtonType.WIKI_CZ);
        initButton(wikiEnButton, WebPageButtonType.WIKI_EN);

        createLayout();
    }

    @Override
    protected void updateComponentData(final GameTO data) {
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
        throw new IllegalStateException("Getting URL to ČSFD page is not allowed for games.");
    }

    @Override
    protected int getImdbUrl() {
        throw new IllegalStateException("Getting URL to IMDB page is not allowed for games.");
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
     * @param game TO for game
     * @return additional data
     */
    private static String getAdditionalData(final GameTO game) {
        final StringBuilder result = new StringBuilder();
        if (game.hasCrack()) {
            result.append("Crack");
        }
        addToResult(result, game.hasSerialKey(), "serial key");
        addToResult(result, game.hasPatch(), "patch");
        addToResult(result, game.hasTrainer(), "trainer");
        addToResult(result, game.hasTrainerData(), "data for trainer");
        addToResult(result, game.hasEditor(), "editor");
        addToResult(result, game.haveSaves(), "saves");
        if (game.getOtherData() != null && !game.getOtherData().isEmpty()) {
            if (result.length() != 0) {
                result.append(", ");
            }
            result.append(game.getOtherData());
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
