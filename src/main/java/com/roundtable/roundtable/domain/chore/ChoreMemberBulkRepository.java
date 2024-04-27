package com.roundtable.roundtable.domain.chore;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class ChoreMemberBulkRepository {

    private final JdbcTemplate jdbcTemplate;


    public void insertChoreMembers(ChoreUniqueMatcher matcher, List<Chore> idChores) {

        List<ChoreMember> choreMembers = matcher.getChoreMembers(idChores);
        saveAll(choreMembers);

    }


    public void saveAll(List<ChoreMember> choreMembers) {
        String sql = "INSERT INTO chore_member (chore_id, member_id, created_at, updated_at) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ChoreMember choreMember = choreMembers.get(i);
                        ps.setLong(1, choreMember.getChore().getId());
                        ps.setLong(2, choreMember.getMember().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return choreMembers.size();
                    }
                });
    }
}
