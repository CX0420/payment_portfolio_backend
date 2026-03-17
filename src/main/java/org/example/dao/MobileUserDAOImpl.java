package org.example.dao;

import org.example.dto.MobileUser;
import org.example.supabase.SupabaseClient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MobileUserDAOImpl implements MobileUserDAO{

    @Override
    public List<MobileUser> getMobileUserList() {
        String sql = """
                select
                  id,
                  mobile_user_id,
                  mobile_user_name,
                  pin,
                  email,
                  activated_date,
                  wrong_password_count,
                  created_datetime,
                  modified_datetime
                from mobile_user
                order by id desc
                """;

        List<MobileUser> out = new ArrayList<>();
        SupabaseClient client = new SupabaseClient();

        try (Connection conn = client.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MobileUser u = new MobileUser();

                u.setId(rs.getLong("id"));
                u.setMobileUserId(rs.getString("mobile_user_id"));
                u.setMobileUserName(rs.getString("mobile_user_name"));
                u.setPin(rs.getString("pin"));
                u.setEmail(rs.getString("email"));
                u.setActivatedDate(toLocalDateTime(rs.getTimestamp("activated_date")));
                u.setWrongPasswordCount(rs.getInt("wrong_password_count"));
                u.setCreatedDatetime(toLocalDateTime(rs.getTimestamp("created_datetime")));
                u.setModifiedDatetime(toLocalDateTime(rs.getTimestamp("modified_datetime")));

                out.add(u);
            }

            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch mobile_user list", e);
        }
    }

    private static LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
