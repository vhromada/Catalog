package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link Music} and {@link MusicTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class MusicConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertMusic() {
        final Music music = MusicUtils.newMusic(1);
        final MusicTO musicTO = converter.convert(music, MusicTO.class);

        MusicUtils.assertMusicDeepEquals(musicTO, music);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertMusic_NullArgument() {
        assertNull(converter.convert(null, MusicTO.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertMusicTO() {
        final MusicTO musicTO = MusicUtils.newMusicTO(1);
        final Music music = converter.convert(musicTO, Music.class);

        assertNotNull(music);
        MusicUtils.assertMusicDeepEquals(musicTO, music);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertMusicTO_NullArgument() {
        assertNull(converter.convert(null, Music.class));
    }

}
