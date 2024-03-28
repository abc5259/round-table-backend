package com.roundtable.roundtable.entity.chore;

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

@Repository
public class ChoreBulkRepository {

    private static final Integer DEFAULT_CHUNK_SIZE = 1_000;

    private final DataSource dataSource;

    private final SQLExceptionTranslator exTranslator;

    private final ChoreRepository choreRepository;

    public ChoreBulkRepository(DataSource dataSource, ChoreRepository choreRepository) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
        this.choreRepository = choreRepository;
    }

    public void saveAll(List<Chore> chores) {
        saveAll(chores, DEFAULT_CHUNK_SIZE);
    }

    public void saveAll(List<Chore> chores, int chunkSize) {
        ChoreUniqueMatcher choreUniqueMatcher = new ChoreUniqueMatcher(chores);
        List<List<Chore>> subSets = Lists.partition(choreUniqueMatcher.getChores(), chunkSize);

        for (List<Chore> subSet : subSets) {
            List<Chore> savedItems = insertChores(subSet);

        }
    }

    public List<Chore> insertChores(List<Chore> chores) {
        List<Long> generatedKeys = new ArrayList<>();

        String sql = "INSERT INTO chore (schedule_id, is_completed, start_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (Chore chore : chores) {
                ps.setLong(1, chore.getSchedule().getId());
                ps.setBoolean(2, chore.isCompleted());
                ps.setDate(3, Date.valueOf(chore.getStartDate()));
            }

            ps.executeBatch();
            rs = ps.getGeneratedKeys();
            while (rs.next()) {
                long generatedKey = rs.getLong(1);
                generatedKeys.add(generatedKey);
            }

            return choreRepository.findAllById(generatedKeys);

        } catch (SQLException e) {
            throw Objects.requireNonNull(exTranslator.translate("batchUpdate", sql, e));
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
