package cz.vhromada.catalog.gui.music;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.validators.Validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents panel with music data.
 *
 * @author Vladimir Hromada
 */
public class MusicDataPanel extends JPanel {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(MusicDataPanel.class);

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size */
    private static final int HORIZONTAL_LABEL_SIZE = 150;

    /** Horizontal data size */
    private static final int HORIZONTAL_DATA_SIZE = 600;

    /** Horizontal button size */
    private static final int HORIZONTAL_BUTTON_SIZE = 120;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 10;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Facade for songs */
    private SongFacade songFacade;

    /** Label for name */
    private JLabel nameLabel = new JLabel("Name");

    /** Label with name */
    private JLabel nameData = new JLabel();

    /** Label for count of media */
    private JLabel mediaCountLabel = new JLabel("Count of media");

    /** Label with count of media */
    private JLabel mediaCountData = new JLabel();

    /** Label for count of songs */
    private JLabel songsCountLabel = new JLabel("Count of songs");

    /** Label with count of songs */
    private JLabel songsCountData = new JLabel();

    /** Label for total length */
    private JLabel totalLengthLabel = new JLabel("Total length");

    /** Label with total length */
    private JLabel totalLengthData = new JLabel();

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Label with note */
    private JLabel noteData = new JLabel();

    /** Button for showing music czech Wikipedia page */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /** Button for showing music english Wikipedia page */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /** URL to czech Wikipedia page about music */
    private String wikiCz;

    /** URL to english Wikipedia page about music */
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
        Validators.validateArgumentNotNull(music, "TO for music");
        Validators.validateArgumentNotNull(songFacade, "Facade for songs");

        this.songFacade = songFacade;

        initData(nameLabel, nameData, music.getName());
        initData(mediaCountLabel, mediaCountData, Integer.toString(music.getMediaCount()));
        initData(songsCountLabel, songsCountData, getSongsCount(music));
        initData(totalLengthLabel, totalLengthData, getMusicLength(music));
        initData(noteLabel, noteData, music.getNote());

        wikiCz = music.getWikiCz();
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiCzButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final String url = "http://cs.wikipedia.org/wiki/" + wikiCz;
                try {
                    Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
                } catch (final IOException ex) {
                    logger.error("Error in showing czech Wikipedia page.", ex);
                }
            }

        });

        wikiEn = music.getWikiEn();
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
        wikiEnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final String url = "http://en.wikipedia.org/wiki/" + wikiEn;
                try {
                    Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
                } catch (final IOException ex) {
                    logger.error("Error in showing english Wikipedia page.", ex);
                }
            }

        });

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Updates TO for music.
     *
     * @param music TO for music
     * @throws IllegalArgumentException if TO for music is null
     */
    public void updateMusic(final MusicTO music) {
        Validators.validateArgumentNotNull(music, "TO for music");

        nameData.setText(music.getName());
        mediaCountData.setText(Integer.toString(music.getMediaCount()));
        songsCountData.setText(getSongsCount(music));
        totalLengthData.setText(getMusicLength(music));
        noteData.setText(music.getNote());
        wikiCz = music.getWikiCz();
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiEn = music.getWikiEn();
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
    }

    /**
     * Initializes data.
     *
     * @param label label
     * @param data  data
     * @param text  text for data
     */
    private static void initData(final JLabel label, final JLabel data, final String text) {
        label.setFocusable(false);
        label.setLabelFor(data);
        data.setText(text);
        data.setFocusable(false);
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
        for (SongTO song : songs) {
            totalLength += song.getLength();
        }
        return new Time(totalLength).toString();
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addComponent(wikiCzButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(wikiEnButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);

        final GroupLayout.Group components = layout.createParallelGroup()
                .addGroup(createHorizontalDataComponents(layout, nameLabel, nameData))
                .addGroup(createHorizontalDataComponents(layout, mediaCountLabel, mediaCountData))
                .addGroup(createHorizontalDataComponents(layout, songsCountLabel, songsCountData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
                .addGroup(buttons);

        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(components)
                .addGap(HORIZONTAL_GAP_SIZE);

    }

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout layout
     * @param label  label
     * @param data   data
     * @return horizontal layout for label component with data component
     */
    private GroupLayout.Group createHorizontalDataComponents(final GroupLayout layout, final JLabel label, final JLabel data) {
        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(data, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(wikiCzButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(wikiEnButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(5)
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
                .addGroup(buttons)
                .addGap(5);
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

}
