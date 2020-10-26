INSERT INTO accounts (id, uuid, username, password) VALUES (accounts_sq.nextval, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', 'Account 1 username', 'Account 1 password');
INSERT INTO accounts (id, uuid, username, password) VALUES (accounts_sq.nextval, '0998ab47-0d27-4538-b551-ee7a471cfcf1', 'Account 2 username', 'Account 2 password');
INSERT INTO account_roles (account, role) VALUES ((SELECT id FROM accounts WHERE uuid = 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014'), (SELECT id FROM roles WHERE role_name = 'ROLE_USER'));
INSERT INTO account_roles (account, role) VALUES ((SELECT id FROM accounts WHERE uuid = '0998ab47-0d27-4538-b551-ee7a471cfcf1'), (SELECT id FROM roles WHERE role_name = 'ROLE_ADMIN'));
INSERT INTO account_roles (account, role) VALUES ((SELECT id FROM accounts WHERE uuid = '0998ab47-0d27-4538-b551-ee7a471cfcf1'), (SELECT id FROM roles WHERE role_name = 'ROLE_USER'));
INSERT INTO genres (id, genre_name, position, created_user, created_time, updated_user, updated_time) VALUES (genres_sq.nextval, 'Genre 1 name', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO genres (id, genre_name, position, created_user, created_time, updated_user, updated_time) VALUES (genres_sq.nextval, 'Genre 2 name', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO genres (id, genre_name, position, created_user, created_time, updated_user, updated_time) VALUES (genres_sq.nextval, 'Genre 3 name', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO genres (id, genre_name, position, created_user, created_time, updated_user, updated_time) VALUES (genres_sq.nextval, 'Genre 4 name', 3, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO pictures (id, content, position, created_user, created_time, updated_user, updated_time) VALUES (pictures_sq.nextval, 0x11, 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO pictures (id, content, position, created_user, created_time, updated_user, updated_time) VALUES (pictures_sq.nextval, 0x12, 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO pictures (id, content, position, created_user, created_time, updated_user, updated_time) VALUES (pictures_sq.nextval, 0x13, 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO pictures (id, content, position, created_user, created_time, updated_user, updated_time) VALUES (pictures_sq.nextval, 0x14, 3, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO pictures (id, content, position, created_user, created_time, updated_user, updated_time) VALUES (pictures_sq.nextval, 0x15, 4, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO pictures (id, content, position, created_user, created_time, updated_user, updated_time) VALUES (pictures_sq.nextval, 0x16, 5, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO media (id, medium_number, medium_length, created_user, created_time, updated_user, updated_time) VALUES (media_sq.nextval, 1, 100, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO media (id, medium_number, medium_length, created_user, created_time, updated_user, updated_time) VALUES (media_sq.nextval, 1, 200, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO media (id, medium_number, medium_length, created_user, created_time, updated_user, updated_time) VALUES (media_sq.nextval, 1, 300, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO media (id, medium_number, medium_length, created_user, created_time, updated_user, updated_time) VALUES (media_sq.nextval, 2, 400, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO movies (id, picture, czech_name, original_name, movie_year, movie_language, csfd, imdb_code, wiki_en, wiki_cz, note, position, created_user, created_time, updated_user, updated_time) VALUES (movies_sq.nextval, (SELECT id FROM pictures WHERE content = 0x11), 'Movie 1 czech name', 'Movie 1 original name', 2001, 'CZ', 'Movie 1 CSFD', 1, 'Movie 1 English Wikipedia', 'Movie 1 Czech Wikipedia', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO movies (id, picture, czech_name, original_name, movie_year, movie_language, csfd, imdb_code, wiki_en, wiki_cz, note, position, created_user, created_time, updated_user, updated_time) VALUES (movies_sq.nextval, (SELECT id FROM pictures WHERE content = 0x12), 'Movie 2 czech name', 'Movie 2 original name', 2002, 'JP', 'Movie 2 CSFD', 2, 'Movie 2 English Wikipedia', 'Movie 2 Czech Wikipedia', '', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO movies (id, picture, czech_name, original_name, movie_year, movie_language, csfd, imdb_code, wiki_en, wiki_cz, note, position, created_user, created_time, updated_user, updated_time) VALUES (movies_sq.nextval, (SELECT id FROM pictures WHERE content = 0x13), 'Movie 3 czech name', 'Movie 3 original name', 2003, 'FR', 'Movie 3 CSFD', 3, 'Movie 3 English Wikipedia', 'Movie 3 Czech Wikipedia', 'Movie 3 note', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO movie_subtitles (movie, subtitles) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 2 czech name'), 'EN');
INSERT INTO movie_subtitles (movie, subtitles) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 3 czech name'), 'CZ');
INSERT INTO movie_subtitles (movie, subtitles) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 3 czech name'), 'EN');
INSERT INTO movie_media (movie, medium) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 1 czech name'), (SELECT id FROM media WHERE medium_length = 100));
INSERT INTO movie_media (movie, medium) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 2 czech name'), (SELECT id FROM media WHERE medium_length = 200));
INSERT INTO movie_media (movie, medium) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 3 czech name'), (SELECT id FROM media WHERE medium_length = 300));
INSERT INTO movie_media (movie, medium) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 3 czech name'), (SELECT id FROM media WHERE medium_length = 400));
INSERT INTO movie_genres (movie, genre) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 1 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 1 name'));
INSERT INTO movie_genres (movie, genre) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 2 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 2 name'));
INSERT INTO movie_genres (movie, genre) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 3 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 3 name'));
INSERT INTO movie_genres (movie, genre) VALUES ((SELECT id FROM movies WHERE czech_name = 'Movie 3 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 4 name'));
INSERT INTO tv_shows (id, picture, czech_name, original_name, csfd, imdb_code, wiki_en, wiki_cz, note, position, created_user, created_time, updated_user, updated_time) VALUES (tv_shows_sq.nextval, (SELECT id FROM pictures WHERE content = 0x14), 'Show 1 czech name', 'Show 1 original name', 'Show 1 CSFD', 100, 'Show 1 English Wikipedia', 'Show 1 Czech Wikipedia', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO tv_shows (id, picture, czech_name, original_name, csfd, imdb_code, wiki_en, wiki_cz, note, position, created_user, created_time, updated_user, updated_time) VALUES (tv_shows_sq.nextval, (SELECT id FROM pictures WHERE content = 0x15), 'Show 2 czech name', 'Show 2 original name', 'Show 2 CSFD', 200, 'Show 2 English Wikipedia', 'Show 2 Czech Wikipedia', 'Show 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO tv_shows (id, picture, czech_name, original_name, csfd, imdb_code, wiki_en, wiki_cz, note, position, created_user, created_time, updated_user, updated_time) VALUES (tv_shows_sq.nextval, (SELECT id FROM pictures WHERE content = 0x16), 'Show 3 czech name', 'Show 3 original name', 'Show 3 CSFD', 300, 'Show 3 English Wikipedia', 'Show 3 Czech Wikipedia', '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO tv_show_genres (tv_show, genre) VALUES ((SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 1 name'));
INSERT INTO tv_show_genres (tv_show, genre) VALUES ((SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 2 name'));
INSERT INTO tv_show_genres (tv_show, genre) VALUES ((SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 3 name'));
INSERT INTO tv_show_genres (tv_show, genre) VALUES ((SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name'), (SELECT id FROM genres WHERE genre_name = 'Genre 4 name'));
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name'), 1, 1981, 1982, 'EN', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name'), 2, 1982, 1982, 'FR', 'Show 1 Season 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name'), 3, 1983, 1984, 'JP', '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name'), 1, 1981, 1982, 'EN', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name'), 2, 1982, 1982, 'FR', 'Show 2 Season 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name'), 3, 1983, 1984, 'JP', '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name'), 1, 1981, 1982, 'EN', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name'), 2, 1982, 1982, 'FR', 'Show 3 Season 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO seasons (id, tv_show, season_number, start_year, end_year, season_language, note, position, created_user, created_time, updated_user, updated_time) VALUES (seasons_sq.nextval, (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name'), 3, 1983, 1984, 'JP', '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 1), 'CZ');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 1), 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 3), 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 1), 'CZ');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 1), 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 3), 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 1), 'CZ');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 1), 'EN');
INSERT INTO season_subtitles (season, subtitles) VALUES ((SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 3), 'EN');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 1), 1, 'Show 1 Season 1 Episode 1', 1, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 1), 2, 'Show 1 Season 1 Episode 2', 2, 'Show 1 Season 1 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 1), 3, 'Show 1 Season 1 Episode 3', 3, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 2), 1, 'Show 1 Season 2 Episode 1', 10, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 2), 2, 'Show 1 Season 2 Episode 2', 20, 'Show 1 Season 2 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 2), 3, 'Show 1 Season 2 Episode 3', 30, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 3), 1, 'Show 1 Season 3 Episode 1', 100, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 3), 2, 'Show 1 Season 3 Episode 2', 200, 'Show 1 Season 3 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 1 czech name') AND season_number = 3), 3, 'Show 1 Season 3 Episode 3', 300, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 1), 1, 'Show 2 Season 1 Episode 1', 1, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 1), 2, 'Show 2 Season 1 Episode 2', 2, 'Show 2 Season 1 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 1), 3, 'Show 2 Season 1 Episode 3', 3, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 2), 1, 'Show 2 Season 2 Episode 1', 10, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 2), 2, 'Show 2 Season 2 Episode 2', 20, 'Show 2 Season 2 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 2), 3, 'Show 2 Season 2 Episode 3', 30, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 3), 1, 'Show 2 Season 3 Episode 1', 100, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 3), 2, 'Show 2 Season 3 Episode 2', 200, 'Show 2 Season 3 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 2 czech name') AND season_number = 3), 3, 'Show 2 Season 3 Episode 3', 300, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 1), 1, 'Show 3 Season 1 Episode 1', 1, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 1), 2, 'Show 3 Season 1 Episode 2', 2, 'Show 3 Season 1 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 1), 3, 'Show 3 Season 1 Episode 3', 3, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 2), 1, 'Show 3 Season 2 Episode 1', 10, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 2), 2, 'Show 3 Season 2 Episode 2', 20, 'Show 3 Season 2 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 2), 3, 'Show 3 Season 2 Episode 3', 30, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 3), 1, 'Show 3 Season 3 Episode 1', 100, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 3), 2, 'Show 3 Season 3 Episode 2', 200, 'Show 3 Season 3 Episode 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO episodes (id, season, episode_number, episode_name, episode_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (episodes_sq.nextval, (SELECT id FROM seasons WHERE tv_show = (SELECT id FROM tv_shows WHERE czech_name = 'Show 3 czech name') AND season_number = 3), 3, 'Show 3 Season 3 Episode 3', 300, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheats(id, game_setting, cheat_setting, created_user, created_time, updated_user, updated_time) VALUES (cheats_sq.nextval, 'Game 1 setting', 'Cheat 1 setting', 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheats(id, game_setting, cheat_setting, created_user, created_time, updated_user, updated_time) VALUES (cheats_sq.nextval, 'Game 2 setting', 'Cheat 2 setting', 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheat_data(id, cheat, action, description, position, created_user, created_time, updated_user, updated_time) VALUES (cheat_data_sq.nextval, (SELECT id FROM cheats WHERE game_setting = 'Game 1 setting'), 'Cheat 1 Data 1 action', 'Cheat 1 Data 1 description', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheat_data(id, cheat, action, description, position, created_user, created_time, updated_user, updated_time) VALUES (cheat_data_sq.nextval, (SELECT id FROM cheats WHERE game_setting = 'Game 1 setting'), 'Cheat 1 Data 2 action', 'Cheat 1 Data 2 description', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheat_data(id, cheat, action, description, position, created_user, created_time, updated_user, updated_time) VALUES (cheat_data_sq.nextval, (SELECT id FROM cheats WHERE game_setting = 'Game 1 setting'), 'Cheat 1 Data 3 action', 'Cheat 1 Data 3 description', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheat_data(id, cheat, action, description, position, created_user, created_time, updated_user, updated_time) VALUES (cheat_data_sq.nextval, (SELECT id FROM cheats WHERE game_setting = 'Game 2 setting'), 'Cheat 2 Data 1 action', 'Cheat 2 Data 1 description', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheat_data(id, cheat, action, description, position, created_user, created_time, updated_user, updated_time) VALUES (cheat_data_sq.nextval, (SELECT id FROM cheats WHERE game_setting = 'Game 2 setting'), 'Cheat 2 Data 2 action', 'Cheat 2 Data 2 description', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO cheat_data(id, cheat, action, description, position, created_user, created_time, updated_user, updated_time) VALUES (cheat_data_sq.nextval, (SELECT id FROM cheats WHERE game_setting = 'Game 2 setting'), 'Cheat 2 Data 3 action', 'Cheat 2 Data 3 description', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO games (id, game_name, wiki_en, wiki_cz, media_count, format, crack, serial_key, patch, trainer, trainer_data, editor, saves, other_data, note, position, created_user, created_time, updated_user, updated_time) VALUES (games_sq.nextval, 'Game 1 name', 'Game 1 English Wikipedia', 'Game 1 Czech Wikipedia', 1, 'ISO', FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, '', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO games (id, game_name, wiki_en, wiki_cz, media_count, format, cheat, crack, serial_key, patch, trainer, trainer_data, editor, saves, other_data, note, position, created_user, created_time, updated_user, updated_time) VALUES (games_sq.nextval, 'Game 2 name', 'Game 2 English Wikipedia', 'Game 2 Czech Wikipedia', 2, 'STEAM', (SELECT id FROM cheats WHERE game_setting = 'Game 1 setting'), TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, '', '', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO games (id, game_name, wiki_en, wiki_cz, media_count, format, cheat, crack, serial_key, patch, trainer, trainer_data, editor, saves, other_data, note, position, created_user, created_time, updated_user, updated_time) VALUES (games_sq.nextval, 'Game 3 name', 'Game 3 English Wikipedia', 'Game 3 Czech Wikipedia', 3, 'BINARY', (SELECT id FROM cheats WHERE game_setting = 'Game 2 setting'), TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, 'Game 3 other data', 'Game 3 note', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO music (id, music_name, wiki_en, wiki_cz, media_count, note, position, created_user, created_time, updated_user, updated_time) VALUES (music_sq.nextval, 'Music 1 name', 'Music 1 English Wikipedia', 'Music 1 Czech Wikipedia', 10, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO music (id, music_name, wiki_en, wiki_cz, media_count, note, position, created_user, created_time, updated_user, updated_time) VALUES (music_sq.nextval, 'Music 2 name', 'Music 2 English Wikipedia', 'Music 2 Czech Wikipedia', 20, 'Music 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO music (id, music_name, wiki_en, wiki_cz, media_count, note, position, created_user, created_time, updated_user, updated_time) VALUES (music_sq.nextval, 'Music 3 name', 'Music 3 English Wikipedia', 'Music 3 Czech Wikipedia', 30, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 1 name'), 'Music 1 Song 1', 1, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 1 name'), 'Music 1 Song 2', 2, 'Music 1 Song 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 1 name'), 'Music 1 Song 3', 3, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 2 name'), 'Music 2 Song 1', 10, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 2 name'), 'Music 2 Song 2', 20, 'Music 2 Song 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 2 name'), 'Music 2 Song 3', 30, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 3 name'), 'Music 3 Song 1', 100, '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 3 name'), 'Music 3 Song 2', 200, 'Music 3 Song 2 note', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO songs (id, music, song_name, song_length, note, position, created_user, created_time, updated_user, updated_time) VALUES (songs_sq.nextval, (SELECT id FROM music WHERE music_name = 'Music 3 name'), 'Music 3 Song 3', 300, '', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO programs (id, program_name, wiki_en, wiki_cz, media_count, format, crack, serial_key, other_data, note, position, created_user, created_time, updated_user, updated_time) VALUES (programs_sq.nextval, 'Program 1 name', 'Program 1 English Wikipedia', 'Program 1 Czech Wikipedia', 100, 'ISO', false, false, '', '', 0, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO programs (id, program_name, wiki_en, wiki_cz, media_count, format, crack, serial_key, other_data, note, position, created_user, created_time, updated_user, updated_time) VALUES (programs_sq.nextval, 'Program 2 name', 'Program 2 English Wikipedia', 'Program 2 Czech Wikipedia', 200, 'STEAM', false, true, '', '', 1, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
INSERT INTO programs (id, program_name, wiki_en, wiki_cz, media_count, format, crack, serial_key, other_data, note, position, created_user, created_time, updated_user, updated_time) VALUES (programs_sq.nextval, 'Program 3 name', 'Program 3 English Wikipedia', 'Program 3 Czech Wikipedia', 300, 'BINARY', true, true, 'Program 3 other data', 'Program 3 note', 2, 'd53b2577-a3de-4df7-a8dd-2e6d9e5c1014', '2020-01-01 00:00:00', '0998ab47-0d27-4538-b551-ee7a471cfcf1', '2020-01-02 00:00:00');
