create table if not exists chat
(
    id bigint primary key
);

create table if not exists link
(
    url     text primary key,
    updated timestamptz default '1970-01-01T00:00:00Z'
);

create table if not exists chat_link
(
    chat_id  bigint references chat on delete cascade,
    link_url text references link on delete cascade,
    primary key( chat_id, link_url )
);
