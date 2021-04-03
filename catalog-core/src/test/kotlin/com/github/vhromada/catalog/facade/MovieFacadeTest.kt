package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.impl.MovieFacadeImpl
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.MovieUtils
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

/**
 * A class represents test for class [MovieFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class MovieFacadeTest {

    /**
     * Instance of [ParentService] for movies
     */
    @Mock
    private lateinit var movieService: ParentService<com.github.vhromada.catalog.domain.Movie>

    /**
     * Instance of [ParentService] for pictures
     */
    @Mock
    private lateinit var pictureService: ParentService<com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [ParentService] for genres
     */
    @Mock
    private lateinit var genreService: ParentService<com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [Mapper] for movies
     */
    @Mock
    private lateinit var mapper: Mapper<Movie, com.github.vhromada.catalog.domain.Movie>

    /**
     * Instance of [Validator] for movies
     */
    @Mock
    private lateinit var movieValidator: Validator<Movie, com.github.vhromada.catalog.domain.Movie>

    /**
     * Instance of [Validator] for pictures
     */
    @Mock
    private lateinit var pictureValidator: Validator<Picture, com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [Validator] for genres
     */
    @Mock
    private lateinit var genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [MovieFacade]
     */
    private lateinit var facade: MovieFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = MovieFacadeImpl(
            movieService = movieService,
            pictureService = pictureService,
            genreService = genreService,
            mapper = mapper,
            movieValidator = movieValidator,
            pictureValidator = pictureValidator,
            genreValidator = genreValidator
        )
    }

    /**
     * Test method for [MovieFacade.get] with existing movie.
     */
    @Test
    fun getExistingMovie() {
        val entity = MovieUtils.newMovie(id = 1)
        val domain = MovieUtils.newMovieDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Movie>())).thenReturn(entity)
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(movieService, mapper, movieValidator)
        verifyZeroInteractions(pictureService, genreService, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.get] with not existing movie.
     */
    @Test
    fun getNotExistingMovie() {
        whenever(movieService.get(id = any())).thenReturn(Optional.empty())
        whenever(movieValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = Int.MAX_VALUE)
        verify(movieValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.update].
     */
    @Test
    fun update() {
        val entity = MovieUtils.newMovie(id = 1)
        val domain = MovieUtils.newMovieDomain(id = 1)
        val picture = PictureUtils.newPictureDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(mapper.map(source = any<Movie>())).thenReturn(domain)
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).get(id = entity.id!!)
        verify(movieService).update(data = domain)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(mapper).map(source = entity)
        verify(movieValidator).validate(data = entity, update = true)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(movieService, pictureService, genreService, mapper, movieValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.update] with invalid movie.
     */
    @Test
    fun updateInvalidMovie() {
        val entity = MovieUtils.newMovie(id = Int.MAX_VALUE)

        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieValidator).validate(data = entity, update = true)
        verifyNoMoreInteractions(movieValidator)
        verifyZeroInteractions(movieService, pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.update] with movie with invalid picture.
     */
    @Test
    fun updateInvalidPicture() {
        val entity = MovieUtils.newMovie(id = 1)
        val domain = MovieUtils.newMovieDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(pictureService.get(id = any())).thenReturn(Optional.empty())
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = entity.id!!)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(movieValidator).validate(data = entity, update = true)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(pictureValidator).validateExists(data = Optional.empty())
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(movieService, pictureService, genreService, movieValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MovieFacade.update] with movie with invalid genre.
     */
    @Test
    fun updateInvalidGenre() {
        val entity = MovieUtils.newMovie(id = 1)
        val domain = MovieUtils.newMovieDomain(id = 1)
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.empty())
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = entity.id!!)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(movieValidator).validate(data = entity, update = true)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(movieService, pictureService, genreService, movieValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MovieFacade.update] with not existing movie.
     */
    @Test
    fun updateNotExistingMovie() {
        val entity = MovieUtils.newMovie(id = Int.MAX_VALUE)
        val picture = PictureUtils.newPictureDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.empty())
        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(movieValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = entity.id!!)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(movieValidator).validate(data = entity, update = true)
        verify(movieValidator).validateExists(data = Optional.empty())
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(movieService, pictureService, genreService, movieValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MovieFacade.remove].
     */
    @Test
    fun remove() {
        val domain = MovieUtils.newMovieDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).get(id = 1)
        verify(movieService).remove(data = domain)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.remove] with invalid movie.
     */
    @Test
    fun removeInvalidMovie() {
        whenever(movieService.get(id = any())).thenReturn(Optional.empty())
        whenever(movieValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = Int.MAX_VALUE)
        verify(movieValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = MovieUtils.newMovieDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).get(id = 1)
        verify(movieService).duplicate(data = domain)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.duplicate] with invalid movie.
     */
    @Test
    fun duplicateInvalidMovie() {
        whenever(movieService.get(id = any())).thenReturn(Optional.empty())
        whenever(movieValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = Int.MAX_VALUE)
        verify(movieValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = MovieUtils.newMovieDomain(id = 1)
        val movies = listOf(domain, MovieUtils.newMovieDomain(id = 2))

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieService.getAll()).thenReturn(movies)
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(movieValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).get(id = 1)
        verify(movieService).getAll()
        verify(movieService).moveUp(data = domain)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(movieValidator).validateMovingData(data = domain, list = movies, up = true)
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.moveUp] not existing movie.
     */
    @Test
    fun moveUpNotExistingMovie() {
        val domain = MovieUtils.newMovieDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = 1)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.moveUp] with not movable movie.
     */
    @Test
    fun moveUpNotMovableMovie() {
        val domain = MovieUtils.newMovieDomain(id = 1)
        val movies = listOf(domain, MovieUtils.newMovieDomain(id = 2))

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieService.getAll()).thenReturn(movies)
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(movieValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = 1)
        verify(movieService).getAll()
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(movieValidator).validateMovingData(data = domain, list = movies, up = true)
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = MovieUtils.newMovieDomain(id = 1)
        val movies = listOf(domain, MovieUtils.newMovieDomain(id = 2))

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieService.getAll()).thenReturn(movies)
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(movieValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).get(id = 1)
        verify(movieService).getAll()
        verify(movieService).moveDown(data = domain)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(movieValidator).validateMovingData(data = domain, list = movies, up = false)
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.moveDown] with not existing movie.
     */
    @Test
    fun moveDownNotExistingMovie() {
        val domain = MovieUtils.newMovieDomain(id = 1)

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = 1)
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.moveDown] with not movable movie.
     */
    @Test
    fun moveDownNotMovableMovie() {
        val domain = MovieUtils.newMovieDomain(id = 1)
        val movies = listOf(domain, MovieUtils.newMovieDomain(id = 2))

        whenever(movieService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(movieService.getAll()).thenReturn(movies)
        whenever(movieValidator.validateExists(data = any())).thenReturn(Result())
        whenever(movieValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieService).get(id = 1)
        verify(movieService).getAll()
        verify(movieValidator).validateExists(data = Optional.of(domain))
        verify(movieValidator).validateMovingData(data = domain, list = movies, up = false)
        verifyNoMoreInteractions(movieService, movieValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).newData()
        verifyNoMoreInteractions(movieService)
        verifyZeroInteractions(pictureService, genreService, mapper, movieValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.getAll].
     */
    @Test
    fun getAll() {
        val entityList = listOf(MovieUtils.newMovie(id = 1), MovieUtils.newMovie(id = 2))
        val domainList = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(movieService.getAll()).thenReturn(domainList)
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Movie>>())).thenReturn(entityList)

        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).getAll()
        verify(mapper).mapBack(source = domainList)
        verifyNoMoreInteractions(movieService, mapper)
        verifyZeroInteractions(pictureService, genreService, movieValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.add].
     */
    @Test
    fun add() {
        val entity = MovieUtils.newMovie(id = 1)
        val domain = MovieUtils.newMovieDomain(id = 1)
        val picture = PictureUtils.newPictureDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(mapper.map(source = any<Movie>())).thenReturn(domain)
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).add(data = domain)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(mapper).map(source = entity)
        verify(movieValidator).validate(data = entity, update = false)
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(movieService, pictureService, genreService, mapper, movieValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.add] with invalid movie.
     */
    @Test
    fun addInvalidMovie() {
        val entity = MovieUtils.newMovie(id = Int.MAX_VALUE)

        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(movieValidator).validate(data = entity, update = false)
        verifyNoMoreInteractions(movieValidator)
        verifyZeroInteractions(movieService, pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.add] with movie with invalid picture.
     */
    @Test
    fun addInvalidPicture() {
        val entity = MovieUtils.newMovie(id = Int.MAX_VALUE)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(pictureService.get(id = any())).thenReturn(Optional.empty())
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(movieValidator).validate(data = entity, update = false)
        verify(pictureValidator).validateExists(data = Optional.empty())
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(pictureService, genreService, movieValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(movieService, mapper)
    }

    /**
     * Test method for [MovieFacade.add] with movie with invalid genre.
     */
    @Test
    fun addInvalidGenre() {
        val entity = MovieUtils.newMovie(id = Int.MAX_VALUE)
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.empty())
        whenever(movieValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(movieValidator).validate(data = entity, update = false)
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(pictureService, genreService, movieValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(movieService, mapper)
    }

    /**
     * Test method for [MovieFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).updatePositions()
        verifyNoMoreInteractions(movieService)
        verifyZeroInteractions(pictureService, genreService, mapper, movieValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val movie1 = MovieUtils.newMovieDomain(id = 1)
        val movie2 = MovieUtils.newMovieDomain(id = 2)
        val expectedCount = movie1.media.size + movie2.media.size

        whenever(movieService.getAll()).thenReturn(listOf(movie1, movie2))

        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedCount)
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).getAll()
        verifyNoMoreInteractions(movieService)
        verifyZeroInteractions(pictureService, genreService, mapper, movieValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [MovieFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))
        var totalLength = 0
        for (movie in movies) {
            for (medium in movie.media) {
                totalLength += medium.length
            }
        }
        val expectedTotalLength = totalLength

        whenever(movieService.getAll()).thenReturn(movies)

        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(length = expectedTotalLength))
            it.assertThat(result.events()).isEmpty()
        }

        verify(movieService).getAll()
        verifyNoMoreInteractions(movieService)
        verifyZeroInteractions(pictureService, genreService, mapper, movieValidator, pictureValidator, genreValidator)
    }

}
