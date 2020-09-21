/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms;

import java.io.File;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import ru.org.cicada.beans.persist.Persistable;
import ru.org.cicada.beans.printForms.JDBCext.Query;
import static ru.org.cicada.servlet.printForms.util.StreamAPIExt.Maps.transformInnerCollection;

/**
 *
 * @author Kalinin Maksim
 */
public class FormImpl implements Form {

    protected long id = Form.IDSEQ.decrementAndGet();
    private boolean isGroup;
    protected String name;
    private String description;
    private String filePath;
    private List<Field> fields = new ArrayList<>();
    private String query;
    private FormGroup group;

    FormImpl(String name, boolean isGroup) {
        this.name = name;
        this.isGroup = isGroup;
    }

    FormImpl(long id, String name, boolean isGroup) {
        this.id = id;
        this.name = name;
        this.isGroup = isGroup;
    }

    FormImpl(String name, String description, String path) {
        this.name = name;
        this.description = description;
        this.filePath = path;
    }

    @Override
    public String toString() {
        return "Form{" + "id=" + id + ", name=" + name + '}';
    }

    @Override
    public Form setID(long id) {
        this.id = id;
        return this;
    }

    public boolean isGroup() {
        return isGroup;
    }

    @Override
    public Form setGroup(FormGroup group) {
        this.group = group;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPath(String path) {
        this.filePath = path;
    }

    @Override
    public Form setFields(List<Map<String, String>> fieldsList) {
        fields = fieldsList.stream()
                .map(e -> new FieldImpl(this, e))
                .collect(Collectors.toList());
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void addField(Map<String, String> fieldParams) {
        fields.add(new FieldImpl(this, fieldParams));
    }

    @Override
    public Form setQuery(String query) {
        this.query = query;

        return this;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public FormGroup getGroup() {
        return Optional.ofNullable(group).orElseGet(() -> Forms.getRoot());
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public List<Field> getFields() {

        return fields;
    }

    @Override
    public int setFields(PreparedStatement stmt) throws SQLException {

        int i = 0;

        stmt.setLong(++i, getGroup().getID());
        stmt.setString(++i, name);
        stmt.setString(++i, description);
        if (!isGroup()) {
            stmt.setString(++i, filePath);
            int part = 1;
            stmt.setString(++i, ss(0, part * 2000));
            stmt.setString(++i, ss(part * 2000, (++part) * 2000));
        } else {
            stmt.setLong(++i, 1);//ISGROUP
        }
        return i;
    }

    private String ss(int beg, int end) {
        return !(beg < query.length()) ? "" : String.valueOf(query.substring(beg, end < query.length() ? end : query.length()));
    }

    @Override
    public void save(Connection connection, List<SQLException> excList) {
        try {
            setID(persist(connection, new Persistable.Identificator("rr1")));

            persistRecords(connection,
                    getFields(),
                    getManager().getDeleteQueryFor(Form.Field.manager.getBDTableName()),
                    Form.Field.manager.getInsertQuery());

        } catch (SQLException ex) {
            excList.add(ex);
        }
    }

    @Override
    public Optional<File> fillBlank(BiFunction<String, Map<String, List<String>>, File> filler,
            Connection connection,
            String... params) throws SQLException {

        class Value {

            String name;
            String value;

            public Value(String name, String value) {
                this.name = name;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

        }
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(getQuery());

            Query.setParameters(stmt, params);
            
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            List<Value> rowsValues = new ArrayList<>(columnCount * rs.getFetchSize());
            while (rs.next()) {

                for (int c = 1; c <= columnCount; c++) {
                    String columnName = md.getColumnName(c);
                    Optional<Field> field = getFieldByName(columnName);
                    if (field.isPresent()) {
                        rowsValues.add(new Value(field.get().getMark(), rs.getString(c)));
                    }
                }
            }

            //rowsValues:
            //[{name:A},{email::x}]
            //[{name:B},{email::y}]
            //[{name:C},{email::z}]
            Map<String, List<Value>> valuesMap = rowsValues.stream()
                    .collect(Collectors.groupingBy(Value::getName));

            Map<String, List<String>> result = transformInnerCollection(valuesMap, Value::getValue);

            return Optional.ofNullable(filler.apply(getFilePath(), result));
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FormImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
