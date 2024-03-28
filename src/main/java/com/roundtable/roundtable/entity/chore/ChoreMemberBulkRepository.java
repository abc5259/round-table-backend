package com.roundtable.roundtable.entity.chore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

@Repository
public class ChoreMemberBulkRepository {

    private final DataSource dataSource;

    private final SQLExceptionTranslator exTranslator;

    public ChoreMemberBulkRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    public void insertChores(ChoreUniqueMatcher matcher, List<Chore> idChores) {
        String sql = "INSERT INTO chore_member (chore_id, member_id, created_at, updated_at) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (Chore idChore : idChores) {
                for (ChoreMember choreMember : matcher.getChoreMembers(idChore)) {
                    ps.setLong(1, choreMember.getChore().getId());
                    ps.setLong(2, choreMember.getMember().getId());
                }
            }

            ps.executeBatch();

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
