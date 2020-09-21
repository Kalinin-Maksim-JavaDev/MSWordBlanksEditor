/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kalinin Maksim
 */
public class Downloader {

    private static final Logger LOGGER = Logger.getLogger(Downloader.class.getName());

    public static void write(File file,
            OutputStream ouputStream,
            HttpServletResponse response,
            ServletContext servletContext,
            StringBuilder errors) throws Exception {

        FileInputStream inStream = null;

        $initSource:
        {
            try {
                inStream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                errors.append("Obtain file error: ".concat(ex.getMessage()));
                break $initSource;
            }
            
            $prepareResponse:
            {
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                try {
                    while ((bytesRead = inStream.read(buffer)) > 0) {
                        ouputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    errors.append("Can't read or write file: ".concat(ex.getMessage()));
                    break $prepareResponse;
                }

                // gets MIME type of the file
                String mimeType = servletContext.getMimeType(file.getAbsolutePath());
                if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            }
            try {
                inStream.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                errors.append("Close file error: ".concat(ex.getMessage()));
            }

        }
        if (errors.length() != 0) {
            errors.insert(0, file.getAbsolutePath().concat("\n").concat("errrr"));
        }
    }

}
