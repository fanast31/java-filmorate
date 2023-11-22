
CREATE TABLE IF NOT EXISTS friends (
  user_id int,
  friend_id int,
  PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS users (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar(100) NOT NULL UNIQUE,
  login varchar(100) NOT NULL,
  name varchar(100),
  birthday date
);

CREATE TABLE IF NOT EXISTS genres (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS filmsGenres (
  film_id int,
  genre_id int,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likesFromUsers (
  film_id int,
  user_id int,
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS mpa (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(200),
  releaseDate date NOT NULL,
  duration int,
  mpa_id int
);

ALTER TABLE friends ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE friends ADD FOREIGN KEY (friend_id) REFERENCES users (id);

ALTER TABLE filmsGenres ADD FOREIGN KEY (genre_id) REFERENCES genres (id);
ALTER TABLE filmsGenres ADD FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE likesFromUsers ADD FOREIGN KEY (film_id) REFERENCES films (id);
ALTER TABLE likesFromUsers ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE films ADD FOREIGN KEY (mpa_id) REFERENCES mpa (id);
