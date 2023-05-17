DROP TABLE IF EXISTS endpoint_hits;

create table if not exists endpoint_hits
(
    id
    bigint
    generated
    by
    default as
    identity
    primary
    key,
    app
    varchar
(
    300
) not null,
    uri varchar
(
    500
) not null,
    ip varchar
(
    500
) not null,
    timestamp timestamp without time zone not null
    );
