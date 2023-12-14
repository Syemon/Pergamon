package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostgresResourceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresResourceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean exists(URL url) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM resource WHERE url=?)", Boolean.class, url.toString());
    }

    public Optional<ResourceEntity> findByUrl(String url) {
        return jdbcTemplate.query(
                "SELECT * FROM resource WHERE url=?",
                PostgresResourceRepository::toResource,
                url)
                .stream()
                .findFirst();
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
        return new ResourceEntity()
                .setId(rs.getInt("id"))
                .setFileId(rs.getObject("content_id", Integer.class))
                .setStatus(ResourceStatus.valueOf(rs.getString("status")))
                .setUrl(rs.getString("url"))
                .setCreatedAt(rs.getObject("created_at", OffsetDateTime.class))
                .setModifiedAt(rs.getObject("updated_at", OffsetDateTime.class));
    }

    public ResourceEntity create(ResourceCommand resourceCommand) {
        OffsetDateTime createdAt = OffsetDateTime.now();
        String url = resourceCommand.getUrl().toString();
        String sql = "INSERT INTO resource(url, status, created_at) VALUES(?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setString(1, resourceCommand.getUrl().toString());
                    ps.setString(2, ResourceStatus.NEW.name());
                    ps.setObject(3, createdAt);
                    return ps;
                }
                , keyHolder);

        int id = (int) keyHolder.getKeys().get(idColumn);

        return new ResourceEntity()
                .setId(id)
                .setStatus(ResourceStatus.NEW)
                .setUrl(url)
                .setCreatedAt(createdAt);
    }

    public ResourceEntity save(Resource resource) {
        OffsetDateTime modifiedAt = OffsetDateTime.now();
        Integer fileId = resource.getContentId() != null ? resource.getContentId().id() : null;
        String sql = """
            UPDATE
                resource
            SET
                content_id=?,
                url=?, 
                updated_at=?, 
                status=?, 
                attempt_number=?
            WHERE
                id = ?
                """;
        String idColumn = "id";
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{idColumn});
                    ps.setObject(1, fileId);
                    ps.setString(2, resource.getUrl().toString());
                    ps.setObject(3, modifiedAt);
                    ps.setString(4, resource.getStatus().name());
                    ps.setInt(5, resource.getAttemptNumber());
                    ps.setInt(6, resource.getId().id());
                    return ps;
                });

        resource.setModifiedAt(modifiedAt);
        return new ResourceEntity()
                .setId(resource.getId().id())
                .setStatus(resource.getStatus())
                .setUrl(resource.getUrl().toString())
                .setCreatedAt(resource.getCreatedAt())
                .setModifiedAt(resource.getModifiedAt())
                .setFileId(fileId)
                .setAttemptNumber(resource.getAttemptNumber());
    }
}
