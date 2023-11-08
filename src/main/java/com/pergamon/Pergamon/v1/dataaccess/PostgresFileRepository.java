package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.FileId;
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

    public FileEntity save(FileEntity fileEntity) {
        LocalDateTime createdAt = LocalDateTime.now();
        String sql = "INSERT INTO file(name, storage_name, type, created_at) VALUES(?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setString(1, fileEntity.getName());
                    ps.setString(2, fileEntity.getStorageName());
                    ps.setString(3, fileEntity.getType());
                    ps.setTimestamp(4, Timestamp.valueOf(createdAt));
                    return ps;
                }
                , keyHolder);

        int id = (int) keyHolder.getKeys().get(idColumn);

        return fileEntity.setCreatedAt(createdAt)
                .setId(new FileId(id));
    }

    public void update(FileEntity file) {
        jdbcTemplate.update("UPDATE file SET name=?, storage_name=?, type=?, updated_at=NOW()", file.getName(), file.getStorageName(), file.getType());
    }

    public FileEntity findByUrl(URL url) {
        return jdbcTemplate.queryForObject("""
                        SELECT f.*
                        FROM resource AS r 
                        INNER JOIN file AS f 
                        ON r.file_id = f.id 
                        WHERE r.url = ?
                        """, PostgresFileRepository::toFile, url.toString());

    }

    public Optional<FileEntity> findById(FileId id) {
        try {
            FileEntity result = jdbcTemplate.queryForObject("SELECT * FROM file f WHERE f.id = ?", PostgresFileRepository::toFile, id.id());
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static FileEntity toFile(ResultSet rs, int rowNum) throws SQLException {
        return new FileEntity(
                new FileId(rs.getInt("id")),
                rs.getString("name"),
                rs.getString("storage_name"),
                rs.getString("type"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class)
        );
    }
}
