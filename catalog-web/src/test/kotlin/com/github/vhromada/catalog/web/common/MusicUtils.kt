package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.web.fo.MusicFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for music.
 *
 * @author Vladimir Hromada
 */
object MusicUtils {

    /**
     * Returns FO for music.
     *
     * @return FO for music
     */
    fun getMusicFO(): MusicFO {
        return MusicFO(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                mediaCount = CatalogUtils.MEDIA.toString(),
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns music.
     *
     * @return music
     */
    fun getMusic(): Music {
        return Music(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                mediaCount = CatalogUtils.MEDIA,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected FO for music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: MusicFO?, actual: Music?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount?.toInt())
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
