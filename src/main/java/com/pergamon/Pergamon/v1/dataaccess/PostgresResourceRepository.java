package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.FileId;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PostgresResourceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresResourceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResourceEntity save(FileEntity file, URL url) {
        LocalDateTime createdAt = LocalDateTime.now();
        String sql = "INSERT INTO resource(url, file_id, created_at) VALUES(?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setString(1, url.toString());
                    ps.setInt(2, file.getId().id());
                    ps.setTimestamp(3, Timestamp.valueOf(createdAt));
                    return ps;
                }
                , keyHolder);

        int id = (int) keyHolder.getKeys().get(idColumn);

        return new ResourceEntity(
                new ResourceId(id),
                file.getId(),
                url.toString(),
                createdAt
        );
    }

    public Boolean exists(URL url) {
        System.out.println(url.toString());
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM resource WHERE url=?)", Boolean.class, url.toString());
    }

    public List<ResourceEntity> list() {
        return jdbcTemplate.query("SELECT * FROM resource",
                (PostgresResourceRepository::toResource));
    }

    public List<ResourceEntity> list(String search) {
        return jdbcTemplate.query("SELECT * FROM resource WHERE url ilike '%'||?||'%'",
                (PostgresResourceRepository::toResource), search);
    }

    private static ResourceEntity toResource(ResultSet rs, int rowNum) throws SQLException {
        return new ResourceEntity(
                new ResourceId(rs.getInt("id")),
                new FileId(rs.getInt("file_id")),
                rs.getString("url"),
                rs.getObject("created_at", LocalDateTime.class));
    }
}