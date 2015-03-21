package cz.vhromada.catalog.gui.music;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.WebPageButtonType;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with music data.
 *
 * @author Vladimir Hromada
 */
public class MusicDataPanel extends AbstractDataPanel<MusicTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for songs
     */
    private SongFacade songFacade;

    /**
     * Label for name
     */
    private JLabel nameLabel = new JLabel("Name");

    /**
     * Label with name
     */
    private JLabel nameData = new JLabel();

    /**
     * Label for count of media
     */
    private JLabel mediaCountLabel = new JLabel("Count of media");

    /**
     * Label with count of media
     */
    private JLabel mediaCountData = new JLabel();

    /**
     * Label for count of songs
     */
    private JLabel songsCountLabel = new JLabel("Count of songs");

    /**
     * Label with count of songs
     */
    private JLabel songsCountData = new JLabel();

    /**
     * Label for total length
     */
    private JLabel totalLengthLabel = new JLabel("Total length");

    /**
     * Label with total length
     */
    private JLabel totalLengthData = new JLabel();

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Label with note
     */
    private JLabel noteData = new JLabel();

    /**
     * Button for showing music czech Wikipedia page
     */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /**
     * Button for showing music english Wikipedia page
     */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /**
     * URL to czech Wikipedia page about music
     */
    private String wikiCz;

    /**
     * URL to english Wikipedia page about music
     */
    private String wikiEn;

    /**
     * Creates a new instance of MusicDataPanel.
     *
     * @param music      TO for music
     * @param songFacade facade for songs
     * @throws IllegalArgumentException if TO for music is null
     *                                  or facade for songs is null
     */
    public MusicDataPanel(final MusicTO music, final SongFacade songFacade) {
        Validators.validateArgumentNotNull(songFacade, "Facade for songs");

        this.songFacade = songFacade;

        updateData(music);

        initData(nameLabel, nameData);
        initData(mediaCountLabel, mediaCountData);
        initData(songsCountLabel, songsCountData);
        initData(totalLengthLabel, totalLengthData);
        initData(noteLabel, noteData);

        initButton(wikiCzButton, WebPageButtonType.WIKI_CZ);
        initButton(wikiEnButton, WebPageButtonType.WIKI_EN);

        createLayout();
    }

    @Override
    protected void updateComponentData(final MusicTO data) {
        nameData.setText(data.getName());
        mediaCountData.setText(Integer.toString(data.getMediaCount()));
        songsCountData.setText(getSongsCount(data));
        totalLengthData.setText(getMusicLength(data));
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
        throw new IllegalStateException("Getting URL to ČSFD page is not allowed for music.");
    }

    @Override
    protected int getImdbUrl() {
        throw new IllegalStateException("Getting URL to IMDB page is not allowed for music.");
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createHorizontalDataComponents(layout, nameLabel, nameData))
                .addGroup(createHorizontalDataComponents(layout, mediaCountLabel, mediaCountData))
                .addGroup(createHorizontalDataComponents(layout, songsCountLabel, songsCountData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
                .addGroup(createHorizontalButtons(layout, wikiCzButton, wikiEnButton));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, mediaCountLabel, mediaCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, songsCountLabel, songsCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, totalLengthLabel, totalLengthData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalButtons(layout, wikiCzButton, wikiEnButton));
    }

    /**
     * Returns count of music songs.
     *
     * @param music TO for music
     * @return count of music songs
     */
    private String getSongsCount(final MusicTO music) {
        final List<SongTO> songs = songFacade.findSongsByMusic(music);
        return Integer.toString(songs.size());
    }

    /**
     * Returns total length of all music songs.
     *
     * @param music TO for music
     * @return total length of all music songs
     */
    private String getMusicLength(final MusicTO music) {
        final List<SongTO> songs = songFacade.findSongsByMusic(music);
        int totalLength = 0;
        for (final SongTO song : songs) {
            totalLength += song.getLength();
        }
        return new Time(totalLength).toString();
    }

}
