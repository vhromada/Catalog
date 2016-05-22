//package cz.vhromada.catalog.commons;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import cz.vhromada.catalog.dao.entities.Episode;
//import cz.vhromada.catalog.dao.entities.Genre;
//import cz.vhromada.catalog.dao.entities.Medium;
//import cz.vhromada.catalog.dao.entities.Music;
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.catalog.dao.entities.Show;
//import cz.vhromada.catalog.dao.entities.Song;
//import cz.vhromada.generator.ObjectGenerator;
//
//import org.joda.time.DateTime;
//
///**
// * A class represents utility class for entities for Spring framework.
// *
// * @author Vladimir Hromada
// */
//public final class SpringEntitiesUtils {
//
//    /**
//     * Creates a new instance of SpringEntitiesUtils.
//     */
//    private SpringEntitiesUtils() {
//    }
//
//    /**
//     * Returns shows.
//     *
//     * @return shows
//     */
//    public static List<Show> getShows() {
//        final List<Show> shows = new ArrayList<>();
//        for (int i = 0; i < SpringUtils.SHOWS_COUNT; i++) {
//            shows.add(getShow(i + 1));
//        }
//        return shows;
//    }
//
//    /**
//     * Returns show for index.
//     *
//     * @param index index
//     * @return show for index
//     */
//    public static Show getShow(final int index) {
//        final Show show = new Show();
//        show.setId(index);
//        show.setCzechName("Show " + index + " czech name");
//        show.setOriginalName("Show " + index + " original name");
//        show.setCsfd("Show " + index + " CSFD");
//        show.setImdbCode(index * 100);
//        show.setWikiEn("Show " + index + " English Wikipedia");
//        show.setWikiCz("Show " + index + " Czech Wikipedia");
//        show.setPicture("Show " + index + " pc");
//        show.setNote(index == 2 ? "Show 2 note" : "");
//        show.setPosition(index - 1);
//        final List<Genre> genres = new ArrayList<>();
//        genres.add(getGenre(index));
//        if (index == 3) {
//            genres.add(getGenre(4));
//        }
//        show.setGenres(genres);
//
//        return show;
//    }
//
//    /**
//     * Returns new show.
//     *
//     * @param objectGenerator object generator
//     * @param entityManager   entity manager
//     * @return new show
//     */
//    public static Show newShow(final ObjectGenerator objectGenerator, final EntityManager entityManager) {
//        final Show show = objectGenerator.generate(Show.class);
//        show.setId(null);
//        show.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));
//
//        return show;
//    }
//
//    /**
//     * Returns show with updated fields.
//     *
//     * @param id              show ID
//     * @param objectGenerator object generator
//     * @param entityManager   entity manager
//     * @return show with updated fields
//     */
//    public static Show updateShow(final int id, final ObjectGenerator objectGenerator, final EntityManager entityManager) {
//        final Show show = SpringUtils.getShow(entityManager, id);
//        show.setCzechName(objectGenerator.generate(String.class));
//        show.setOriginalName(objectGenerator.generate(String.class));
//        show.setCsfd(objectGenerator.generate(String.class));
//        show.setImdbCode(objectGenerator.generate(Integer.class));
//        show.setWikiEn(objectGenerator.generate(String.class));
//        show.setWikiCz(objectGenerator.generate(String.class));
//        show.setPicture(objectGenerator.generate(String.class));
//        show.setNote(objectGenerator.generate(String.class));
//        show.setPosition(objectGenerator.generate(Integer.class));
//
//        return show;
//    }
//
//    /**
//     * Returns seasons.
//     *
//     * @param show index of show
//     * @return seasons
//     */
//    public static List<Season> getSeasons(final int show) {
//        final List<Season> seasons = new ArrayList<>();
//        for (int i = 0; i < SpringUtils.SEASONS_PER_SHOW_COUNT; i++) {
//            seasons.add(getSeason(show, i + 1));
//        }
//        return seasons;
//    }
//
//    /**
//     * Returns season for indexes.
//     *
//     * @param showIndex   show index
//     * @param seasonIndex season index
//     * @return season for indexes
//     */
//    public static Season getSeason(final int showIndex, final int seasonIndex) {
//        final Season season = new Season();
//        season.setId((showIndex - 1) * SpringUtils.SEASONS_PER_SHOW_COUNT + seasonIndex);
//        season.setNumber(seasonIndex);
//        season.setStartYear(1980 + seasonIndex);
//        season.setEndYear(seasonIndex == 3 ? 1984 : 1982);
//        season.setNote(seasonIndex == 2 ? "Show " + showIndex + " Season 2 note" : "");
//        season.setPosition(seasonIndex - 1);
//        season.setShow(getShow(showIndex));
//        final List<Language> subtitles = new ArrayList<>();
//        final Language language;
//        switch (seasonIndex) {
//            case 1:
//                language = Language.EN;
//                subtitles.add(Language.CZ);
//                subtitles.add(Language.EN);
//                break;
//            case 2:
//                language = Language.FR;
//                break;
//            case 3:
//                language = Language.JP;
//                subtitles.add(Language.EN);
//                break;
//            default:
//                throw new IllegalArgumentException("Bad season index");
//        }
//        season.setLanguage(language);
//        season.setSubtitles(subtitles);
//
//        return season;
//    }
//
//    /**
//     * Returns new season.
//     *
//     * @param objectGenerator object generator
//     * @param entityManager   entity manager
//     * @return new season
//     */
//    public static Season newSeason(final ObjectGenerator objectGenerator, final EntityManager entityManager) {
//        final Season season = objectGenerator.generate(Season.class);
//        season.setId(null);
//        season.setStartYear(objectGenerator.generate(DateTime.class).getYear());
//        season.setEndYear(objectGenerator.generate(DateTime.class).getYear());
//        season.setShow(SpringUtils.getShow(entityManager, 1));
//
//        return season;
//    }
//
//    /**
//     * Returns season with updated fields.
//     *
//     * @param id              season ID
//     * @param objectGenerator object generator
//     * @param entityManager   entity manager
//     * @return season with updated fields
//     */
//    public static Season updateSeason(final int id, final ObjectGenerator objectGenerator, final EntityManager entityManager) {
//        final Season season = SpringUtils.getSeason(entityManager, id);
//        season.setNumber(objectGenerator.generate(Integer.class));
//        season.setStartYear(objectGenerator.generate(DateTime.class).getYear());
//        season.setEndYear(objectGenerator.generate(DateTime.class).getYear());
//        season.setLanguage(objectGenerator.generate(Language.class));
//        season.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), objectGenerator.generate(Language.class)));
//        season.setNote(objectGenerator.generate(String.class));
//        season.setPosition(objectGenerator.generate(Integer.class));
//
//        return season;
//    }
//
//    /**
//     * Returns episodes.
//     *
//     * @param show   index of show
//     * @param season index of season
//     * @return episodes
//     */
//    public static List<Episode> getEpisodes(final int show, final int season) {
//        final List<Episode> episodes = new ArrayList<>();
//        for (int i = 0; i < SpringUtils.EPISODES_PER_SEASON_COUNT; i++) {
//            episodes.add(getEpisode(show, season, i + 1));
//        }
//        return episodes;
//    }
//
//    /**
//     * Returns episode for indexes.
//     *
//     * @param showIndex    show index
//     * @param seasonIndex  season index
//     * @param episodeIndex episode index
//     * @return episode for indexes
//     */
//    public static Episode getEpisode(final int showIndex, final int seasonIndex, final int episodeIndex) {
//        final Episode episode = new Episode();
//        episode.setId((showIndex - 1) * SpringUtils.EPISODES_PER_SHOW_COUNT + (seasonIndex - 1) * SpringUtils.EPISODES_PER_SEASON_COUNT + episodeIndex);
//        episode.setNumber(episodeIndex);
//        episode.setName("Show " + showIndex + " Season " + seasonIndex + " Episode " + episodeIndex);
//        episode.setLength(episodeIndex * SpringUtils.LENGTH_MULTIPLIERS[seasonIndex - 1]);
//        episode.setNote(episodeIndex == 2 ? "Show " + showIndex + " Season " + seasonIndex + " Episode 2 note" : "");
//        episode.setPosition(episodeIndex - 1);
//        episode.setSeason(getSeason(showIndex, seasonIndex));
//
//        return episode;
//    }
//
//    /**
//     * Returns new episode.
//     *
//     * @param objectGenerator object generator
//     * @param entityManager   entity manager
//     * @return new episode
//     */
//    public static Episode newEpisode(final ObjectGenerator objectGenerator, final EntityManager entityManager) {
//        final Episode episode = objectGenerator.generate(Episode.class);
//        episode.setId(null);
//        episode.setSeason(SpringUtils.getSeason(entityManager, 1));
//
//        return episode;
//    }
//
//    /**
//     * Returns episode with updated fields.
//     *
//     * @param id              episode ID
//     * @param objectGenerator object generator
//     * @param entityManager   entity manager
//     * @return episode with updated fields
//     */
//    public static Episode updateEpisode(final int id, final ObjectGenerator objectGenerator, final EntityManager entityManager) {
//        final Episode episode = SpringUtils.getEpisode(entityManager, id);
//        episode.setNumber(objectGenerator.generate(Integer.class));
//        episode.setName(objectGenerator.generate(String.class));
//        episode.setLength(objectGenerator.generate(Integer.class));
//        episode.setNote(objectGenerator.generate(String.class));
//        episode.setPosition(objectGenerator.generate(Integer.class));
//
//        return episode;
//    }
//
//}
