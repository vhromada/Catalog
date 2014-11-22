package cz.vhromada.catalog.facade.to;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link MovieTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testGeneratorContext.xml")
public class MovieTOTest {

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Instance of {@link MovieTO} */
    private MovieTO movie;

    /** Initializes TO for movie. */
    @Before
    public void setUp() {
        movie = new MovieTO();
    }

    /** Test method for {@link MovieTO#getSubtitlesAsString()}. */
    @Test
    public void testSubtitlesAsString() {
        final Language subtitles1 = objectGenerator.generate(Language.class);
        final Language subtitles2 = objectGenerator.generate(Language.class);
        final Language subtitles3 = objectGenerator.generate(Language.class);
        final String subtitleDivider = " / ";

        DeepAsserts.assertEquals("", movie.getSubtitlesAsString());

        movie.setSubtitles(CollectionUtils.newList(subtitles1));
        DeepAsserts.assertEquals(subtitles1.toString(), movie.getSubtitlesAsString());

        movie.setSubtitles(CollectionUtils.newList(subtitles1, subtitles2));
        DeepAsserts.assertEquals(subtitles1 + subtitleDivider + subtitles2, movie.getSubtitlesAsString());

        movie.setSubtitles(CollectionUtils.newList(subtitles1, subtitles2, subtitles3));
        DeepAsserts.assertEquals(subtitles1 + subtitleDivider + subtitles2 + subtitleDivider + subtitles3, movie.getSubtitlesAsString());
    }

    /** Test method for {@link MovieTO#getTotalLength()}. */
    @Test
    public void testGetTotalLength() {
        final int medium1 = objectGenerator.generate(Integer.class);
        final int medium2 = objectGenerator.generate(Integer.class);

        DeepAsserts.assertEquals(0, movie.getTotalLength());

        movie.setMedia(CollectionUtils.newList(medium1));
        DeepAsserts.assertEquals(medium1, movie.getTotalLength());

        movie.setMedia(CollectionUtils.newList(medium1, medium2));
        DeepAsserts.assertEquals(medium1 + medium2, movie.getTotalLength());
    }

    /** Test method for {@link MovieTO#getGenresAsString()}. */
    @Test
    public void testGenresAsString() {
        final GenreTO genre1 = objectGenerator.generate(GenreTO.class);
        final GenreTO genre2 = objectGenerator.generate(GenreTO.class);
        final GenreTO genre3 = objectGenerator.generate(GenreTO.class);

        DeepAsserts.assertEquals("", movie.getGenresAsString());

        movie.setGenres(CollectionUtils.newList(genre1));
        DeepAsserts.assertEquals(genre1.getName(), movie.getGenresAsString());

        movie.setGenres(CollectionUtils.newList(genre1, genre2));
        DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName(), movie.getGenresAsString());

        movie.setGenres(CollectionUtils.newList(genre1, genre2, genre3));
        DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName() + ", " + genre3.getName(), movie.getGenresAsString());
    }

}
