package org.example.dao;

import org.example.model.TID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TIDDAOImpl implements TIDDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TID create(TID tid) {
        String sql = """
            INSERT INTO tid 
            (tid, mobile_user_fk, mid_fk, stan, trace, created_datetime, modified_datetime, created_by, modified_by)
            VALUES (?, ?, ?, ?, ?, NOW(), NOW(), ?, ?)
            RETURNING id
            """;

        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                tid.getTid(),
                tid.getMobileUser() != null ? tid.getMobileUser().getId() : null,  // Gets the ID from saved user
                tid.getMid() != null ? tid.getMid().getId() : null,                // Gets the ID from saved MID
                tid.getStan(),
                tid.getTrace(),
                tid.getCreatedBy(),
                tid.getModifiedBy()
        );

        tid.setId(id);
        return tid;
    }

    @Override
    public TID findById(Long id) {
        String sql = """
                select id, tid, mobile_user_fk, mid_fk, created_datetime, modified_datetime, created_by, modified_by, stan, trace
                from tid
                where id = ?
                """;

        List<TID> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TID.class), id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public TID findByTid(String tid) {
        String sql = """
                select id, tid, mobile_user_fk, mid_fk, created_datetime, modified_datetime, created_by, modified_by, stan, trace
                from tid
                where tid = ?
                """;

        List<TID> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TID.class), tid);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<TID> findByMobileUserId(String mobileUserId) {
        String sql = """
                select t.id, t.tid, t.mobile_user_fk, t.mid_fk, t.created_datetime, t.modified_datetime, t.created_by, t.modified_by, t.stan, t.trace
                from tid t
                join mobile_user m on t.mobile_user_fk = m.id
                where m.mobile_user_id = ?
                order by t.created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TID.class), mobileUserId);
    }

    @Override
    public List<TID> findByMidId(Long midId) {
        String sql = """
                select id, tid, mobile_user_fk, mid_fk, created_datetime, modified_datetime, created_by, modified_by, stan, trace
                from tid
                where mid_fk = ?
                order by created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TID.class), midId);
    }

    @Override
    public List<TID> findAll() {
        String sql = """
                select id, tid, mobile_user_fk, mid_fk, created_datetime, modified_datetime, created_by, modified_by, stan, trace
                from tid
                order by created_datetime desc
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TID.class));
    }

    @Override
    public TID update(TID tid) {
        String sql = """
                update tid
                set stan = ?, trace = ?, modified_datetime = now(), modified_by = ?
                where id = ?
                """;

        jdbcTemplate.update(sql,
                tid.getStan(),
                tid.getTrace(),
                "SYSTEM",
                tid.getId());

        return tid;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "delete from tid where id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public boolean existsByTid(String tid) {
        String sql = "select count(*) from tid where tid = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tid);
        return count != null && count > 0;
    }
}
