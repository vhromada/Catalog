package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.web.fo.EpisodeFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for episodes.
 *
 * @author Vladimir Hromada
 */
object EpisodeUtils {

    /**
     * Returns FO for episode.
     *
     * @return FO for episode
     */
    fun getEpisodeFO(): EpisodeFO {
        return EpisodeFO(id = CatalogUtils.ID,
                number = CatalogUtils.NUMBER.toString(),
                name = CatalogUtils.NAME,
                length = TimeUtils.getTimeFO(),
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns episode.
     *
     * @return episode
     */
    fun getEpisode(): Episode {
        return Episode(id = CatalogUtils.ID,
                number = CatalogUtils.NUMBER,
                name = CatalogUtils.NAME,
                length = CatalogUtils.LENGTH,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected FO for episode
     * @param actual   actual episode
     */
    fun assertEpisodeDeepEquals(expected: EpisodeFO?, actual: Episode?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.number).isEqualTo(expected.number!!.toInt())
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        TimeUtils.assertTimeDeepEquals(expected!!.length, actual!!.length)
    }

}
