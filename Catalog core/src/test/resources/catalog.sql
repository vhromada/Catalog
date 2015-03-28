DROP TABLE book_languages;
DROP TABLE books;
DROP TABLE book_categories;
DROP TABLE programs;
DROP TABLE songs;
DROP TABLE music;
DROP TABLE games;
DROP TABLE episodes;
DROP TABLE season_subtitles;
DROP TABLE seasons;
DROP TABLE serie_genres;
DROP TABLE series;
DROP TABLE movie_subtitles;
DROP TABLE movie_media;
DROP TABLE movie_genres;
DROP TABLE movies;
DROP TABLE media;
DROP TABLE genres;

CREATE TABLE genres (
  id         INTEGER      NOT NULL CONSTRAINT genres_pk PRIMARY KEY,
  genre_name VARCHAR(200) NOT NULL CONSTRAINT genres_genre_name_ck CHECK (LENGTH(genre_name) > 0)
);

CREATE TABLE media (
  id            INTEGER NOT NULL CONSTRAINT media_pk PRIMARY KEY,
  medium_number INTEGER NOT NULL CONSTRAINT media_medium_number_ck CHECK (medium_number > 0),
  medium_length INTEGER NOT NULL CONSTRAINT media_medium_length_ck CHECK (medium_length >= 0)
);

CREATE TABLE movies (
  id             INTEGER      NOT NULL CONSTRAINT movies_pk PRIMARY KEY,
  czech_name     VARCHAR(200) NOT NULL CONSTRAINT movies_czech_name_ck CHECK (LENGTH(czech_name) > 0),
  original_name  VARCHAR(100) NOT NULL CONSTRAINT movies_original_name_ck CHECK (LENGTH(original_name) > 0),
  movie_year     INTEGER      NOT NULL CONSTRAINT movies_movie_year_ck CHECK (movie_year BETWEEN 1930 AND 2100),
  movie_language VARCHAR(2)   NOT NULL CONSTRAINT movies_movie_language_ck CHECK (movie_language IN ('CZ', 'EN', 'FR', 'JP', 'SK')),
  csfd           VARCHAR(100) NOT NULL,
  imdb_code      INTEGER      NOT NULL CONSTRAINT movies_imdb_code_ck CHECK (imdb_code BETWEEN 1 AND 9999999 OR imdb_code = -1),
  wiki_en        VARCHAR(100) NOT NULL,
  wiki_cz        VARCHAR(100) NOT NULL,
  picture        VARCHAR(20)  NOT NULL,
  note           VARCHAR(100) NOT NULL,
  position       INTEGER      NOT NULL CONSTRAINT movies_position_ck CHECK (position >= 0)
);

CREATE TABLE movie_subtitles (
  movie     INTEGER    NOT NULL CONSTRAINT movie_subtitles_movie_fk REFERENCES movies (id),
  subtitles VARCHAR(2) NOT NULL CONSTRAINT movie_subtitles_subtitles_ck CHECK (subtitles IN ('CZ', 'EN', 'FR', 'JP', 'SK'))
);

CREATE TABLE movie_media (
  movie  INTEGER NOT NULL CONSTRAINT movie_media_movie_fk REFERENCES movies (id),
  medium INTEGER NOT NULL CONSTRAINT movie_media_medium_fk REFERENCES media (id)
);

CREATE TABLE movie_genres (
  movie INTEGER NOT NULL CONSTRAINT movie_genres_movie_fk REFERENCES movies (id),
  genre INTEGER NOT NULL CONSTRAINT movie_genres_genre_fk REFERENCES genres (id)
);

CREATE TABLE series (
  id            INTEGER      NOT NULL CONSTRAINT series_pk PRIMARY KEY,
  czech_name    VARCHAR(200) NOT NULL CONSTRAINT series_czech_name_ck CHECK (LENGTH(czech_name) > 0),
  original_name VARCHAR(100) NOT NULL CONSTRAINT series_original_name_ck CHECK (LENGTH(original_name) > 0),
  csfd          VARCHAR(100) NOT NULL,
  imdb_code     INTEGER      NOT NULL CONSTRAINT series_imdb_code_ck CHECK (imdb_code BETWEEN 1 AND 9999999 OR imdb_code = -1),
  wiki_en       VARCHAR(100) NOT NULL,
  wiki_cz       VARCHAR(100) NOT NULL,
  picture       VARCHAR(20)  NOT NULL,
  note          VARCHAR(100) NOT NULL,
  position      INTEGER      NOT NULL CONSTRAINT series_position_ck CHECK (position >= 0)
);

CREATE TABLE serie_genres (
  serie INTEGER NOT NULL CONSTRAINT serie_genres_serie_fk REFERENCES series (id),
  genre INTEGER NOT NULL CONSTRAINT serie_genres_genre_fk REFERENCES genres (id)
);

CREATE TABLE seasons (
  id              INTEGER      NOT NULL CONSTRAINT seasons_pk PRIMARY KEY,
  serie           INTEGER      NOT NULL CONSTRAINT seasons_serie_fk REFERENCES series (id),
  season_number   INTEGER      NOT NULL CONSTRAINT seasons_season_number_ck CHECK (season_number > 0),
  start_year      INTEGER      NOT NULL CONSTRAINT seasons_start_year_ck CHECK (start_year BETWEEN 1930 AND 2100),
  end_year        INTEGER      NOT NULL CONSTRAINT seasons_end_year_ck CHECK (end_year BETWEEN 1930 AND 2100),
  season_language VARCHAR(2)   NOT NULL CONSTRAINT seasons_season_language_ck CHECK (season_language IN ('CZ', 'EN', 'FR', 'JP', 'SK')),
  note            VARCHAR(100) NOT NULL,
  position        INTEGER      NOT NULL CONSTRAINT seasons_position_ck CHECK (position >= 0),
  CONSTRAINT seasons_years_ck CHECK (start_year <= end_year)
);

CREATE TABLE season_subtitles (
  season    INTEGER    NOT NULL CONSTRAINT season_subtitles_fk REFERENCES seasons (id),
  subtitles VARCHAR(2) NOT NULL CONSTRAINT season_subtitles_subtitles_ck CHECK (subtitles IN ('CZ', 'EN', 'FR', 'JP', 'SK'))
);

CREATE TABLE episodes (
  id             INTEGER      NOT NULL CONSTRAINT episodes_pk PRIMARY KEY,
  season         INTEGER      NOT NULL CONSTRAINT episodes_season_fk REFERENCES seasons (id),
  episode_number INTEGER      NOT NULL CONSTRAINT episodes_episode_number_ck CHECK (episode_number > 0),
  episode_name   VARCHAR(100) NOT NULL CONSTRAINT episodes_episode_name_ck CHECK (LENGTH(episode_name) > 0),
  episode_length INTEGER      NOT NULL CONSTRAINT episodes_episode_length_ck CHECK (episode_length >= 0),
  note           VARCHAR(100) NOT NULL,
  position       INTEGER      NOT NULL CONSTRAINT episodes_position_ck CHECK (position >= 0)
);

CREATE TABLE games (
  id           INTEGER      NOT NULL CONSTRAINT games_pk PRIMARY KEY,
  game_name    VARCHAR(200) NOT NULL CONSTRAINT games_game_name_ck CHECK (LENGTH(game_name) > 0),
  wiki_en      VARCHAR(100) NOT NULL,
  wiki_cz      VARCHAR(100) NOT NULL,
  media_count  INTEGER      NOT NULL CONSTRAINT games_media_count_ck CHECK (media_count > 0),
  crack        BOOLEAN      NOT NULL,
  serial_key   BOOLEAN      NOT NULL,
  patch        BOOLEAN      NOT NULL,
  trainer      BOOLEAN      NOT NULL,
  trainer_data BOOLEAN      NOT NULL,
  editor       BOOLEAN      NOT NULL,
  saves        BOOLEAN      NOT NULL,
  other_data   VARCHAR(100) NOT NULL,
  note         VARCHAR(100) NOT NULL,
  position     INTEGER      NOT NULL CONSTRAINT games_position_ck CHECK (position >= 0)
);

CREATE TABLE music (
  id          INTEGER      NOT NULL CONSTRAINT music_pk PRIMARY KEY,
  music_name  VARCHAR(200) NOT NULL CONSTRAINT music_music_name_ck CHECK (LENGTH(music_name) > 0),
  wiki_en     VARCHAR(100) NOT NULL,
  wiki_cz     VARCHAR(100) NOT NULL,
  media_count INTEGER      NOT NULL CONSTRAINT music_media_count_ck CHECK (media_count > 0),
  note        VARCHAR(100) NOT NULL,
  position    INTEGER      NOT NULL CONSTRAINT music_position_ck CHECK (position >= 0)
);

CREATE TABLE songs (
  id          INTEGER      NOT NULL CONSTRAINT songs_pk PRIMARY KEY,
  music       INTEGER      NOT NULL CONSTRAINT songs_music_fk REFERENCES music (id),
  song_name   VARCHAR(100) NOT NULL CONSTRAINT songs_song_name_ck CHECK (LENGTH(song_name) > 0),
  song_length INTEGER      NOT NULL CONSTRAINT songs_song_length_ck CHECK (song_length >= 0),
  note        VARCHAR(100) NOT NULL,
  position    INTEGER      NOT NULL CONSTRAINT songs_position_ck CHECK (position >= 0)
);

CREATE TABLE programs (
  id           INTEGER      NOT NULL CONSTRAINT programs_pk PRIMARY KEY,
  program_name VARCHAR(200) NOT NULL CONSTRAINT programs_program_name_ck CHECK (LENGTH(program_name) > 0),
  wiki_en      VARCHAR(100) NOT NULL,
  wiki_cz      VARCHAR(100) NOT NULL,
  media_count  INTEGER      NOT NULL CONSTRAINT programs_media_count_ck CHECK (media_count > 0),
  crack        BOOLEAN      NOT NULL,
  serial_key   BOOLEAN      NOT NULL,
  other_data   VARCHAR(100) NOT NULL,
  note         VARCHAR(100) NOT NULL,
  position     INTEGER      NOT NULL CONSTRAINT programs_position_ck CHECK (position >= 0)
);

CREATE TABLE book_categories (
  id                 INTEGER      NOT NULL CONSTRAINT book_categories_pk PRIMARY KEY,
  book_category_name VARCHAR(200) NOT NULL CONSTRAINT book_categories_book_category_name_ck CHECK (LENGTH(book_category_name) > 0),
  note               VARCHAR(100) NOT NULL,
  position           INTEGER      NOT NULL CONSTRAINT book_categories_position_ck CHECK (position >= 0)
);

CREATE TABLE books (
  id            INTEGER      NOT NULL CONSTRAINT books_pk PRIMARY KEY,
  book_category INTEGER      NOT NULL CONSTRAINT books_book_category_fk REFERENCES book_categories (id),
  author        VARCHAR(200) NOT NULL CONSTRAINT books_author_ck CHECK (LENGTH(author) > 0),
  title         VARCHAR(100) NOT NULL CONSTRAINT books_title_ck CHECK (LENGTH(title) > 0),
  category      VARCHAR(100) NOT NULL CONSTRAINT books_category_ck CHECK (LENGTH(category) > 0),
  note          VARCHAR(100) NOT NULL,
  position      INTEGER      NOT NULL CONSTRAINT books_position_ck CHECK (position >= 0)
);

CREATE TABLE book_languages (
  book          INTEGER    NOT NULL CONSTRAINT book_languages_book_fk REFERENCES books (id),
  book_language VARCHAR(2) NOT NULL CONSTRAINT book_languages_book_language_ck CHECK (book_language IN ('CZ', 'EN', 'FR', 'JP', 'SK'))
);

DROP SEQUENCE movies_sq;
DROP SEQUENCE media_sq;
DROP SEQUENCE series_sq;
DROP SEQUENCE seasons_sq;
DROP SEQUENCE episodes_sq;
DROP SEQUENCE games_sq;
DROP SEQUENCE music_sq;
DROP SEQUENCE songs_sq;
DROP SEQUENCE programs_sq;
DROP SEQUENCE book_categories_sq;
DROP SEQUENCE books_sq;
DROP SEQUENCE genres_sq;

CREATE SEQUENCE movies_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE media_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE series_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seasons_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE episodes_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE games_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE music_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE songs_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE programs_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE book_categories_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE books_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE genres_sq START WITH 1 INCREMENT BY 1;
