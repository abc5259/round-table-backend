package com.roundtable.roundtable.domain.chore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ChoreMemberBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertChoreMembers(ChoreUniqueMatcher matcher, List<Chore> idChores) {

        List<ChoreMember> choreMembers = idChores.stream()
                .flatMap(idChore -> matcher.getChoreMembers(idChore).stream())
                .toList();

        saveAll(choreMembers);
    }

    private void saveAll(List<ChoreMember> choreMembers) {
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
