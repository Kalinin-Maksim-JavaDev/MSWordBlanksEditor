/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms.JDBCext;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Kalinin Maksim
 */
public class Query {

    public static void setParameters(PreparedStatement stmt,
            String... params) throws SQLException {
        
        ParameterMetaData parameterMetaData = stmt.getParameterMetaData();

        int i = 0;
        for (String param : params) {
            if (i++ < parameterMetaData.getParameterCount()) {
                String parameterTypeName = parameterMetaData.getParameterTypeName(i);
                switch (parameterTypeName) {
                    case "text":
                        stmt.setString(i, param);
                        break;
                    case "numeric":                    
                    case "NUMBER":    
                        stmt.setLong(i, Long.parseLong(param));
                        break;
                    default:
                        throw new IllegalArgumentException("Query parameter type " + parameterTypeName + " undefined");
                }
            }
        }
    }
}
