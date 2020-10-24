ALTER TABLE games ADD format VARCHAR(6) CONSTRAINT games_format_ck CHECK (format IN ('ISO', 'BINARY', 'STEAM'));
ALTER TABLE programs ADD format VARCHAR(6) CONSTRAINT programs_format_ck CHECK (format IN ('ISO', 'BINARY', 'STEAM'));

UPDATE games SET format='ISO';
UPDATE programs SET format='ISO';

ALTER TABLE games ALTER COLUMN format SET NOT NULL;
ALTER TABLE games ALTER COLUMN format SET NOT NULL;
