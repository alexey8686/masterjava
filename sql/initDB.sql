
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_group;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS city;
DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS common_seq;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL
);

