package com.roundtable.roundtable.entity.chore;

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

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public ChoreBulkRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    public List<Long> batchUpdate(List<Chore> chores) {

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

            ps.executeUpdate();

            List<Long> generatedKeys = new ArrayList<>();
            rs = ps.getGeneratedKeys();
            while (rs.next()) {
                long generatedKey = rs.getLong(1); // 첫 번째 컬럼의 값을 가져옵니다.
                generatedKeys.add(generatedKey);
            }

            return generatedKeys;

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
