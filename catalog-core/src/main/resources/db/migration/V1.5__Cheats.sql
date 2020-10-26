DROP TABLE IF EXISTS cheat_data;
DROP TABLE IF EXISTS cheats;

CREATE TABLE cheats (
  id            INTEGER      NOT NULL CONSTRAINT cheats_pk PRIMARY KEY,
  game_setting  VARCHAR(200),
  cheat_setting VARCHAR(200),
  created_user  VARCHAR(36)  NOT NULL,
  created_time  TIMESTAMP    NOT NULL,
  updated_user  VARCHAR(36)  NOT NULL,
  updated_time  TIMESTAMP    NOT NULL
);

CREATE TABLE cheat_data (
  id           INTEGER      NOT NULL CONSTRAINT cheat_data_pk PRIMARY KEY,
  cheat        INTEGER      CONSTRAINT cheat_data_cheat_fk REFERENCES cheats (id),
  action       VARCHAR(200) NOT NULL CONSTRAINT cheat_data_action_ck CHECK (LENGTH(action) > 0),
  description  VARCHAR(200) NOT NULL CONSTRAINT cheat_data_description_ck CHECK (LENGTH(description) > 0),
  position     INTEGER      NOT NULL CONSTRAINT cheat_data_position_ck CHECK (position >= 0),
  created_user VARCHAR(36)  NOT NULL,
  created_time TIMESTAMP    NOT NULL,
  updated_user VARCHAR(36)  NOT NULL,
  updated_time TIMESTAMP    NOT NULL
);

ALTER TABLE games ADD cheat INTEGER CONSTRAINT games_cheat_fk REFERENCES cheats (id);

DROP SEQUENCE IF EXISTS cheats_sq;
DROP SEQUENCE IF EXISTS cheat_data_sq;

CREATE SEQUENCE cheats_sq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE cheat_data_sq START WITH 1 INCREMENT BY 1;
