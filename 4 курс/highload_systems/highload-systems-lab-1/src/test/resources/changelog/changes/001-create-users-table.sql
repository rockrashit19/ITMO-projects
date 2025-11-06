--liquibase formatted sql

--changeset developer:create-users-table
CREATE TABLE users (
    login VARCHAR(16) NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    invited_count INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT users_pk PRIMARY KEY (login)
);

--rollback DROP TABLE users;

--changeset developer:create-projects-table
CREATE TABLE projects (
    id INTEGER PRIMARY KEY,
    creator_user_login VARCHAR(16) NOT NULL,
    token_balance DECIMAL(14, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_projects_creator FOREIGN KEY (creator_user_login) REFERENCES users(login)
);

--rollback DROP TABLE projects;

--changeset developer:create-access-keys-table
CREATE TABLE access_keys (
    "key" VARCHAR(36) PRIMARY KEY,
    user_login VARCHAR(16) NOT NULL,
    usages_limit INTEGER NULL,
    usages_count INTEGER NOT NULL DEFAULT 0,
    lifespan_end_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_access_keys_user FOREIGN KEY (user_login) REFERENCES users(login)
);

--rollback DROP TABLE access_keys;

--changeset developer:create-user-projects-table
CREATE TABLE user_projects (
    user_login VARCHAR(16) NOT NULL,
    project_id INTEGER NOT NULL,
    notification_email VARCHAR(64) NULL,
    PRIMARY KEY (user_login, project_id),
    CONSTRAINT fk_user_projects_user FOREIGN KEY (user_login) REFERENCES users(login),
    CONSTRAINT fk_user_projects_project FOREIGN KEY (project_id) REFERENCES projects(id)
);

--rollback DROP TABLE user_projects;

--changeset developer:create-chats-table
CREATE TABLE chats (
    id INTEGER PRIMARY KEY,
    chat_name VARCHAR(255) NOT NULL,
    project_id INTEGER NOT NULL,
    CONSTRAINT fk_chats_project FOREIGN KEY (project_id) REFERENCES projects(id)
);

--rollback DROP TABLE chats;

--changeset developer:create-user-chat-subscriptions-table
CREATE TABLE user_chat_subscriptions (
    user_login VARCHAR(16) NOT NULL,
    chat_id INTEGER NOT NULL,
    PRIMARY KEY (user_login, chat_id),
    CONSTRAINT fk_user_chat_subscriptions_user FOREIGN KEY (user_login) REFERENCES users(login),
    CONSTRAINT fk_user_chat_subscriptions_chat FOREIGN KEY (chat_id) REFERENCES chats(id)
);

--rollback DROP TABLE user_chat_subscriptions;

--changeset developer:create-files-table
CREATE TABLE files (
    id INTEGER PRIMARY KEY,
    project_id INTEGER NOT NULL,
    uploader_user_login VARCHAR(16) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    storage_link VARCHAR(512) NOT NULL,
    CONSTRAINT fk_files_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_files_uploader FOREIGN KEY (uploader_user_login) REFERENCES users(login)
);

--rollback DROP TABLE files;
