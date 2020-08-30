package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.domain.Genre
import com.github.vhromada.catalog.domain.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.common.entity.Language
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import java.util.ArrayList
import javax.persistence.EntityManager

/**
 * Updates movie fields.
 *
 * @return updated movie
 */
fun com.github.vhromada.catalog.domain.Movie.updated(): com.github.vhromada.catalog.domain.Movie {
    return copy(
            czechName = "czName",
            originalName = "origName",
            year = MovieUtils.START_YEAR,
            language = Language.EN,
            subtitles = listOf(Language.CZ),
            csfd = "Csfd",
            imdbCode = 1000,
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            note = "Note",
            audit = AuditUtils.newAudit())
}

/**
 * Updates movie fields.
 *
 * @return updated movie
 */
fun Movie.updated(): Movie {
    return copy(
            czechName = "czName",
            originalName = "origName",
            year = MovieUtils.START_YEAR,
            language = Language.EN,
            subtitles = listOf(Language.CZ),
            csfd = "Csfd",
            imdbCode = 1000,
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            note = "Note")
}

/**
 * A class represents utility class for movies.
 *
 * @author Vladimir Hromada
 */
object MovieUtils {

    /**
     * Count of movies
     */
    const val MOVIES_COUNT = 3

    /**
     * Start year
     */
    const val START_YEAR = 2000

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Movie name
     */
    private const val MOVIE = "Movie "

    /**
     * Returns movies.
     *
     * @return movies
     */
    fun getMovies(): List<com.github.vhromada.catalog.domain.Movie> {
        val movies = ArrayList<com.github.vhromada.catalog.domain.Movie>()
        for (i in 0 until MOVIES_COUNT) {
            movies.add(getMovie(i + 1))
        }

        return movies
    }

    /**
     * Returns movie.
     *
     * @param id ID
     * @return movie
     */
    fun newMovieDomain(id: Int?): com.github.vhromada.catalog.domain.Movie {
        return com.github.vhromada.catalog.domain.Movie(
                id = id,
                czechName = "",
                originalName = "",
                year = 0,
                language = Language.JP,
                subtitles = emptyList(),
                media = listOf(MediumUtils.newMediumDomain(id)),
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = id,
                note = null,
                position = if (id == null) null else id - 1,
                genres = listOf(GenreUtils.newGenreDomain(id)),
                audit = null)
                .updated()
    }

    /**
     * Returns movie.
     *
     * @param id ID
     * @return movie
     */
    fun newMovie(id: Int?): Movie {
        return Movie(
                id = id,
                czechName = "",
                originalName = "",
                year = 0,
                language = Language.JP,
                subtitles = emptyList(),
                media = listOf(MediumUtils.newMedium(id)),
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = id,
                note = null,
                position = if (id == null) null else id - 1,
                genres = listOf(GenreUtils.newGenre(id)))
                .updated()
    }

    /**
     * Returns movie for index.
     *
     * @param index index
     * @return movie for index
     */
    fun getMovie(index: Int): com.github.vhromada.catalog.domain.Movie {
        val subtitles = mutableListOf<Language>()
        val media = mutableListOf<Medium>()
        val genres = mutableListOf<Genre>()
        media.add(MediumUtils.getMedium(index))
        genres.add(GenreUtils.getGenreDomain(index))
        fillData(index, subtitles, media, genres)

        return com.github.vhromada.catalog.domain.Movie(
                id = index,
                czechName = "$MOVIE$index czech name",
                originalName = "$MOVIE$index original name",
                year = START_YEAR + index,
                language = getLanguage(index),
                subtitles = subtitles,
                media = media,
                csfd = "$MOVIE$index CSFD",
                imdbCode = index,
                wikiEn = "$MOVIE$index English Wikipedia",
                wikiCz = "$MOVIE$index Czech Wikipedia",
                picture = index,
                note = if (index == 3) MOVIE + "3 note" else "",
                position = index - 1,
                genres = genres,
                audit = AuditUtils.getAudit())
    }

    /**
     * Returns language for index.
     *
     * @param index index
     * @return language for index
     */
    private fun getLanguage(index: Int): Language {
        return when (index) {
            1 -> Language.CZ
            2 -> Language.JP
            3 -> Language.FR
            else -> throw IllegalArgumentException("Bad index")
        }
    }

    /**
     * Fills data for index.
     *
     * @param index     index
     * @param subtitles subtitles
     * @param media     media
     * @param genres    genres
     */
    private fun fillData(index: Int, subtitles: MutableList<Language>, media: MutableList<Medium>, genres: MutableList<Genre>) {
        when (index) {
            1 -> {
            }
            2 -> subtitles.add(Language.EN)
            3 -> {
                subtitles.add(Language.CZ)
                subtitles.add(Language.EN)
                media.add(MediumUtils.getMedium(4))
                genres.add(GenreUtils.getGenreDomain(4))
            }
            else -> throw IllegalArgumentException("Bad index")
        }
    }

    /**
     * Returns movie.
     *
     * @param entityManager entity manager
     * @param id            movie ID
     * @return movie
     */
    fun getMovie(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Movie? {
        return entityManager.find(com.github.vhromada.catalog.domain.Movie::class.java, id)
    }

    /**
     * Returns movie with updated fields.
     *
     * @param id            movie ID
     * @param entityManager entity manager
     * @return movie with updated fields
     */
    fun updateMovie(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Movie {
        return getMovie(entityManager, id)!!
                .updated()
                .copy(position = POSITION, genres = listOf(GenreUtils.getGenreDomain(1)))
    }

    /**
     * Returns count of movies.
     *
     * @param entityManager entity manager
     * @return count of movies
     */
    @Suppress("CheckStyle")
    fun getMoviesCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Movie m", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts movies deep equals.
     *
     * @param expected expected movies
     * @param actual   actual movies
     */
    fun assertMoviesDeepEquals(expected: List<com.github.vhromada.catalog.domain.Movie?>?, actual: List<com.github.vhromada.catalog.domain.Movie?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMovieDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected movie
     * @param actual   actual movie
     */
    fun assertMovieDeepEquals(expected: com.github.vhromada.catalog.domain.Movie?, actual: com.github.vhromada.catalog.domain.Movie?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.czechName).isEqualTo(actual.czechName)
            it.assertThat(expected.originalName).isEqualTo(actual.originalName)
            it.assertThat(expected.year).isEqualTo(actual.year)
            it.assertThat(expected.language).isEqualTo(actual.language)
            it.assertThat(expected.subtitles).isEqualTo(actual.subtitles)
            MediumUtils.assertMediaDeepEquals(expected.media, actual.media)
            it.assertThat(expected.csfd).isEqualTo(actual.csfd)
            it.assertThat(expected.imdbCode).isEqualTo(actual.imdbCode)
            it.assertThat(expected.wikiEn).isEqualTo(actual.wikiEn)
            it.assertThat(expected.wikiCz).isEqualTo(actual.wikiCz)
            it.assertThat(expected.picture).isEqualTo(actual.picture)
            it.assertThat(expected.note).isEqualTo(actual.note)
            it.assertThat(expected.position).isEqualTo(actual.position)
            GenreUtils.assertGenresDeepEquals(expected.genres, actual.genres)
            AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
        }
    }

    /**
     * Asserts movies deep equals.
     *
     * @param expected expected list of To for movie
     * @param actual   actual movies
     */
    fun assertMovieListDeepEquals(expected: List<Movie?>?, actual: List<com.github.vhromada.catalog.domain.Movie?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMovieDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected movie
     * @param actual   actual movie
     */
    fun assertMovieDeepEquals(expected: Movie?, actual: com.github.vhromada.catalog.domain.Movie?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.year).isEqualTo(expected.year)
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles)
                    .hasSameSizeAs(expected.subtitles)
                    .hasSameElementsAs(expected.subtitles)
            MediumUtils.assertMediumListDeepEquals(expected.media, actual.media)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            it.assertThat(actual.imdbCode).isEqualTo(expected.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            GenreUtils.assertGenreListDeepEquals(expected.genres, actual.genres)
        }
    }

}
