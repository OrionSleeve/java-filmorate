drop table IF EXISTS rating_mpa CASCADE;
drop table IF EXISTS friendship CASCADE;
drop table IF EXISTS films CASCADE;
drop table IF EXISTS users CASCADE;
drop table IF EXISTS films CASCADE;
drop table IF EXISTS likes CASCADE;
drop table IF EXISTS genres CASCADE;
drop table IF EXISTS film_genre CASCADE;


create TABLE IF NOT EXISTS rating_mpa(
    id INTEGER  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255)
    );

create TABLE IF NOT EXISTS genres(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR (50)
    );

create TABLE IF NOT EXISTS films(
    id INTEGER generated by default as identity primary key,
    name VARCHAR(50) NOT NULL CHECK (name <> ''),
    description VARCHAR(255) NOT NULL,
    release_date DATE,
    duration INTEGER CHECK (duration > 0),
    rating_id INTEGER,
    FOREIGN  KEY (rating_id) REFERENCES rating_mpa(id) ON delete SET NULL,
    CONSTRAINT exist_film_name UNIQUE (name)
    );

create TABLE IF NOT EXISTS film_genre(
    film_id INTEGER REFERENCES films(id) ON delete CASCADE,
    genre_id INTEGER REFERENCES genres(id) ON delete CASCADE,
    PRIMARY KEY(film_id, genre_id)
    );

create TABLE IF NOT EXISTS users(
    id INTEGER generated by default as identity primary key,
    email VARCHAR (70)  NOT NULL,
    login VARCHAR(50) NOT NULL,
    name varchar(255) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT user_const CHECK (login <> '' AND email <> '' ),
    CONSTRAINT exist_user_email UNIQUE (email),
    CONSTRAINT exist_user_login UNIQUE (login)
    );

create TABLE IF NOT EXISTS friendship(
    user_id INTEGER REFERENCES users (id) ON delete CASCADE,
    friend_user_id INTEGER REFERENCES users(id) ON delete CASCADE,
    friend_status BOOLEAN,
    PRIMARY KEY (user_id, friend_user_id)
    );

create TABLE IF NOT EXISTS likes(
    film_id INTEGER REFERENCES films (id) ON delete CASCADE,
    user_id INTEGER REFERENCES users (id) ON delete CASCADE,
    PRIMARY KEY(user_id, film_id)
    );