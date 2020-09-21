/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author m.p.kalinin
 */
public class StreamAPIExt {

    public static class Maps {

        public static <K, E, T>
                Map<K, List<T>> transformInnerCollection(Map<K, List<E>> map, Function<E, T> transform) {
            return map.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            l -> l.getValue()
                                    .stream()
                                    .map(transform)
                                    .collect(Collectors.toList())));
        }
    }
}
