CREATE TABLE file
(
    id SERIAL PRIMARY KEY,
    name text,
    storage_name text,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone
);

CREATE TABLE resource
(
    id SERIAL PRIMARY KEY,
    file_id int,
    name text,
    status text,
    storage_name text,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    CONSTRAINT fk_file
        FOREIGN KEY(file_id)
            REFERENCES file(id)
)
