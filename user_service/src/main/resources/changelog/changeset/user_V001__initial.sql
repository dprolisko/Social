CREATE TABLE country (
                         id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                         country_name VARCHAR(64) NOT NULL
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(32) NOT NULL,
    email VARCHAR(64) UNIQUE  NOT NULL,
    phone VARCHAR(16) UNIQUE NOT NULL,
    active boolean default true NOT NULL,
    bio VARCHAR(512),
    createdAt timestamptz DEFAULT current_timestamp,
    updateAt timestamptz DEFAULT current_timestamp,
    profile_pic_file_id TEXT,
    profile_pic_small_file_id TEXT,
    country_id BIGINT NOT NULL,

    CONSTRAINT fk_country_id FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE events(
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                       name VARCHAR(16) NOT NULL,
                       description VARCHAR(256),
                       start_date timestamptz DEFAULT current_timestamp,
                       end_date timestamptz DEFAULT current_timestamp,
                       createAt timestamptz DEFAULT current_timestamp,
                       updateAt timestamptz DEFAULT current_timestamp,
                       count BIGINT NOT NULL,
                       type VARCHAR,
                       status VARCHAR,
                       user_id BIGINT NOT NULL,

                       CONSTRAINT fk_events_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE Jira (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    username VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    url VARCHAR UNIQUE NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE Premium(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id BIGINT UNIQUE NOT NULL,
    start_date_time timestamptz DEFAULT current_timestamp,
    end_date_time timestamptz,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE Rating(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    rating INT,
    user_id BIGINT UNIQUE NOT NULL,
    created_datatime timestamptz DEFAULT current_timestamp,
    updated_datatime timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE skills(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    skillName VARCHAR(32) NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    updatedAt timestamptz DEFAULT current_timestamp,
    user_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE SkillGuarantee(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id BIGINT UNIQUE NOT NULL,
    userguarentee_id BIGINT UNIQUE NOT NULL,
    skill_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_userguarantee_id FOREIGN KEY (userguarentee_id) REFERENCES users(id),
    CONSTRAINT fk_skill_id FOREIGN KEY (skill_id) REFERENCES  skills(id)
);

CREATE TABLE goal(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    title VARCHAR(32) NOT NULL,
    description VARCHAR(128),
    createdAt timestamptz DEFAULT current_timestamp,
    updateAt timestamptz DEFAULT current_timestamp,
    deadline timestamptz,
    status SMALLINT DEFAULT 0 NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,
    goal BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_goal_id FOREIGN KEY (goal) REFERENCES users(id)
);

CREATE TABLE goal_invitation(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    goal_id BIGINT UNIQUE NOT NULL,
    inviter_id BIGINT UNIQUE NOT NULL,
    invited_id BIGINT UNIQUE NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    updatedAT timestamptz DEFAULT current_timestamp,
    status SMALLINT DEFAULT 0 NOT NULL,

    CONSTRAINT fk_goal_id FOREIGN KEY (goal_id) REFERENCES goal(id),
    CONSTRAINT fk_inviter_id FOREIGN KEY (inviter_id) REFERENCES users(id),
    CONSTRAINT fk_invited_id FOREIGN KEY (invited_id) REFERENCES users(id)
);

CREATE TABLE contact(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    contact VARCHAR(128) UNIQUE,
    type SMALLINT DEFAULT 0 NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE contact_preference(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    prefered_type SMALLINT DEFAULT 0 NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE subscription(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    follower_id BIGINT NOT NULL ,
    followee_id BIGINT NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    updatedAt timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES users(id),
    CONSTRAINT fk_following_id FOREIGN KEY (followee_id) REFERENCES users(id)
);

CREATE TABLE ContentData(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    content OID
);