DELETE
FROM rating_mpa;
INSERT INTO rating_mpa (name, description)
VALUES ('G', 'нет возрастных ограничений'),
       ('PG', 'детям рекомендуется смотреть фильм с родителями'),
       ('PG-13', 'детям до 13 лет просмотр не желателен'),
       ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       ('NC-17', 'лицам до 18 лет просмотр запрещён');
DELETE
FROM genres;
INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');