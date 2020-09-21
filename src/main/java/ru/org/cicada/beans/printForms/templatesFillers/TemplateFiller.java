/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms.templatesFillers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author Kalinin Maksim
 */
public interface TemplateFiller {

    public static void createDocument(String fileName, File document, Map<String, List<String>> values) throws IOException {
        InputStream input = new FileInputStream(fileName);
        FileOutputStream outputStream = new FileOutputStream(document.getAbsolutePath());
        fillDocument(input, outputStream, values);

    }

    public static void fillDocument(InputStream input,
            OutputStream output,
            Map<String, List<String>> keyValues) throws IOException {

        XWPFDocument document = new XWPFDocument(input);

        for (XWPFTable t : document.getTables()) {
            for (XWPFTableRow row : t.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        p.getRuns().forEach(r -> {
                            String text = r.getText(0);
                            if (text != null) {
                                keyValues
                                        .entrySet()
                                        .stream()
                                        .forEach((kv) -> replace(text, r, kv.getKey(), kv.getValue()));
                            }
                        });
                    }
                }
            }
        }

        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            for (XWPFRun r : paragraph.getRuns()) {
                String text = r.getText(0);
                if (text != null) {
                    keyValues
                            .entrySet()
                            .stream()
                            .forEach((kv) -> replace(text, r, kv.getKey(), kv.getValue().get(0)));
                }
            }
        }

        document.write(output);
    }

    public static void replace(String text, XWPFRun r, String key, Object value) {
        String newText = text.replace(key, value.toString());
        if (!text.equals(newText)) {
            r.setText(newText, 0);
        }
    }
}
