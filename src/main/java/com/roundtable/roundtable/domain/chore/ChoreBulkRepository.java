package com.roundtable.roundtable.domain.chore;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ChoreBulkRepository {

    private static final Integer DEFAULT_CHUNK_SIZE = 1_000;

    private final DataSource dataSource;

    private final SQLExceptionTranslator exTranslator;

    private final ChoreMatcherRepository choreMatcherRepository;

    private final ChoreMemberBulkRepository choreMemberBulkRepository;

    public ChoreBulkRepository(
            DataSource dataSource,
            ChoreMatcherRepository choreMatcherRepository,
            ChoreMemberBulkRepository choreMemberBulkRepository) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
        this.choreMatcherRepository = choreMatcherRepository;
        this.choreMemberBulkRepository = choreMemberBulkRepository;
    }

    public void saveAll(List<Chore> chores) {
        saveAll(chores, DEFAULT_CHUNK_SIZE);
    }

    public void saveAll(List<Chore> chores, int chunkSize) {
        ChoreUniqueMatcher matcher = new ChoreUniqueMatcher(chores);
        List<List<Chore>> subSets = Lists.partition(matcher.getChores(), chunkSize);

        for (List<Chore> subSet : subSets) {
            List<Chore> savedItems = insertChores(subSet);
            choreMemberBulkRepository.insertChoreMembers(matcher, savedItems);
        }
    }

    public List<Chore> insertChores(List<Chore> chores) {
        List<Long> generatedKeys = new ArrayList<>();

        String sql = "INSERT INTO chore (schedule_id, is_completed, match_key, start_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (Chore chore : chores) {
                ps.setLong(1, chore.getSchedule().getId());
                ps.setBoolean(2, chore.isCompleted());
                ps.setString(3, chore.getMatchKey());
                ps.setDate(4, Date.valueOf(chore.getStartDate()));
                ps.addBatch();
            }

            ps.executeBatch();

            ps.getGeneratedKeys();
            rs = ps.getGeneratedKeys();
            while (rs.next()) {
                long generatedKey = rs.getLong(1);
                generatedKeys.add(generatedKey);
            }

            return choreMatcherRepository.findAllByIds(generatedKeys);

        } catch (SQLException e) {
            throw Objects.requireNonNull(exTranslator.translate("insertChores", sql, e));
        } finally {
            close(con, ps, rs);
        }

    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
