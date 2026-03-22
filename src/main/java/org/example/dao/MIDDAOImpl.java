package org.example.dao;

import org.example.model.MID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MIDDAOImpl implements MIDDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public MID create(MID mid) {
        String sql = """
        INSERT INTO mid 
        (mid, is_mastercard_enabled, is_visa_enabled, is_tng_enabled, 
         is_duitnow_enabled, is_boost_enabled, is_grab_enabled, created_by, created_datetime, modified_datetime)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
        RETURNING id
        """;

        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                mid.getMid(),
                mid.getIsMastercardEnabled(),
                mid.getIsVisaEnabled(),
                mid.getIsTngEnabled(),
                mid.getIsDuitnowEnabled(),
                mid.getIsBoostEnabled(),
                mid.getIsGrabEnabled(),
                mid.getCreatedBy()
        );

        mid.setId(id);
        return mid;
    }

    @Override
    public MID findById(Long id) {
        String sql = """
                select id, mid, created_datetime, modified_datetime, created_by, modified_by
                from mid
                where id = ?
                """;

        List<MID> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MID.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public MID findByMid(String mid) {
        String sql = """
                select id, mid, created_datetime, modified_datetime, created_by, modified_by
                from mid
                where mid = ?
                """;

        List<MID> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MID.class), mid);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<MID> findAll() {
        String sql = """
                select id, mid, created_datetime, modified_datetime, created_by, modified_by
                from mid
                order by created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MID.class));
    }

    @Override
    public MID update(MID mid) {
        String sql = """
                update mid
                set mid = ?, modified_datetime = now(), modified_by = ?
                where id = ?
                """;

        jdbcTemplate.update(sql,
                mid.getMid(),
                "SYSTEM",
                mid.getId());

        return mid;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "delete from mid where id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public boolean existsByMid(String mid) {
        String sql = "select count(*) from mid where mid = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mid);
        return count != null && count > 0;
    }
}
