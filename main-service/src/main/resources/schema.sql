CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    email
    VARCHAR
(
    255
) UNIQUE NOT NULL,
    name VARCHAR
(
    250
)
    );

CREATE TABLE IF NOT EXISTS categories
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    50
) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS locations
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    lat
    FLOAT
    NOT
    NULL,
    lon
    FLOAT
    NOT
    NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    title
    VARCHAR
(
    121
) NOT NULL,
    annotation VARCHAR
(
    2001
) NOT NULL,
    description VARCHAR
(
    7001
) NOT NULL,
    category_id BIGINT,
    location_id BIGINT,
    initiator_id BIGINT,
    event_date timestamp WITHOUT TIME ZONE,
    created_on timestamp
                         WITHOUT TIME ZONE,
    published_on timestamp
                         WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    paid BOOLEAN,
    participant_limit INTEGER,
    state VARCHAR
(
    20
),
    confirmed_requests BIGINT,
    views BIGINT,
    CONSTRAINT fk_category_events FOREIGN KEY
(
    category_id
) REFERENCES categories
(
    id
)
                         ON DELETE CASCADE,
    CONSTRAINT fk_location_events FOREIGN KEY
(
    location_id
) REFERENCES locations
(
    id
)
                         ON DELETE CASCADE,
    CONSTRAINT fk_user_events FOREIGN KEY
(
    initiator_id
) REFERENCES users
(
    id
)
                         ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS compilations
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    title
    VARCHAR
(
    50
) NOT NULL,
    pinned BOOLEAN
    );

CREATE TABLE IF NOT EXISTS participation_requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    event_id
    BIGINT,
    requester_id
    BIGINT,
    state
    VARCHAR
(
    20
),
    created timestamp WITHOUT TIME ZONE,
    CONSTRAINT fk_event_requesters FOREIGN KEY
(
    event_id
) REFERENCES events
(
    id
)
                      ON DELETE CASCADE,
    CONSTRAINT fk_requester_requests FOREIGN KEY
(
    requester_id
) REFERENCES users
(
    id
)
                      ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id
    BIGINT,
    event_id
    BIGINT,
    PRIMARY
    KEY
(
    compilation_id,
    event_id
),
    CONSTRAINT fk_compilation_events_event
    FOREIGN KEY
(
    event_id
) REFERENCES events
(
    id
)
    ON DELETE CASCADE,
    CONSTRAINT fk_compilation_events_compilation
    FOREIGN KEY
(
    compilation_id
) REFERENCES compilations
(
    id
)
    ON DELETE CASCADE
    );