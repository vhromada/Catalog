package cz.vhromada.catalog.utils

import cz.vhromada.catalog.entity.Picture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates picture fields.
 *
 * @return updated picture
 */
fun cz.vhromada.catalog.domain.Picture.updated(): cz.vhromada.catalog.domain.Picture {
    return copy(content = PictureUtils.CONTENT.toByteArray(), audit = AuditUtils.newAudit())
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
    fun getPictures(): List<cz.vhromada.catalog.domain.Picture> {
        val pictures = mutableListOf<cz.vhromada.catalog.domain.Picture>()
        for (i in 0 until PICTURES_COUNT) {
            pictures.add(getPicture(i + 1))
        }

        return pictures
    }

    /**
     * Returns picture.
     *
     * @param id ID
     * @return picture
     */
    fun newPictureDomain(id: Int?): cz.vhromada.catalog.domain.Picture {
        return cz.vhromada.catalog.domain.Picture(id = id, content = ByteArray(0), position = if (id == null) null else id - 1, audit = null)
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
    fun getPicture(index: Int): cz.vhromada.catalog.domain.Picture {
        val value = Integer.parseInt((16 + index).toString(), 16)

        return cz.vhromada.catalog.domain.Picture(id = index, content = byteArrayOf(value.toByte()), position = index - 1, audit = AuditUtils.getAudit())
    }

    /**
     * Returns picture.
     *
     * @param entityManager entity manager
     * @param id            picture ID
     * @return picture
     */
    fun getPicture(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Picture? {
        return entityManager.find(cz.vhromada.catalog.domain.Picture::class.java, id)
    }

    /**
     * Returns picture with updated fields.
     *
     * @param entityManager entity manager
     * @param id            picture ID
     * @return picture with updated fields
     */
    fun updatePicture(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Picture {
        return getPicture(entityManager, id)!!
                .updated()
                .copy(position = POSITION)
    }

    /**
     * Returns count of pictures.
     *
     * @param entityManager entity manager
     * @return count of pictures
     */
    @Suppress("CheckStyle")
    fun getPicturesCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Picture g", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts pictures deep equals.
     *
     * @param expected expected pictures
     * @param actual   actual pictures
     */
    fun assertPicturesDeepEquals(expected: List<cz.vhromada.catalog.domain.Picture?>?, actual: List<cz.vhromada.catalog.domain.Picture?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertPictureDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    fun assertPictureDeepEquals(expected: cz.vhromada.catalog.domain.Picture?, actual: cz.vhromada.catalog.domain.Picture?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.content).isEqualTo(actual.content)
            it.assertThat(expected.position).isEqualTo(actual.position)
            AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
        }
    }

    /**
     * Asserts pictures deep equals.
     *
     * @param expected expected list of picture
     * @param actual   actual pictures
     */
    fun assertPictureListDeepEquals(expected: List<Picture?>?, actual: List<cz.vhromada.catalog.domain.Picture?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertPictureDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts picture deep equals.
     *
     * @param expected expected picture
     * @param actual   actual picture
     */
    fun assertPictureDeepEquals(expected: Picture?, actual: cz.vhromada.catalog.domain.Picture?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.content).isEqualTo(expected.content)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
