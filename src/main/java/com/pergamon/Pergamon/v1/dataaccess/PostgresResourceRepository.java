package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentId;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import com.pergamon.Pergamon.v1.domain.ResourceRoot;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private static ResourceRoot toResourceRoot(ResultSet rs, int rowNum) throws SQLException {
        Content content = Content.builder()
                .id(new ContentId(rs.getInt("resource_content_id")))
                .name(rs.getString("content_name"))
                .storageName(rs.getString("content_storage_name"))
                .type(rs.getString("content_type"))
                .createdAt(rs.getObject("content_created_at", OffsetDateTime.class))
                .updatedAt(rs.getObject("content_updated_at", OffsetDateTime.class))
                .build();

        URL url;
        try {
            url = new URL(rs.getString("resource_url"));
        } catch (MalformedURLException e) {
            log.error("Received MalformedURLException. Was url edited in database?", e);
            throw new IllegalStateException("Malformed url when extracted from database", e);
        }

        return new ResourceRoot()
                .setId(new ResourceId(rs.getInt("resource_content_id")))
                .setContent(content)
                .setStatus(ResourceStatus.valueOf(rs.getString("resource_status")))
                .setUrl(url)
                .setAttemptNumber(rs.getInt("resource_attempt_number"))
                .setCreatedAt(rs.getObject("resource_created_at", OffsetDateTime.class))
                .setModifiedAt(rs.getObject("resource_updated_at", OffsetDateTime.class));
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

    public List<ResourceRoot> listResourceRootToRetry(Set<ResourceStatus> resourceStatuses, int olderThenInMinutes, int maxRetryCount) {
        List<String> statuses = resourceStatuses.stream().map(Enum::name).toList();
        String[] statusesParam = statuses.toArray(new String[0]);
        return jdbcTemplate.query("""
                        SELECT 
                            r.content_id as resource_content_id,
                            r.url as resource_url,
                            r.status as resource_status,
                            r.created_at as resource_created_at,
                            r.updated_at as resource_updated_at,
                            r.attempt_number as resource_attempt_number,
                            c.id as content_id,
                            c.name as content_name,
                            c.storage_name as content_storage_name,
                            c.type as content_type,
                            c.created_at as content_created_at,
                            c.updated_at as content_updated_at
                        FROM 
                            resource as r 
                        INNER JOIN
                            content as c on  r.content_id = c.id
                        WHERE
                            r.status = ANY(?)
                            and r.created_at < NOW() - make_interval(mins => ?)
                            and r.attempt_number < ?;
                """,
                (PostgresResourceRepository::toResourceRoot),
                statusesParam,
                olderThenInMinutes,
                maxRetryCount
        );
    }
}
