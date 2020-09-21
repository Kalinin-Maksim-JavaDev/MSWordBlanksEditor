/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.persist;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m.p.kalinin
 */
public class UpdateManager {

    private static final Logger LOGGER = Logger.getLogger(UpdateManager.class.getName());

    public static void persist(Connection connection, BiConsumer<Connection, List<SQLException>> operation, StringBuilder errors) throws Exception {
        List<Exception> exceptions = new ArrayList();

        if (prepare(exceptions, connection, errors)) {

            if (!perform(exceptions, connection, operation, errors)) {
                rollback(exceptions, connection, errors);
            }

            finish(exceptions, connection, errors);
        }

        if (!exceptions.isEmpty()) {
            int i = 0;
            for (; i < exceptions.size() - 1; i++) {
                exceptions.get(i + 1).initCause(exceptions.get(i));
            }
            throw exceptions.get(i);
        }
    }

    private static boolean prepare(List<Exception> exception, Connection connection, StringBuilder errors) {
        try {
            // if (!connection.getAutoCommit()) {
            //     throw new IllegalStateException("AutoCommit is disabled, when it must be enabled");
            // }
            connection.setAutoCommit(false);
            return true;
        } catch (Exception ex) {
            exception.add(ex);
            LOGGER.log(Level.SEVERE, null, ex);
            errors.append("There was an error disabling autocommit");
            errors.append(ex.getMessage());
        }
        return false;
    }

    private static boolean perform(List<Exception> exception, Connection connection, BiConsumer<Connection, List<SQLException>> operation, StringBuilder errors) {
        try {
            List<SQLException> excList = new ArrayList<>();
            operation.accept(connection, excList);
            for(Exception ex:excList){
                throw ex;
            } 
            connection.commit();
            return true;
        } catch (Exception ex) {
            exception.add(ex);
            LOGGER.log(Level.SEVERE, null, ex);
            errors.append("There was an error persisting entry");
            errors.append(ex.getMessage());
        }
        return false;
    }

    private static void rollback(List<Exception> exception, Connection connection, StringBuilder errors) {
        try {
            connection.rollback();
            errors.append("Transaction rollback");
        } catch (Exception ex) {
            exception.add(ex);
            LOGGER.log(Level.SEVERE, null, ex);
            errors.append("There was an error making a rollback");
            errors.append(ex.getMessage());
        }
    }

    private static void finish(List<Exception> exception, Connection connection, StringBuilder errors) {
        try {
//            if (connection.getAutoCommit()) {
//                throw new IllegalStateException("AutoCommit is enabled, when it must be disabled");
//            }
            connection.setAutoCommit(false);
        } catch (Exception ex) {
            exception.add(ex);
            LOGGER.log(Level.SEVERE, null, ex);
            errors.append("There was an error disabling autocommit");
            errors.append(ex.getMessage());
        }
    }

}
