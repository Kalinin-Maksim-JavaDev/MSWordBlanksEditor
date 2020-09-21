/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kalinin Maksim
 */
public class FieldImpl implements Form.Field {

    private final Form form;
    private final String name;
    private final String mark;
    private final String description;

    FieldImpl(Form form, String name, String mark, String description) {
        this.form = form;
        this.name = name;
        this.mark = mark;
        this.description = description;
    }

    FieldImpl(Form form, Map<String, String> src) {
        this(form, src.get("NAME"), src.get("MARK"), src.get("DESCRIPTION"));
    }

    @Override
    public long getID() {
        return form.getID();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMark() {
        return mark;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int setFields(PreparedStatement stmt) throws SQLException {

        int i = 0;
        stmt.setString(++i, name);
        stmt.setString(++i, mark);
        stmt.setString(++i, description);
        stmt.setLong(++i, form.getID());
        return i;

    }
}
