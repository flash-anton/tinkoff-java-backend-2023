create table if not exists chat
(
    id bigint primary key
);

create table if not exists link
(
    url     text primary key,
    updated timestamptz not null default '1970-01-01T00:00:00Z'
);

create table if not exists chat_link
(
    chat_id  bigint references chat on delete cascade,
    link_url text references link on delete cascade,
    primary key( chat_id, link_url )
);

-- trigger
create or replace function remove_unreferenced_links() returns trigger as $$
begin
    delete from link l where not exists (select from chat_link cl where cl.link_url = l.url);
    return null;
end;
$$ language plpgsql;

create trigger remove_unreferenced_links
after delete on chat_link
execute procedure remove_unreferenced_links();
