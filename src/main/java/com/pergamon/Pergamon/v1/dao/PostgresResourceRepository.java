package com.pergamon.Pergamon.v1.dao;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FileId;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.entity.Resource;
import com.pergamon.Pergamon.v1.entity.ResourceId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class PostgresResourceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresResourceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public void save(File file, URL url) {
//        jdbcTemplate.update("INSERT INTO resource(url, file_id) VALUES(?, ?)", file.getId().id(), url);
//    }

    public Resource save(File file, URL url) {
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

        return new Resource(
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

    public List<Resource> list() {
        return jdbcTemplate.query("SELECT * FROM resource",
                (PostgresResourceRepository::toResource));
    }

    public List<Resource> list(String search) {
        return jdbcTemplate.query("SELECT * FROM resource WHERE url ilike '%'||?||'%'",
                (PostgresResourceRepository::toResource), search);
    }

    private static Resource toResource(ResultSet rs, int rowNum) throws SQLException {
        return new Resource(
                new ResourceId(rs.getInt("id")),
                new FileId(rs.getInt("file_id")),
                rs.getString("url"),
                rs.getObject("created_at", LocalDateTime.class));
    }
}
