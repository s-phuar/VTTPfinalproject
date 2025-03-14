create database final_project;
use final_project;

--     source enum('local', 'google') not null default 'local', -- local for normal users, google for google users 
--    source_id VARCHAR(255) unique null -- allow null, stores oauth2 ID from google
CREATE TABLE users (
    email VARCHAR(255) unique not null primary key,
    name VARCHAR(255) not null
);

CREATE TABLE credentials (
    users_email VARCHAR(255) unique not null primary key,
    password VARCHAR(255) not null, -- only used for local users, ignored for oauth2 users
    constraint fk_email foreign key(users_email) REFERENCES users(email) ON DELETE CASCADE
);
drop table users;
drop table credentials;

select * from users;
select * from credentials;






