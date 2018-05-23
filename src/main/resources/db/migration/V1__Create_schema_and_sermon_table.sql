CREATE SCHEMA IF NOT EXISTS fbc_media;

CREATE TABLE IF NOT EXISTS fbc_media.sermons(
  id          integer PRIMARY KEY,
  title varchar(255),
  href varchar(512),
  speaker varchar(255),
  series varchar(255),
  image_url varchar(512),
  tags jsonb,

  likes integer,
  shares integer,
  plays integer,

  mapped boolean,
  creation_date     varchar(32),
  last_updated_by   varchar(255),
  last_updated_date varchar(255)

);
