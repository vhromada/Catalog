package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Picture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates picture fields.
 *
 * @return updated picture
 */
fun com.github.vhromada.catalog.domain.Picture.updated(): com.github.vhromada.catalog.domain.Picture {
    return copy(content = PictureUtils.CONTENT.toByteArray())
}

/**
 * Updates picture fields.
 *
 * @return updated picture
 */
fun Picture.updated(): Picture {
    return copy(content = PictureUtils.CONTENT.toByteArray())
}

/**
 * A class represents utility class for pictures.
 *
 * @author Vladimir Hromada
 */
object PictureUtils {

    /**
     * Count of pictures
     */
    const val PICTURES_COUNT = 6

    /**
     * Picture content
     */
    const val CONTENT = "Picture"

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Returns pictures.
     *
     * @return pictures
     */
    fun getPictures(): List<com.github.vhromada.catalog.domain.Picture> {
        val pictures = mutableListOf<com.github.vhromada.catalog.domain.Picture>()
        for (i in 1..PICTURES_COUNT) {
            pictures.add(getPictureDomain(index = i))
        }

        return pictures
    }

    /**
     * Returns picture.
     *
     * @param id ID
     * @return picture
     */
    fun newPictureDomain(id: Int?): com.github.vhromada.catalog.domain.Picture {
        return com.github.vhromada.catalog.domain.Picture(id = id, content = ByteArray(0), position = if (id == null) null else id - 1)
            .updated()
    }

    /**
     * Returns picture.
     *
     * @param id ID
     * @return picture
     */
    fun newPicture(id: Int?): Picture {
        return Picture(id = id, content = null, position = if (id == null) null else id - 1)
            .updated()
    }

    /**
     * Returns picture for index.
     *
     * @param index index
     * @return picture for index
     */
    fun getPictureDomain(index: Int): com.github.vhromada.catalog.domain.Picture {
        val value = (16 + index).toString().toInt(16)

        return com.github.vhromada.catalog.domain.Picture(id = index, content = byteArrayOf(value.toByte()), position = index + 9)
            .fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns picture.
     *
     * @param entityManager entity manager
     * @param id            picture ID
     * @return picture
     */
    fun getPicture(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Picture? {
        return entityManager.find(com.github.vhromada.catalog.domain.Picture::class.java, id)
    }

    /**
     * Returns picture with updated fields.
     *
     * @param entityManager entity manager
     * @param id            picture ID
     * @return picture with updated fields
     */
    fun updatePicture(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Picture {
        val picture = getPicture(entityManager = entityManager, id = id)!!
        return picture
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = picture)
    }

    /**
     * Returns count of pictures.
     *
     * @param entityManager entity manager
     * @return count of pictures
     */
    @Suppress("JpaQlInspection")
    fun getPicturesCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Picture p", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts pictures deep equals.
     *
     * @param expected expected list of pictures
     * @param actual   actual list of pictures
     */
    fun assertDomainPicturesDeepEquals(expected: List<com.github.vhromada.catalog.domain.Picture>, actual: List<com.github.vhromada.catalog.domain.Picture>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertPictureDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    fun assertPictureDeepEquals(expected: com.github.vhromada.catalog.domain.Picture, actual: com.github.vhromada.catalog.domain.Picture) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.content).isEqualTo(expected.content)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    fun assertPictureDeepEquals(expected: Picture, actual: com.github.vhromada.catalog.domain.Picture) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.content).isEqualTo(expected.content)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
    }

    /**
     * Asserts pictures deep equals.
     *
     * @param expected expected list of pictures
     * @param actual   actual list of pictures
     */
    fun assertPictureListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Picture>, actual: List<Picture>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertPictureDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    fun assertPictureDeepEquals(expected: com.github.vhromada.catalog.domain.Picture, actual: Picture) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.content).isEqualTo(expected.content)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
