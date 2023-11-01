package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.FileId;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        return new ResourceEntity()
                .setId(new ResourceId(id))
                .setFileId(file.getId())
                .setStatus(ResourceStatus.NEW)
                .setUrl(url.toString())
                .setCreatedAt(createdAt);
    }

    public Boolean exists(URL url) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM resource WHERE url=?)", Boolean.class, url.toString());
    }

    public Optional<ResourceEntity> findByUrl(String url) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM resource WHERE url=?", PostgresResourceRepository::toResource, url)
        );
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
        Integer fileIdValue = rs.getInt("file_id") == 0 ? null : rs.getInt("file_id");
        return new ResourceEntity()
                .setId(new ResourceId(rs.getInt("id")))
                .setFileId(new FileId(fileIdValue))
                .setStatus(ResourceStatus.NEW)
                .setUrl(rs.getString("url"))
                .setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
    }

    public ResourceEntity create(ResourceCommand resourceCommand) {
        LocalDateTime createdAt = LocalDateTime.now();
        String url = resourceCommand.getUrl().toString();
        String sql = "INSERT INTO resource(url, status, created_at) VALUES(?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setString(1, resourceCommand.getUrl().toString());
                    ps.setString(2, ResourceStatus.NEW.name());
                    ps.setTimestamp(3, Timestamp.valueOf(createdAt));
                    return ps;
                }
                , keyHolder);

        int id = (int) keyHolder.getKeys().get(idColumn);

        return new ResourceEntity()
                .setId(new ResourceId(id))
                .setStatus(ResourceStatus.NEW)
                .setUrl(url)
                .setCreatedAt(createdAt);
    }
}
