CREATE TABLE genres (
  id         INTEGER      NOT NULL CONSTRAINT genres_pk PRIMARY KEY,
  genre_name VARCHAR(200) NOT NULL CONSTRAINT genres_genre_name_ck CHECK (LENGTH(genre_name) > 0),
  position   INTEGER      NOT NULL CONSTRAINT genres_position_ck CHECK (position >= 0)
);

CREATE TABLE pictures (
  id       INTEGER  NOT NULL CONSTRAINT pictures_pk PRIMARY KEY,
  content  BLOB     NOT NULL,
  position INTEGER  NOT NULL CONSTRAINT pictures_position_ck CHECK (position >= 0)
);

CREATE TABLE media (
  id            INTEGER NOT NULL CONSTRAINT media_pk PRIMARY KEY,
  medium_number INTEGER NOT NULL CONSTRAINT media_medium_number_ck CHECK (medium_number > 0),
  medium_length INTEGER NOT NULL CONSTRAINT media_medium_length_ck CHECK (medium_length >= 0)
);

CREATE TABLE movies (
  id             INTEGER      NOT NULL CONSTRAINT movies_pk PRIMARY KEY,
  picture        INTEGER      CONSTRAINT movies_picture_fk REFERENCES pictures (id),
  czech_name     VARCHAR(200) NOT NULL CONSTRAINT movies_czech_name_ck CHECK (LENGTH(czech_name) > 0),
  original_name  VARCHAR(100) NOT NULL CONSTRAINT movies_original_name_ck CHECK (LENGTH(original_name) > 0),
  movie_year     INTEGER      NOT NULL CONSTRAINT movies_movie_year_ck CHECK (movie_year BETWEEN 1930 AND 2100),
  movie_language VARCHAR(2)   NOT NULL CONSTRAINT movies_movie_language_ck CHECK (movie_language IN ('CZ', 'EN', 'FR', 'JP', 'SK')),
  csfd           VARCHAR(100) NOT NULL,
  imdb_code      INTEGER      NOT NULL CONSTRAINT movies_imdb_code_ck CHECK (imdb_code BETWEEN 1 AND 9999999 OR imdb_code = -1),
  wiki_en        VARCHAR(100) NOT NULL,
  wiki_cz        VARCHAR(100) NOT NULL,
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

CREATE TABLE tv_shows (
  id            INTEGER      NOT NULL CONSTRAINT tv_shows_pk PRIMARY KEY,
  picture       INTEGER      CONSTRAINT tv_shows_picture_fk REFERENCES pictures (id),
  czech_name    VARCHAR(200) NOT NULL CONSTRAINT tv_shows_czech_name_ck CHECK (LENGTH(czech_name) > 0),
  original_name VARCHAR(100) NOT NULL CONSTRAINT tv_shows_original_name_ck CHECK (LENGTH(original_name) > 0),
  csfd          VARCHAR(100) NOT NULL,
  imdb_code     INTEGER      NOT NULL CONSTRAINT tv_shows_imdb_code_ck CHECK (imdb_code BETWEEN 1 AND 9999999 OR imdb_code = -1),
  wiki_en       VARCHAR(100) NOT NULL,
  wiki_cz       VARCHAR(100) NOT NULL,
  note          VARCHAR(100) NOT NULL,
  position      INTEGER      NOT NULL CONSTRAINT tv_shows_position_ck CHECK (position >= 0)
);

CREATE TABLE tv_show_genres (
  tv_show INTEGER NOT NULL CONSTRAINT tv_show_genres_show_fk REFERENCES tv_shows (id),
  genre   INTEGER NOT NULL CONSTRAINT tv_show_genres_genre_fk REFERENCES genres (id)
);

CREATE TABLE seasons (
  id              INTEGER      NOT NULL CONSTRAINT seasons_pk PRIMARY KEY,
  tv_show         INTEGER      CONSTRAINT seasons_tv_show_fk REFERENCES tv_shows (id),
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
  season         INTEGER      CONSTRAINT episodes_season_fk REFERENCES seasons (id),
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
  music       INTEGER      CONSTRAINT songs_music_fk REFERENCES music (id),
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

CREATE SEQUENCE movies_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE media_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE tv_shows_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seasons_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE episodes_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE games_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE music_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE songs_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE programs_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE genres_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pictures_sq START WITH 1 INCREMENT BY 1;
