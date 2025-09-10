CREATE TABLE post (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    authorId BIGINT NOT NULL,
    authorName VARCHAR(32) NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    updatedAt timestamptz DEFAULT current_timestamp,
    content VARCHAR(4096),
    published BOOLEAN DEFAULT FALSE NOT NULL,
    publishedAt timestamptz DEFAULT current_timestamp,
    viewCount BIGINT NOT NULL,
    verifiedAt timestamptz DEFAULT current_timestamp,
    status VARCHAR NOT NULL
);

CREATE TABLE comment(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    authorId BIGINT NOT NULL,
    authorName VARCHAR(32) NOT NULL,
    content VARCHAR(4096) NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    publishedAt timestamptz DEFAULT current_timestamp,
    updatedAt timestamptz DEFAULT current_timestamp,
    published BOOLEAN DEFAULT FALSE NOT NULL,
    postId BIGINT NOT NULL,

    CONSTRAINT fk_post_id_comment FOREIGN KEY (postId) REFERENCES post (id),
);

CREATE TABLE hashtag(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    hashtag VARCHAR(16) NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    postId BIGINT NOT NULL,

    CONSTRAINT fk_post_id_hashtag FOREIGN KEY (postId) REFERENCES post (id)
);

CREATE TABLE resources(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    size BIGINT NOT NULL,
    key VARCHAR NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,
    deletedAt timestamptz DEFAULT current_timestamp,
    updatedAt timestamptz DEFAULT current_timestamp,
    type VARCHAR NOT NULL,
    title VARCHAR(32) NOT NULL,
    postId BIGINT NOT NULL,

    CONSTRAINT fk_post_id_resources FOREIGN KEY (postId) REFERENCES post (id)
);

CREATE TABLE commentLike(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    userId BIGINT NOT NULL,
    commentId BIGINT NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_comment_id_commentLike FOREIGN KEY (commentId) REFERENCES comment(id)
);

CREATE TABLE postLike(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    postId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    createdAt timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_post_id_postLike FOREIGN KEY (postId) REFERENCES post(id)
);