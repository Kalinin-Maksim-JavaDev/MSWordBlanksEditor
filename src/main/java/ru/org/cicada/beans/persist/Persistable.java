/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.org.cicada.beans.printForms.FormGroup;

/**
 *
 * @author Kalinin Maksim
 */
public interface Persistable {

    static final Logger LOGGER = Logger.getLogger(Persistable.class.getName());

    public long getID();

    public String[] getGeneratedFileds();

    public int setFields(PreparedStatement stmt) throws SQLException;

    public default boolean isNew() {
        return getID() < 0;
    }

    public boolean isReferenceType();

    public Manager getManager();

    interface Manager {

        public String getBDTableName();

        public String getIDName();

        public List<String> getPersistedFields();

        public default String getInsertQuery() {
            class Values {

                private String place() {
                    return "(" + Stream.generate(() -> "?")
                            .limit(getPersistedFields().size())
                            .collect(Collectors.joining(",")) + ")";
                }

                private String names() {
                    return "(" + String.join(", ", getPersistedFields()) + ")";
                }

            }
            Values values = new Values();

            return "insert into " + getBDTableName() + values.names() + "\n"
                    + " values " + values.place();
        }

        public default String getDeleteQueryFor(String tableName) {
            return "delete from  " + tableName + "\n"
                    + "where " + getIDName() + " = ?";
        }

        public default String getUpdateQuery() {
            class Values {

                private String set() {
                    return getPersistedFields()
                            .stream()
                            .map(s -> s + " = ?")
                            .collect(Collectors.joining(",\n"));
                }

            }
            Values values = new Values();
            return "update  " + getBDTableName() + " set " + values.set() + "\n"
                    + "where " + getIDName() + " = ?";
        }

        public default String getFieldsForQuery(String alias) {
            String alias_ = alias + ".";
            return alias_ + getIDName() + ",\n"
                    + getPersistedFields().stream().collect(Collectors.joining(",\n" + alias_, alias_, ""));
        }
    }

    class ResultGetters {

        public static String getString(ResultSet rs, String colName) {
            try {
                return Optional.ofNullable(rs.getString(colName)).orElse("");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }

        public static long getLong(ResultSet rs, String colName) {
            try {
                return Optional.ofNullable(rs.getLong(colName)).orElse(0L);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public default void persist(Connection connection) throws SQLException {
        persist(connection, new Identificator());
    }

    public default long persist(Connection connection, Identificator identy)
            throws SQLException {

        long id = getID();

        PreparedStatement stmt = null;

        try {
            if (id < FormGroup.ROOTID) {
                if (identy.getIds().length != 0) {
                    boolean isOracleBD = true;
                    if (isOracleBD) {
                        stmt = connection.prepareStatement(getManager().getInsertQuery(), Statement.RETURN_GENERATED_KEYS);
                        setFields(stmt);
                        stmt.executeUpdate();

                        String rowid = identy.getROWID(stmt.getGeneratedKeys());

                        PreparedStatement stmtID = connection.prepareStatement("select rr1 from " + getManager().getBDTableName() + " where rowid=?");
                        stmtID.setString(1, rowid);
                        stmtID.execute();
                        ResultSet IdRes = stmtID.getResultSet();
                        if (IdRes.next()) {
                            id = IdRes.getLong(1);
                        }
                    } else {
                        
                        stmt = connection.prepareStatement(getManager().getInsertQuery(), identy.getIds());
                        setFields(stmt);
                        stmt.executeUpdate();
                        id = identy.getID(stmt.getGeneratedKeys());
                    }
                } else {
                    stmt = connection.prepareStatement(getManager().getInsertQuery());
                    setFields(stmt);
                    stmt.executeUpdate();
                }
            } else {
                if (isReferenceType()) {
                    stmt = connection.prepareStatement(getManager().getUpdateQuery());
                    int rowI = setFields(stmt);
                    stmt.setLong(++rowI, id);
                    stmt.executeUpdate();
                }
            }

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }

        return id;
    }

    public default <T extends Persistable> void persistRecords(Connection connection,
            List<T> records,
            String deleteQuery,
            String insertQuery) throws SQLException {

        long id = getID();

        if (id < 0) {
            throw new SQLException("ID for rows undefined ");
        }

        PreparedStatement stmtDel = null;
        PreparedStatement stmtInsert = null;

        try {

            stmtDel = connection.prepareStatement(deleteQuery);
            stmtDel.setLong(1, id);
            stmtDel.execute();

            stmtInsert = connection.prepareStatement(insertQuery);
            for (T record : records) {
                record.setFields(stmtInsert);
                stmtInsert.addBatch();
            }
            stmtInsert.executeBatch();

        } finally {
            if (stmtDel != null) {
                try {
                    stmtDel.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
            if (stmtInsert != null) {
                try {
                    stmtInsert.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    static class ResultReader {

        private ResultSet rs;
        private List<SQLException> exceptions = new ArrayList<>();

        public ResultReader(ResultSet rs) {
            this.rs = rs;
        }

        public void throwSupressedExceptions() throws SQLException {
            for (SQLException ex : exceptions) {
                throw ex;
            }
        }

        public Columns<String> newStringColumns() {
            return new Columns<>(ResultGetters::getString);
        }

        public class Columns<T> extends HashMap<String, T> {

            private final BiFunction<ResultSet, String, T> getter;

            public Columns(BiFunction<ResultSet, String, T> getter) {
                this.getter = getter;
            }

            public void put(String key) {
                super.put(key, getter.apply(rs, key));
            }
        }
    }

    public static class Identificator {

        List<String> ids = new ArrayList<>();

        public Identificator(String... names) {
            ids.addAll(Arrays.asList(names));
        }

        public String[] getIds() {
            return ids.toArray(new String[0]);
        }

        private String getROWID(ResultSet gk) throws SQLException {
            if (gk.next()) {
                return gk.getString(1);
            }
            throw new SQLException("Wrong id key");
        }
        
        private long getID(ResultSet gk) throws SQLException {
            if (gk.next()) {
                return gk.getLong(ids.get(0));
            }
            throw new SQLException("Wrong id key");
        }

    }
}
