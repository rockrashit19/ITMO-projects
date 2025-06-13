CREATE TABLE users (
                       id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       username VARCHAR2(50) UNIQUE NOT NULL,
                       password_hash VARCHAR2(255) NOT NULL
);


CREATE TABLE points (
                        id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        user_id NUMBER NOT NULL,
                        x NUMBER(10,2) NOT NULL,
                        y NUMBER(10,2) NOT NULL,
                        r NUMBER(10,2) NOT NULL,
                        is_inside NUMBER(1) CHECK (is_inside IN (0,1)),
                        timestamp TIMESTAMP NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (username, password_hash) VALUES ('test', 'a665a45920422f9d417e4867efdc4fb8a04a1f3b5f0e7a8c9d0b1e2f3a4c5d6');

SELECT * FROM users;
SELECT * FROM points;