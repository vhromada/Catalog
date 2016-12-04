package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Music} and {@link Music}.
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
        final cz.vhromada.catalog.domain.Music music = MusicUtils.newMusic(1);
        final Music musicTO = converter.convert(music, Music.class);

        MusicUtils.assertMusicDeepEquals(musicTO, music);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertMusic_NullArgument() {
        assertNull(converter.convert(null, Music.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertMusicTO() {
        final Music musicTO = MusicUtils.newMusicTO(1);
        final cz.vhromada.catalog.domain.Music music = converter.convert(musicTO, cz.vhromada.catalog.domain.Music.class);

        assertNotNull(music);
        MusicUtils.assertMusicDeepEquals(musicTO, music);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertMusicTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Music.class));
    }

}
