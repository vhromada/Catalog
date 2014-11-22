package cz.vhromada.catalog.facade.to;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link SerieTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testGeneratorContext.xml")
public class SerieTOTest {

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Test method for {@link SerieTO#getGenresAsString()}. */
    @Test
    public void testGenresAsString() {
        final SerieTO serie = new SerieTO();
        final GenreTO genre1 = objectGenerator.generate(GenreTO.class);
        final GenreTO genre2 = objectGenerator.generate(GenreTO.class);
        final GenreTO genre3 = objectGenerator.generate(GenreTO.class);

        DeepAsserts.assertEquals("", serie.getGenresAsString());

        serie.setGenres(CollectionUtils.newList(genre1));
        DeepAsserts.assertEquals(genre1.getName(), serie.getGenresAsString());

        serie.setGenres(CollectionUtils.newList(genre1, genre2));
        DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName(), serie.getGenresAsString());

        serie.setGenres(CollectionUtils.newList(genre1, genre2, genre3));
        DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName() + ", " + genre3.getName(), serie.getGenresAsString());
    }

}
