CREATE TABLE IF NOT EXISTS fbc_media.series(
  series_id  SERIAL PRIMARY KEY NOT NULL,
  title varchar(255) NOT NULL,
  image_url varchar(512),
  slug varchar(255) NOT NULL,
  likes integer,
  shares integer,
  plays integer,
  tags jsonb,
  mapped boolean,
  creation_date     TIMESTAMP,
  last_updated_by   varchar(255),
  last_updated_date TIMESTAMP

);