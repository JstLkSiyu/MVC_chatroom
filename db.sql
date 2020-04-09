
-- auto-generated definition
create table users
(
    uid      char(18)                            not null
        primary key,
    uname    varchar(30)                         not null,
    password char(34)                            not null,
    gender   enum ('man', 'woman', 'unknown')    null,
    birthday timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

-- auto-generated definition
create table relation
(
    u1id char(18)                            null,
    u2id char(18)                            null,
    day  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint relate_pk
        unique (u1id, u2id),
    constraint relation_ibfk_1
        foreign key (u1id) references users (uid)
            on update cascade on delete cascade,
    constraint relation_ibfk_2
        foreign key (u2id) references users (uid)
            on update cascade on delete cascade
);

-- auto-generated definition
create table msg_log
(
    `from`   varchar(18)                         null,
    `to`     varchar(18)                         null,
    msg      varchar(400)                        not null,
    log_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    status   enum ('unread', 'read')             not null,
    constraint msg_log_users_uid_fk
        foreign key (`from`) references users (uid)
            on update cascade on delete cascade,
    constraint msg_log_users_uid_fk_2
        foreign key (`to`) references users (uid)
            on update cascade on delete cascade
);

-- auto-generated definition
create table friend_request
(
    `from` char(18)                                                    not null,
    `to`   char(18)                                                    not null,
    status enum ('incomplete', 'reject', 'agree') default 'incomplete' not null,
    primary key (`to`, `from`),
    constraint friend_request_users_uid_fk
        foreign key (`from`) references users (uid)
            on update cascade on delete cascade,
    constraint friend_request_users_uid_fk_2
        foreign key (`to`) references users (uid)
            on update cascade on delete cascade
);


-- auto-generated definition
create table friend_appraise
(
    `from`   char(18)     not null,
    `to`     char(18)     not null,
    appraise varchar(100) not null,
    primary key (`from`, `to`, appraise),
    constraint friend_appraise_users_uid_fk
        foreign key (`from`) references users (uid)
            on update cascade on delete cascade,
    constraint friend_appraise_users_uid_fk_2
        foreign key (`to`) references users (uid)
            on update cascade on delete cascade
);

