/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.extensions.filesLoading;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.Part;

/**
 *
 * @author m.p.kalinin
 */
public class Uploader {

    private String dir;

    public Uploader(String dir) {
        this.dir = dir;
    }

    public List<String> write(Collection<Part> parts) throws IOException {

        List<String> result = new ArrayList<>();

        File uploadDir = new File(dir);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        for (Part part : parts) {
            String fileName = getFileName(part);
            if (!fileName.isEmpty()) {
                String fullPath = dir + File.separator + fileName;
                part.write(fullPath);
                result.add(fullPath);
            }
        }

        return result;
    }

    private String getFileName(Part part) throws IOException {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return "";
        //throw new IOException("Could't get uploaded file name");
    }
}
