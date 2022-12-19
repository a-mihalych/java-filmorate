create table IF NOT EXISTS GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(14) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);
create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(5) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);
create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     CHARACTER VARYING(64) not null,
    LOGIN     CHARACTER VARYING(64) not null
        constraint USERS_UNIQUE_LOGIN
            unique,
    USER_NAME CHARACTER VARYING(64) not null,
    BIRTHDAY  DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);
create table IF NOT EXISTS FILMS
(
    FILM_ID     INTEGER auto_increment,
    FILM_NAME   CHARACTER VARYING(64)  not null
        constraint FILMS_UNIQUE_FILM_NAME
            unique,
    DESCRIPTION CHARACTER VARYING(256) not null,
    MPA_ID      INTEGER                not null,
    RELEASEDATE DATE                   not null,
    DURATION    INTEGER                not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);
create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);
create table IF NOT EXISTS GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint GENRES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint GENRES_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
            on delete cascade
);
create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (FRIEND_ID) references USERS
            on delete cascade,
    constraint FRIENDS_USERS_FRIEND_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);
