-- Creating sequence for IDs
CREATE SEQUENCE labwork_id_seq;
CREATE SEQUENCE coordinates_id_seq;
CREATE SEQUENCE person_id_seq;

-- Users table for authentication
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(40) NOT NULL -- SHA-1 hash is 40 characters
);

-- Coordinates table
CREATE TABLE coordinates (
                             id SERIAL PRIMARY KEY,
                             x DOUBLE PRECISION NOT NULL,
                             y INTEGER NOT NULL CHECK (y > -563)
);

-- Person table
CREATE TABLE person (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        birthday TIMESTAMP WITH TIME ZONE NOT NULL,
                        passport_id VARCHAR(255) NOT NULL,
                        eye_color VARCHAR(50) NOT NULL,
                        hair_color VARCHAR(50)
);

-- LabWork table
CREATE TABLE labwork (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         coordinates_id INTEGER NOT NULL REFERENCES coordinates(id),
                         creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
                         minimal_point BIGINT,
                         difficulty VARCHAR(50),
                         author_id INTEGER REFERENCES person(id),
                         user_id INTEGER NOT NULL REFERENCES users(id),
                         CHECK (minimal_point > 0)
);

SELECT DISTINCT difficulty FROM labwork;