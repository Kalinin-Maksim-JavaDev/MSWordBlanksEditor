/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms.templatesFillers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m.p.kalinin
 */
public class WordDocFiller implements TemplateFiller {

    private static final Logger LOG = Logger.getLogger(WordDocFiller.class.getName());
    private static final String TEMPDIR = "C:\\temp\\";

    public static File fill(String fileName, Map<String, List<String>> values) {

        File document = new File(TEMPDIR + File.separator + new File(fileName).getName());

        try {
            TemplateFiller.createDocument(fileName, document, values);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return document;
    }

}
