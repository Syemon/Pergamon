package com.pergamon.Pergamon.v1.dao;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FileId;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class PostgresFileRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresFileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public File save(FilePropertiesPojo filePropertiesPojo) {
        LocalDateTime createdAt = LocalDateTime.now();
        String sql = "INSERT INTO file(name, storage_name, type, created_at) VALUES(?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setString(1, filePropertiesPojo.getName());
                    ps.setString(2, filePropertiesPojo.getStorageName());
                    ps.setString(3, filePropertiesPojo.getType());
                    ps.setTimestamp(4, Timestamp.valueOf(createdAt));
                    return ps;
                }
                , keyHolder);

        int id = (int) keyHolder.getKeys().get(idColumn);

        return new File(
                new FileId(id),
                filePropertiesPojo.getName(),
                filePropertiesPojo.getStorageName(),
                filePropertiesPojo.getType(),
                createdAt,
                null
        );
    }

    public void update(File file) {
        jdbcTemplate.update("UPDATE file SET name=?, storage_name=?, type=?, updated_at=NOW()", file.getName(), file.getStorageName(), file.getType());
    }

    public File findByUrl(URL url) {
        return jdbcTemplate.queryForObject("""
                        SELECT f.*
                        FROM resource AS r 
                        INNER JOIN file AS f 
                        ON r.file_id = f.id 
                        WHERE r.url = ?
                        """, PostgresFileRepository::toFile, url.toString());

    }

    public Optional<File> findById(FileId id) {
        try {
            File result = jdbcTemplate.queryForObject("SELECT * FROM file f WHERE f.id = ?", PostgresFileRepository::toFile, id.id());
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static File toFile(ResultSet rs, int rowNum) throws SQLException {
        return new File(
                new FileId(rs.getInt("id")),
                rs.getString("name"),
                rs.getString("storage_name"),
                rs.getString("type"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class)
        );
    }
}
