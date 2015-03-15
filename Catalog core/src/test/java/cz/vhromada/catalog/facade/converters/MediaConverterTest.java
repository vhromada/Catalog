package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.test.DeepAsserts;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link MediaConverter}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MediaConverterTest extends ObjectGeneratorTest {

    /**
     * Instance of {@link Mapper}
     */
    @Mock
    private Mapper mapper;

    /**
     * Instance of {@link MediaConverter}
     */
    private MediaConverter converter;

    /**
     * Initializes converter.
     */
    @Before
    public void setUp() {
        converter = new MediaConverter();
        converter.setMapper(mapper);
    }

    /**
     * Test method for {@link MediaConverter#convert(Object, Object, Class, Class)} with list of integer argument.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testConvertWithIntegerListArgument() {
        final Medium medium = generate(Medium.class);
        when(mapper.map(anyInt(), eq(Medium.class))).thenReturn(medium);
        final List<Integer> lengths = CollectionUtils.newList(generate(Integer.class), generate(Integer.class));

        final List<Medium> media = (List<Medium>) converter.convert(null, lengths, null, null);
        DeepAsserts.assertNotNull(media);
        assertEquals(media.size(), lengths.size());
        for (final Medium convertedMedium : media) {
            DeepAsserts.assertEquals(medium, convertedMedium);
        }

        for (final Integer length : lengths) {
            verify(mapper).map(length, Medium.class);
        }
        verifyNoMoreInteractions(mapper);
    }

    /**
     * Test method for {@link MediaConverter#convert(Object, Object, Class, Class)} with medium argument.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testConvertWithMediumArgument() {
        final Integer length = generate(Integer.class);
        when(mapper.map(any(Medium.class), eq(Integer.class))).thenReturn(length);
        final List<Medium> media = CollectionUtils.newList(generate(Medium.class), generate(Medium.class));

        final List<Integer> lengths = (List<Integer>) converter.convert(null, media, null, null);
        DeepAsserts.assertNotNull(lengths);
        assertEquals(lengths.size(), media.size());
        for (final Integer convertedLength : lengths) {
            DeepAsserts.assertEquals(length, convertedLength);
        }

        for (final Medium medium : media) {
            verify(mapper).map(medium, Integer.class);
        }
        verifyNoMoreInteractions(mapper);
    }

    /**
     * Test method for {@link MediaConverter#convert(Object, Object, Class, Class)} with null argument.
     */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null, null, null, null));

        verifyZeroInteractions(mapper);
    }

}
