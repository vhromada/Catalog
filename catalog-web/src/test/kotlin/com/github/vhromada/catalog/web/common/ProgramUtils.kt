package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.web.fo.ProgramFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for programs.
 *
 * @author Vladimir Hromada
 */
object ProgramUtils {

    /**
     * Returns FO for program.
     *
     * @return FO for program
     */
    fun getProgramFO(): ProgramFO {
        return ProgramFO(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                mediaCount = CatalogUtils.MEDIA.toString(),
                crack = true,
                serialKey = true,
                otherData = "Other data",
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns program.
     *
     * @return program
     */
    fun getProgram(): Program {
        return Program(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                mediaCount = CatalogUtils.MEDIA,
                crack = true,
                serialKey = true,
                otherData = "Other data",
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected FO for program
     * @param actual   actual program
     */
    fun assertProgramDeepEquals(expected: ProgramFO?, actual: Program?) {
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
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
