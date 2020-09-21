/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.org.cicada.beans.persist.Persistable;
import static ru.org.cicada.beans.persist.Persistable.ResultGetters.*;
import ru.org.cicada.beans.persist.Persistable.ResultReader;

/**
 *
 * @author Kalinin Maksim
 */
public class Forms {

    private static final Logger LOG = Logger.getLogger(Forms.class.getName());

    private static final EntityCash<Long, FormGroup> groupsCash = new EntityCash<>();
    private static final Persistable.Manager ELEMENTS_MANAGER = Form.getElementsManager();

    private static final String QUERY = "select " + ELEMENTS_MANAGER.getFieldsForQuery("form") + "\n"
            + "        ,form.ISGROUP\n"
            + "        ,NVL(formGroup.RR1, '" + FormGroup.ROOTID + "') as groupID\n"
            + "        ,NVL(formGroup.NAME, '" + FormGroup.ROOTNAME + "') as groupName\n"
            + "from " + ELEMENTS_MANAGER.getBDTableName() + " form\n"
            + "     left join " + ELEMENTS_MANAGER.getBDTableName() + "  formGroup\n"
            + "     on form.Parentid = formGroup." + ELEMENTS_MANAGER.getIDName();

    public static FormGroup getGroupBy(long id, String name) {
        return groupsCash.getValue(id, () -> new FormGroupImpl(id, name));
    }

    public static FormGroup createGroup(String name) {
        return new FormGroupImpl(name);
    }

    public static FormGroup getRoot() {
        return getGroupBy(FormGroup.ROOTID, FormGroup.ROOTNAME);
    }

    public static Form create(String name, String description, String path) {

        return new FormImpl(name, description, path);
    }

    public static List<Form> testList(String... names) {

        return Stream.of(names).map(n -> create(n, "not in BD", "x:/x.docx")).collect(Collectors.toList());
    }

    public static Form testParent(String name) {
        return create(name, "not in BD", "");
    }

    public static List<Form> getGroups(Connection connection) throws SQLException {
        String groupsQuery = QUERY + "\n"
                + "where form.ISGROUP = 1";

        List<Form> forms = getForms(connection, groupsQuery, stmt -> {
        });

        return forms;
    }

    public static Map<FormGroup, List<Form>> getFormsHierarchy(Connection connection) throws SQLException {

        List<Form> forms = getForms(connection, QUERY, stmt -> {
        });

        Map<FormGroup, List<Form>> result = forms.stream()
                .collect(Collectors.groupingBy(Form::getGroup));

        return result;
    }

    public static Optional<Form> getByID(Connection connection, long id) throws SQLException {
        String formsQuery = QUERY + "\n"
                + "where form." + ELEMENTS_MANAGER.getIDName() + " = ?";

        List<Form> forms = getForms(connection, formsQuery, stmt -> {
            try {
                stmt.setLong(1, id);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        });
        forms.add(null);//default

        return Optional.ofNullable(forms.get(0));
    }

    public static List<Form> getByNameContaining(Connection connection, String name) throws SQLException {
        String formsQuery = QUERY + "\n"
                + "where UPPER(form.name) like '%${P1}%'";

        List<Form> forms = getForms(connection, formsQuery.replace("${P1}", name.toUpperCase()), stmt -> {
        });

        return forms;
    }

    private static List<Form> getForms(Connection connection,
            String formsQuery,
            Consumer<PreparedStatement> setParams) throws SQLException {

        List<Form> forms = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement(formsQuery);
        setParams.accept(stmt);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            long id = getLong(rs, ELEMENTS_MANAGER.getIDName());
            String name = getString(rs, "NAME");
            String description = getString(rs, "DESCRIPTION");
            boolean isGroup = getLong(rs, "ISGROUP") == 1;

            Form form = null;
            if (isGroup) {

                FormGroup group = Forms.getGroupBy(id, name);
                group.setDescription(description);
                forms.add(group);
            } else {

                form = Forms.create(name, description,
                        getString(rs, "TEMPLATEPATH"))
                        .setID(id)
                        .setQuery(getString(rs, "QUERY") + getString(rs, "QUERYEXT"));

                String fieldQuery = "select * from " + Form.Field.manager.getBDTableName() + " where " + Form.Field.manager.getIDName() + "=?";
                PreparedStatement fieldsStmt = connection.prepareStatement(fieldQuery);
                fieldsStmt.setLong(1, form.getID());

                ResultSet fieldsStmtResult = fieldsStmt.executeQuery();

                Persistable.ResultReader resultReader = new Persistable.ResultReader(fieldsStmtResult);
                while (fieldsStmtResult.next()) {

                    ResultReader.Columns<String> columns = Form.Field.manager.getPersistedFields().stream()
                            .collect(() -> resultReader.newStringColumns(),
                                    ResultReader.Columns::put,
                                    ResultReader.Columns::putAll);

                    form.addField(columns);
                }

                resultReader.throwSupressedExceptions();
                fieldsStmt.close();

                FormGroup formGroup = Forms.getGroupBy(getLong(rs, "groupID"), getString(rs, "groupName"));
                form.setGroup(formGroup);

                forms.add(form);
            }

        }
        stmt.close();

        return forms;
    }

}
