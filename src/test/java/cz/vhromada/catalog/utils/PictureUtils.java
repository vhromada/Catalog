package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Picture;

/**
 * A class represents utility class for pictures.
 *
 * @author Vladimir Hromada
 */
public final class PictureUtils {

    /**
     * Count of pictures
     */
    public static final int PICTURES_COUNT = 6;

    /**
     * ID
     */
    public static final int ID = 1;

    /**
     * Position
     */
    public static final int POSITION = 10;

    /**
     * Picture content
     */
    private static final String CONTENT = "Picture";

    /**
     * Creates a new instance of PictureUtils.
     */
    private PictureUtils() {
    }

    /**
     * Returns picture.
     *
     * @param id ID
     * @return picture
     */
    public static cz.vhromada.catalog.domain.Picture newPictureDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Picture picture = new cz.vhromada.catalog.domain.Picture();
        updatePicture(picture);
        if (id != null) {
            picture.setId(id);
            picture.setPosition(id - 1);
        } else {
            picture.setPosition(0);
        }

        return picture;
    }

    /**
     * Updates picture fields.
     *
     * @param picture picture
     */
    public static void updatePicture(final cz.vhromada.catalog.domain.Picture picture) {
        picture.setContent(CONTENT.getBytes());
    }

    /**
     * Returns picture.
     *
     * @param id ID
     * @return picture
     */
    public static Picture newPicture(final Integer id) {
        final Picture picture = new Picture();
        updatePicture(picture);
        if (id != null) {
            picture.setId(id);
            picture.setPosition(id - 1);
        } else {
            picture.setPosition(0);
        }

        return picture;
    }

    /**
     * Updates picture fields.
     *
     * @param picture picture
     */
    public static void updatePicture(final Picture picture) {
        picture.setContent(CONTENT.getBytes());
    }

    /**
     * Returns pictures.
     *
     * @return pictures
     */
    public static List<cz.vhromada.catalog.domain.Picture> getPictures() {
        final List<cz.vhromada.catalog.domain.Picture> pictures = new ArrayList<>();
        for (int i = 0; i < PICTURES_COUNT; i++) {
            pictures.add(getPicture(i + 1));
        }

        return pictures;
    }

    /**
     * Returns picture for index.
     *
     * @param index index
     * @return picture for index
     */
    public static cz.vhromada.catalog.domain.Picture getPicture(final int index) {
        final Integer value = Integer.parseInt(String.valueOf(16 + index), 16);

        final cz.vhromada.catalog.domain.Picture picture = new cz.vhromada.catalog.domain.Picture();
        picture.setId(index);
        picture.setContent(new byte[]{ value.byteValue() });
        picture.setPosition(index - 1);

        return picture;
    }

    /**
     * Returns picture.
     *
     * @param entityManager entity manager
     * @param id            picture ID
     * @return picture
     */
    public static cz.vhromada.catalog.domain.Picture getPicture(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Picture.class, id);
    }

    /**
     * Returns picture with updated fields.
     *
     * @param entityManager entity manager
     * @param id            picture ID
     * @return picture with updated fields
     */
    public static cz.vhromada.catalog.domain.Picture updatePicture(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Picture picture = getPicture(entityManager, id);
        updatePicture(picture);
        picture.setPosition(POSITION);

        return picture;
    }

    /**
     * Returns count of pictures.
     *
     * @param entityManager entity manager
     * @return count of pictures
     */
    public static int getPicturesCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Picture g", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts pictures deep equals.
     *
     * @param expected expected pictures
     * @param actual   actual pictures
     */
    public static void assertPicturesDeepEquals(final List<cz.vhromada.catalog.domain.Picture> expected,
        final List<cz.vhromada.catalog.domain.Picture> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertPictureDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    public static void assertPictureDeepEquals(final cz.vhromada.catalog.domain.Picture expected, final cz.vhromada.catalog.domain.Picture actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getContent()).isEqualTo(actual.getContent());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
        });
    }

    /**
     * Asserts pictures deep equals.
     *
     * @param expected expected list of picture
     * @param actual   actual pictures
     */
    public static void assertPictureListDeepEquals(final List<Picture> expected, final List<cz.vhromada.catalog.domain.Picture> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertPictureDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    public static void assertPictureDeepEquals(final Picture expected, final cz.vhromada.catalog.domain.Picture actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getContent()).isEqualTo(expected.getContent());
            softly.assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        });
    }

}
