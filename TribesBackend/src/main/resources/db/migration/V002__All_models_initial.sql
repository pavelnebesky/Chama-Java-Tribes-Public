CREATE TABLE location
(
    id bigint  NOT NULL AUTO_INCREMENT,
    x  integer NOT NULL,
    y  integer NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE kingdom
(
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255),
    user_id bigint,
    location_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT FK_LocationKingdom FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE TABLE building
(
    id bigint NOT NULL AUTO_INCREMENT,
    finished_at bigint,
    hp integer NOT NULL ,
    level integer NOT NULL,
    started_at bigint,
    type integer,
    updated_at bigint,
    kingdom_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT FK_KingdomBuilding FOREIGN KEY (kingdom_id) REFERENCES kingdom(id)
);

CREATE TABLE resource
(
    id         bigint  NOT NULL AUTO_INCREMENT,
    amount     integer NOT NULL,
    generation integer NOT NULL,
    type       integer,
    kingdom_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT FK_KingdomResource FOREIGN KEY (kingdom_id) REFERENCES kingdom(id)
);

CREATE TABLE troop
(
    id bigint NOT NULL AUTO_INCREMENT,
    attack integer NOT NULL,
    defence integer NOT NULL,
    finished_at bigint NOT NULL,
    hp integer NOT NULL,
    level integer NOT NULL,
    started_at bigint NOT NULL,
    kingdom_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT FK_KingdomTroop FOREIGN KEY (kingdom_id) REFERENCES kingdom(id)
);

CREATE TABLE user
(
    id                bigint NOT NULL,
    full_name         varchar(255),
    is_email_verified bit    NOT NULL,
    password          varchar(255),
    username          varchar(255),
    verification_code varchar(255),
    auth_grant_access_token_id bigint,
    kingdom_id        bigint,
    PRIMARY KEY (id),
    CONSTRAINT FK_KingdomUser FOREIGN KEY (kingdom_id) REFERENCES kingdom(id)
);

CREATE TABLE auth_grant_access_token
(
    id bigint NOT NULL AUTO_INCREMENT,
    access_grant_token varchar(255),
    id_external varchar(255),
    user_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT FK_UserAGAT FOREIGN KEY (user_id) REFERENCES user(id)
);