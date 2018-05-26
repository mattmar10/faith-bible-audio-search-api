CREATE SCHEMA IF NOT EXISTS fbc_media;

CREATE TABLE IF NOT EXISTS fbc_media.sermons(
  sermon_id SERIAL PRIMARY KEY NOT NULL,
  title varchar(255) NOT NULL,
  date TIMESTAMP,
  mp3_url varchar(512),
  pdf_url varchar(512),
  video_url varchar(512),
  speaker varchar(255),
  series_id integer,
  image_url varchar(512),
  slug varchar(255) NOT NULL,
  tags jsonb,

  likes integer,
  shares integer,
  plays integer,

  mapped boolean,
  creation_date     TIMESTAMP,
  last_updated_by   varchar(255),
  last_updated_date TIMESTAMP
);
