CREATE TABLE location
(
    location_id bigint  NOT NULL ,
    x  integer NOT NULL,
    y  integer NOT NULL,
    PRIMARY KEY (location_id)
);

CREATE TABLE kingdom
(
    kingdom_id bigint NOT NULL ,
    name varchar(255),
    user_id bigint,
    location_id bigint,
    PRIMARY KEY (kingdom_id),
    CONSTRAINT FK_LocationKingdom FOREIGN KEY (location_id) REFERENCES location(location_id)
);

CREATE TABLE building
(
    building_id bigint NOT NULL ,
    finished_at bigint,
    hp integer NOT NULL ,
    level integer NOT NULL,
    started_at bigint,
    type integer,
    updated_at bigint,
    kingdom_id bigint,
    PRIMARY KEY (building_id),
    CONSTRAINT FK_KingdomBuilding FOREIGN KEY (kingdom_id) REFERENCES kingdom(kingdom_id)
);

CREATE TABLE resource
(
    resource_id         bigint  NOT NULL ,
    amount     integer NOT NULL,
    generation integer NOT NULL,
    type       integer,
    kingdom_id bigint,
    PRIMARY KEY (resource_id),
    CONSTRAINT FK_KingdomResource FOREIGN KEY (kingdom_id) REFERENCES kingdom(kingdom_id)
);

CREATE TABLE troop
(
    troop_id bigint NOT NULL ,
    attack integer NOT NULL,
    defence integer NOT NULL,
    finished_at bigint NOT NULL,
    hp integer NOT NULL,
    level integer NOT NULL,
    started_at bigint NOT NULL,
    kingdom_id bigint,
    PRIMARY KEY (troop_id),
    CONSTRAINT FK_KingdomTroop FOREIGN KEY (kingdom_id) REFERENCES kingdom(kingdom_id)
);

CREATE TABLE user
(
    user_id           bigint NOT NULL,
    full_name         varchar(255),
    is_email_verified bit    NOT NULL,
    password          varchar(255),
    username          varchar(255),
    verification_code varchar(255),
    auth_grant_access_token_id bigint,
    kingdom_id        bigint,
    PRIMARY KEY (user_id),
    CONSTRAINT FK_KingdomUser FOREIGN KEY (kingdom_id) REFERENCES kingdom(kingdom_id)
);

CREATE TABLE auth_grant_access_token
(
    agat_id bigint NOT NULL ,
    access_grant_token varchar(255),
    id_external varchar(255),
    user_id bigint,
    PRIMARY KEY (agat_id),
    CONSTRAINT FK_UserAGAT FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE black_listed_token
(
    blt_id bigint NOT NULL ,
    token varchar(255),
    PRIMARY KEY (blt_id)
);