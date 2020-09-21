/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import ru.org.cicada.beans.persist.Persistable;
import ru.org.cicada.beans.persist.Persistable.Manager;

/**
 *
 * @author Kalinin Maksim
 */
public interface Form extends Persistable {

    public static final AtomicLong IDSEQ = new AtomicLong(FormGroup.ROOTID);
    
    static final Manager GROUPS_MANAGER = new GroupsManager();

    static final Manager ELEMENTS_MANAGER = new ElementsManager();

    static Manager getElementsManager() {
        return ELEMENTS_MANAGER;
    }

    @Override
    public default Manager getManager() {
        if (isGroup()) {
            return GROUPS_MANAGER;
        } else {
            return ELEMENTS_MANAGER;
        }
    }

    public default String getURL(long id) {
        return "/form?id=".concat(String.valueOf(id));
    }

    @Override
    default String[] getGeneratedFileds() {
        return new String[]{"RR1"};
    }

    public default String getURL() {
        return getURL(getID());
    }

    @Override
    public default boolean isReferenceType() {
        return true;
    }

    public default Optional<Field> getFieldByName(String name) {
        return getFields()
                .stream()
                .filter(f -> f.getName().equals(name))
                .findAny();
    }

    boolean isGroup();

    Form setID(long id);

    void setDescription(String description);

    Form setGroup(FormGroup group);

    Form setQuery(String query);

    void save(Connection connection, List<SQLException> excList);

    Form setFields(List<Map<String, String>> fieldsList);

    public void setFilePath(String get);

    void addField(Map<String, String> fieldParams);

    long getID();

    FormGroup getGroup();

    String getName();

    String getDescription();

    String getFilePath();

    String getQuery();

    List<Field> getFields();

    public Optional<File> fillBlank(BiFunction<String, Map<String, List<String>>, File> filler,
            Connection connection,
            String... params) throws SQLException;

    interface Field extends Persistable {

        static final Manager manager = new FormFieldsManager();

        @Override
        public default Manager getManager() {
            return manager;
        }

        @Override
        public default String[] getGeneratedFileds() {
            return new String[0];
        }

        @Override
        public default boolean isReferenceType() {
            return false;
        }

        String getName();

        String getMark();

        String getDescription();

    }
}

abstract class FormsManager implements Manager {

    private final String TABLENAME = "AB_CRM_PRINTFORMS";
    private final String IDNAME = "RR1";

    @Override
    public String getIDName() {
        return IDNAME;
    }

    @Override
    public String getBDTableName() {
        return TABLENAME;
    }
}

class ElementsManager extends FormsManager {

    private final String[] TABLE_FIELDS = {"PARENTID", "NAME", "DESCRIPTION", "TEMPLATEPATH", "QUERY", "QUERYEXT"};

    @Override
    public List<String> getPersistedFields() {
        return Arrays.asList(TABLE_FIELDS);
    }
}

class GroupsManager extends FormsManager {

    private final String[] TABLE_FIELDS = {"PARENTID", "NAME", "DESCRIPTION", "ISGROUP"};

    @Override
    public List<String> getPersistedFields() {
        return Arrays.asList(TABLE_FIELDS);
    }
}

class FormFieldsManager implements Manager {

    public FormFieldsManager() {
    }
    private final String IDNAME = Form.getElementsManager().getIDName();
    private final String TABLENAME = "AB_CRM_PRINTFORM_FIELDS";
    private final String[] TABLE_FIELDS = {"NAME", "MARK", "DESCRIPTION", IDNAME};

    @Override
    public String getBDTableName() {
        return TABLENAME;
    }

    @Override
    public String getIDName() {
        return IDNAME;
    }

    public List<String> getPersistedFields() {
        return Arrays.asList(TABLE_FIELDS);
    }
}
