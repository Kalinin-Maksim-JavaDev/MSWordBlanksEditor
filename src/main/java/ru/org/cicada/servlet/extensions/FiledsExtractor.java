/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author m.p.kalinin
 */
public class FiledsExtractor {
    public static List<Map<String, String>> exctractTableFileds(Map<String, String[]> map) {
        return map.entrySet().stream()
                .filter(kv -> kv.getKey()
                .contains("FIELD."))
                .collect(ArrayList::new,
                        (list, kv) -> {
                            String key = kv.getKey().replace("FIELD.", "");//имя поля
                            String[] values = kv.getValue();//массив значений
                            for (int i = 0; i < values.length; i++) {
                                String value = values[i];

                                //Добавим недостающие строки в лист
                                while (list.size() - 1 < i) {
                                    list.add(new HashMap<>());
                                }

                                //вставка имя-значение в строку листа с индекcом из массива значений
                                list.get(i).put(key, value);
                            }
                        },
                        List::addAll);
    }
}
