CREATE TABLE file
(
    id SERIAL PRIMARY KEY,
    name text,
    storage_name text,
    type text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone
);

CREATE TABLE resource
(
    id SERIAL PRIMARY KEY,
    file_id int,
    url text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    CONSTRAINT fk_file
        FOREIGN KEY(file_id)
            REFERENCES file(id)
)
