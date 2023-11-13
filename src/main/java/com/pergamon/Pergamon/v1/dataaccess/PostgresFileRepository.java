package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.ContentId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

public class PostgresFileRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresFileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ContentEntity save(ContentEntity contentEntity) {
        OffsetDateTime createdAt = OffsetDateTime.now();
        String sql = "INSERT INTO content(name, storage_name, type, created_at) VALUES(?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setString(1, contentEntity.getName());
                    ps.setString(2, contentEntity.getStorageName());
                    ps.setString(3, contentEntity.getType());
                    ps.setObject(4, createdAt);
                    return ps;
                }
                , keyHolder);

        int id = (int) keyHolder.getKeys().get(idColumn);

        return contentEntity.setCreatedAt(createdAt)
                .setId(new ContentId(id));
    }

    public void update(ContentEntity file) {
        jdbcTemplate.update("UPDATE content SET name=?, storage_name=?, type=?, updated_at=NOW()", file.getName(), file.getStorageName(), file.getType());
    }

    public ContentEntity findByUrl(URL url) {
        return jdbcTemplate.queryForObject("""
                        SELECT f.*
                        FROM resource AS r 
                        INNER JOIN content AS f 
                        ON r.content_id = f.id 
                        WHERE r.url = ?
                        """, PostgresFileRepository::toFile, url.toString());

    }

    public Optional<ContentEntity> findById(ContentId id) {
        try {
            ContentEntity result = jdbcTemplate.queryForObject("SELECT * FROM content f WHERE f.id = ?", PostgresFileRepository::toFile, id.id());
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static ContentEntity toFile(ResultSet rs, int rowNum) throws SQLException {
        return new ContentEntity(
                new ContentId(rs.getInt("id")),
                rs.getString("name"),
                rs.getString("storage_name"),
                rs.getString("type"),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getObject("updated_at", OffsetDateTime.class)
        );
    }
}
