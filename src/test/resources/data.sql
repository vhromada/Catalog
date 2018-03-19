INSERT INTO genres (id, genre_name, position) VALUES (1, 'Genre 1 name', 0);
INSERT INTO genres (id, genre_name, position) VALUES (2, 'Genre 2 name', 1);
INSERT INTO genres (id, genre_name, position) VALUES (3, 'Genre 3 name', 2);
INSERT INTO genres (id, genre_name, position) VALUES (4, 'Genre 4 name', 3);
INSERT INTO pictures (id, content, position) VALUES (1, 0x11, 0);
INSERT INTO pictures (id, content, position) VALUES (2, 0x12, 1);
INSERT INTO pictures (id, content, position) VALUES (3, 0x13, 2);
INSERT INTO pictures (id, content, position) VALUES (4, 0x14, 3);
INSERT INTO pictures (id, content, position) VALUES (5, 0x15, 4);
INSERT INTO pictures (id, content, position) VALUES (6, 0x16, 5);
INSERT INTO media (id, medium_number, medium_length) VALUES (1, 1, 100);
INSERT INTO media (id, medium_number, medium_length) VALUES (2, 1, 200);
INSERT INTO media (id, medium_number, medium_length) VALUES (3, 1, 300);
INSERT INTO media (id, medium_number, medium_length) VALUES (4, 2, 400);
INSERT INTO movies (id, picture, czech_name, original_name, movie_year, movie_language, csfd, imdb_code, wiki_en, wiki_cz, note, position) VALUES (1, 1, 'Movie 1 czech name', 'Movie 1 original name', 2001, 'CZ', 'Movie 1 CSFD', 1, 'Movie 1 English Wikipedia', 'Movie 1 Czech Wikipedia', '', 0);
INSERT INTO movies (id, picture, czech_name, original_name, movie_year, movie_language, csfd, imdb_code, wiki_en, wiki_cz, note, position) VALUES (2, 2, 'Movie 2 czech name', 'Movie 2 original name', 2002, 'JP', 'Movie 2 CSFD', 2, 'Movie 2 English Wikipedia', 'Movie 2 Czech Wikipedia', '', 1);
INSERT INTO movies (id, picture, czech_name, original_name, movie_year, movie_language, csfd, imdb_code, wiki_en, wiki_cz, note, position) VALUES (3, 3, 'Movie 3 czech name', 'Movie 3 original name', 2003, 'FR', 'Movie 3 CSFD', 3, 'Movie 3 English Wikipedia', 'Movie 3 Czech Wikipedia', 'Movie 3 note', 2);
INSERT INTO movie_subtitles (movie, subtitles) VALUES (2, 'EN');
INSERT INTO movie_subtitles (movie, subtitles) VALUES (3, 'CZ');
INSERT INTO movie_subtitles (movie, subtitles) VALUES (3, 'EN');
INSERT INTO movie_media (movie, medium) VALUES (1, 1);
INSERT INTO movie_media (movie, medium) VALUES (2, 2);
INSERT INTO movie_media (movie, medium) VALUES (3, 3);
INSERT INTO movie_media (movie, medium) VALUES (3, 4);
INSERT INTO movie_genres (movie, genre) VALUES (1, 1);
INSERT INTO movie_genres (movie, genre) VALUES (2, 2);
INSERT INTO movie_genres (movie, genre) VALUES (3, 3);
INSERT INTO movie_genres (movie, genre) VALUES (3, 4);
INSERT INTO tv_shows (id, picture, czech_name, original_name, csfd, imdb_code, wiki_en, wiki_cz, note, position) VALUES (1, 4, 'Show 1 czech name', 'Show 1 original name', 'Show 1 CSFD', 100, 'Show 1 English Wikipedia', 'Show 1 Czech Wikipedia', '', 0);
INSERT INTO tv_shows (id, picture, czech_name, original_name, csfd, imdb_code, wiki_en, wiki_cz, note, position) VALUES (2, 5, 'Show 2 czech name', 'Show 2 original name', 'Show 2 CSFD', 200, 'Show 2 English Wikipedia', 'Show 2 Czech Wikipedia', 'Show 2 note', 1);
INSERT INTO tv_shows (id, picture, czech_name, original_name, csfd, imdb_code, wiki_en, wiki_cz, note, position) VALUES (3, 6, 'Show 3 czech name', 'Show 3 original name', 'Show 3 CSFD', 300, 'Show 3 English Wikipedia', 'Show 3 Czech Wikipedia', '', 2);
INSERT INTO tv_show_genres (tv_show, genre) VALUES (1, 1);
INSERT INTO tv_show_genres (tv_show, genre) VALUES (2, 2);
INSERT INTO tv_show_genres (tv_show, genre) VALUES (3, 3);
INSERT INTO tv_show_genres (tv_show, genre) VALUES (3, 4);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (1, 1, 1, 1981, 1982, 'EN', '', 0);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (2, 1, 2, 1982, 1982, 'FR', 'Show 1 Season 2 note', 1);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (3, 1, 3, 1983, 1984, 'JP', '', 2);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (4, 2, 1, 1981, 1982, 'EN', '', 0);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (5, 2, 2, 1982, 1982, 'FR', 'Show 2 Season 2 note', 1);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (6, 2, 3, 1983, 1984, 'JP', '', 2);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (7, 3, 1, 1981, 1982, 'EN', '', 0);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (8, 3, 2, 1982, 1982, 'FR', 'Show 3 Season 2 note', 1);
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position) VALUES (9, 3, 3, 1983, 1984, 'JP', '', 2);
INSERT INTO season_subtitles (season, subtitles) VALUES (1, 'CZ');
INSERT INTO season_subtitles (season, subtitles) VALUES (1, 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES (3, 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES (4, 'CZ');
INSERT INTO season_subtitles (season, subtitles) VALUES (4, 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES (6, 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES (7, 'CZ');
INSERT INTO season_subtitles (season, subtitles) VALUES (7, 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES (9, 'EN');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (1, 1, 1, 'Show 1 Season 1 Episode 1', 1, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (2, 1, 2, 'Show 1 Season 1 Episode 2', 2, 'Show 1 Season 1 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (3, 1, 3, 'Show 1 Season 1 Episode 3', 3, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (4, 2, 1, 'Show 1 Season 2 Episode 1', 10, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (5, 2, 2, 'Show 1 Season 2 Episode 2', 20, 'Show 1 Season 2 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (6, 2, 3, 'Show 1 Season 2 Episode 3', 30, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (7, 3, 1, 'Show 1 Season 3 Episode 1', 100, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (8, 3, 2, 'Show 1 Season 3 Episode 2', 200, 'Show 1 Season 3 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (9, 3, 3, 'Show 1 Season 3 Episode 3', 300, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (10, 4, 1, 'Show 2 Season 1 Episode 1', 1, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (11, 4, 2, 'Show 2 Season 1 Episode 2', 2, 'Show 2 Season 1 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (12, 4, 3, 'Show 2 Season 1 Episode 3', 3, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (13, 5, 1, 'Show 2 Season 2 Episode 1', 10, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (14, 5, 2, 'Show 2 Season 2 Episode 2', 20, 'Show 2 Season 2 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (15, 5, 3, 'Show 2 Season 2 Episode 3', 30, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (16, 6, 1, 'Show 2 Season 3 Episode 1', 100, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (17, 6, 2, 'Show 2 Season 3 Episode 2', 200, 'Show 2 Season 3 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (18, 6, 3, 'Show 2 Season 3 Episode 3', 300, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (19, 7, 1, 'Show 3 Season 1 Episode 1', 1, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (20, 7, 2, 'Show 3 Season 1 Episode 2', 2, 'Show 3 Season 1 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (21, 7, 3, 'Show 3 Season 1 Episode 3', 3, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (22, 8, 1, 'Show 3 Season 2 Episode 1', 10, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (23, 8, 2, 'Show 3 Season 2 Episode 2', 20, 'Show 3 Season 2 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (24, 8, 3, 'Show 3 Season 2 Episode 3', 30, '', 2);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (25, 9, 1, 'Show 3 Season 3 Episode 1', 100, '', 0);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (26, 9, 2, 'Show 3 Season 3 Episode 2', 200, 'Show 3 Season 3 Episode 2 note', 1);
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position) VALUES (27, 9, 3, 'Show 3 Season 3 Episode 3', 300, '', 2);
INSERT INTO games (id, game_name, wiki_en, wiki_cz, media_count, crack, serial_key, patch, trainer, trainer_data, editor, saves, other_data, note, position) VALUES (1, 'Game 1 name', 'Game 1 English Wikipedia', 'Game 1 Czech Wikipedia', 1, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, '', '', 0);
INSERT INTO games (id, game_name, wiki_en, wiki_cz, media_count, crack, serial_key, patch, trainer, trainer_data, editor, saves, other_data, note, position) VALUES (2, 'Game 2 name', 'Game 2 English Wikipedia', 'Game 2 Czech Wikipedia', 2, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, '', '', 1);
INSERT INTO games (id, game_name, wiki_en, wiki_cz, media_count, crack, serial_key, patch, trainer, trainer_data, editor, saves, other_data, note, position) VALUES (3, 'Game 3 name', 'Game 3 English Wikipedia', 'Game 3 Czech Wikipedia', 3, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, 'Game 3 other data', 'Game 3 note', 2);
INSERT INTO music (id, music_name, wiki_en, wiki_cz, media_count, note, position) VALUES (1, 'Music 1 name', 'Music 1 English Wikipedia', 'Music 1 Czech Wikipedia', 10, '', 0);
INSERT INTO music (id, music_name, wiki_en, wiki_cz, media_count, note, position) VALUES (2, 'Music 2 name', 'Music 2 English Wikipedia', 'Music 2 Czech Wikipedia', 20, 'Music 2 note', 1);
INSERT INTO music (id, music_name, wiki_en, wiki_cz, media_count, note, position) VALUES (3, 'Music 3 name', 'Music 3 English Wikipedia', 'Music 3 Czech Wikipedia', 30, '', 2);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (1, 1, 'Music 1 Song 1', 1, '', 0);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (2, 1, 'Music 1 Song 2', 2, 'Music 1 Song 2 note', 1);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (3, 1, 'Music 1 Song 3', 3, '', 2);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (4, 2, 'Music 2 Song 1', 10, '', 0);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (5, 2, 'Music 2 Song 2', 20, 'Music 2 Song 2 note', 1);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (6, 2, 'Music 2 Song 3', 30, '', 2);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (7, 3, 'Music 3 Song 1', 100, '', 0);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (8, 3, 'Music 3 Song 2', 200, 'Music 3 Song 2 note', 1);
INSERT INTO songs (id, music, song_name, song_length, note, position) VALUES (9, 3, 'Music 3 Song 3', 300, '', 2);
INSERT INTO programs (id, program_name, wiki_en, wiki_cz, media_count, crack, serial_key, other_data, note, position) VALUES (1, 'Program 1 name', 'Program 1 English Wikipedia', 'Program 1 Czech Wikipedia', 100, false, false, '', '', 0);
INSERT INTO programs (id, program_name, wiki_en, wiki_cz, media_count, crack, serial_key, other_data, note, position) VALUES (2, 'Program 2 name', 'Program 2 English Wikipedia', 'Program 2 Czech Wikipedia', 200, false, true, '', '', 1);
INSERT INTO programs (id, program_name, wiki_en, wiki_cz, media_count, crack, serial_key, other_data, note, position) VALUES (3, 'Program 3 name', 'Program 3 English Wikipedia', 'Program 3 Czech Wikipedia', 300, true, true, 'Program 3 other data', 'Program 3 note', 2);

-- noinspection SqlResolve
ALTER SEQUENCE movies_sq RESTART WITH 4;
-- noinspection SqlResolve
ALTER SEQUENCE media_sq RESTART WITH 5;
-- noinspection SqlResolve
ALTER SEQUENCE tv_shows_sq RESTART WITH 4;
-- noinspection SqlResolve
ALTER SEQUENCE seasons_sq RESTART WITH 10;
-- noinspection SqlResolve
ALTER SEQUENCE episodes_sq RESTART WITH 28;
-- noinspection SqlResolve
ALTER SEQUENCE games_sq RESTART WITH 4;
-- noinspection SqlResolve
ALTER SEQUENCE music_sq RESTART WITH 4;
-- noinspection SqlResolve
ALTER SEQUENCE songs_sq RESTART WITH 10;
-- noinspection SqlResolve
ALTER SEQUENCE programs_sq RESTART WITH 4;
-- noinspection SqlResolve
ALTER SEQUENCE genres_sq RESTART WITH 5;
-- noinspection SqlResolve
ALTER SEQUENCE pictures_sq RESTART WITH 7;