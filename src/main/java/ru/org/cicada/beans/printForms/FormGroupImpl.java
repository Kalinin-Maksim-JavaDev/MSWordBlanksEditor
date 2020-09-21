/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans.printForms;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author m.p.kalinin
 */
public class FormGroupImpl extends FormImpl implements FormGroup {

    FormGroupImpl(long id, String name) {
        super(id, name, true);
    }

    FormGroupImpl(String name) {
        super(IDSEQ.decrementAndGet(), name, true);
    }

    @Override
    public String toString() {
        return "Form group{" + "id=" + id + ", name=" + name + '}';
    }
}
