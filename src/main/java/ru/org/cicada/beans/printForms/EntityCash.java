/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 *
 * @author m.p.kalinin
 */
public class EntityCash<I, V> {

    private final Map<I, V> cash = new HashMap<>();

    V getValue(I id, Supplier<V> factory) {
        return Optional
                .ofNullable(cash.get(id))
                .orElseGet(
                        () -> {
                            V newEntity = factory.get();
                            cash.put(id, newEntity);
                            return newEntity;
                        });
    }

}
