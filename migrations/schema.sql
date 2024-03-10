--liquibase formatted sql

--changeset listok:1
CREATE TABLE IF NOT EXISTS chat
(
	chat_id bigint primary key
);

--changeset listok:2
CREATE TABLE IF NOT EXISTS link
(
	link_id bigint primary key,
	url text unique not null,
	last_check timestamp with time zone not null
);

--changeset listok:3
CREATE TABLE IF NOT EXISTS chat_link_map
(
	chat_id bigint references chat(chat_id),
	link_id bigint references link(link_id)
);
