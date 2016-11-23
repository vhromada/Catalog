package cz.vhromada.catalog;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Song;

/**
 * A class represents utility class for working with data.
 *
 * @author Vladimir Hromada
 */
public final class CatalogUtils {

    /**
     * Creates a new instance of CatalogUtils.
     */
    private CatalogUtils() {
    }

    /**
     * Duplicates episode.
     *
     * @param episode episode for duplication
     * @return duplicated episode
     */
    public static Episode duplicateEpisode(final Episode episode) {
        final Episode newEpisode = new Episode();
        newEpisode.setNumber(episode.getNumber());
        newEpisode.setName(episode.getName());
        newEpisode.setLength(episode.getLength());
        newEpisode.setNote(episode.getNote());
        newEpisode.setPosition(episode.getPosition());

        return newEpisode;
    }

    /**
     * Duplicates season.
     *
     * @param season season for duplication
     * @return duplicated season
     */
    public static Season duplicateSeason(final Season season) {
        final Season newSeason = new Season();
        newSeason.setNumber(season.getNumber());
        newSeason.setStartYear(season.getStartYear());
        newSeason.setEndYear(season.getEndYear());
        newSeason.setLanguage(season.getLanguage());
        newSeason.setSubtitles(new ArrayList<>(season.getSubtitles()));
        newSeason.setNote(season.getNote());
        newSeason.setPosition(season.getPosition());
        final List<Episode> newEpisodes = new ArrayList<>();
        for (final Episode episode : season.getEpisodes()) {
            newEpisodes.add(duplicateEpisode(episode));
        }
        newSeason.setEpisodes(newEpisodes);

        return newSeason;
    }

    /**
     * Duplicates song.
     *
     * @param song song for duplication
     * @return duplicated song
     */
    public static Song duplicateSong(final Song song) {
        final Song newSong = new Song();
        newSong.setName(song.getName());
        newSong.setLength(song.getLength());
        newSong.setNote(song.getNote());
        newSong.setPosition(song.getPosition());

        return newSong;
    }

}
