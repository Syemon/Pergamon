CREATE TABLE content
(
    id SERIAL PRIMARY KEY,
    name text,
    storage_name text,
    type text,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone
);

CREATE TABLE resource
(
    id SERIAL PRIMARY KEY,
    content_id int,
    url text unique,
    status text,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    attempt_number int,
    CONSTRAINT fk_file
        FOREIGN KEY(content_id)
            REFERENCES content(id)
)
