CREATE TABLE Users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(40) NOT NULL
);

CREATE TABLE Persons (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         birthday TIMESTAMP WITH TIME ZONE NOT NULL,
                         passportID VARCHAR(255) NOT NULL,
                         eyeColour VARCHAR(50) NOT NULL,
                         hairColour VARCHAR(50)
);

CREATE TABLE LabWorks (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          coordinates_x DOUBLE PRECISION NOT NULL,
                          coordinates_y INT NOT NULL,
                          creationDate TIMESTAMP WITH TIME ZONE NOT NULL,
                          minimalPoint BIGINT,
                          difficulty VARCHAR(50) NOT NULL,
                          author_id INT NOT NULL REFERENCES Persons(id),
                          user_id INT NOT NULL REFERENCES Users(id)
);