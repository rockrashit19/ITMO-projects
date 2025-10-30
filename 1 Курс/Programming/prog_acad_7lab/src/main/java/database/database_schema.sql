BEGIN;

DROP TABLE IF EXISTS labwork CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS coordinates CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(40) NOT NULL -- SHA-1 hex (40 символов)
);

CREATE TABLE coordinates (
                             id SERIAL PRIMARY KEY,
                             x DOUBLE PRECISION NOT NULL,
                             y INTEGER NOT NULL CHECK (y > -563)
);

CREATE TABLE person (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        birthday TIMESTAMP WITH TIME ZONE NOT NULL,
                        passport_id VARCHAR(255) NOT NULL,
                        eye_color VARCHAR(50) NOT NULL,
                        hair_color VARCHAR(50)
);

CREATE TABLE labwork (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         coordinates_id INTEGER NOT NULL REFERENCES coordinates(id) ON DELETE CASCADE,
                         creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
                         minimal_point BIGINT CHECK (minimal_point > 0),
                         difficulty VARCHAR(50),
                         author_id INTEGER REFERENCES person(id) ON DELETE SET NULL,
                         user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

COMMIT;
