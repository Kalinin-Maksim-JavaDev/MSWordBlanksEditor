/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.beans;

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author r.i.konoplev
 */
public class DBSotr implements Serializable {
    private static final Logger LOG = getLogger(DBSotr.class.getName());
    private static final Logger LOGGER = getLogger(DBSotr.class.getName());
       private String FIO;
       private String GOROD;
       private String DOLZNOST;
       private String DT;

    /**
     *
     * @return
     */
    public String getFIO() {
        return this.FIO;
    }

    /**
     *
     * @param FIO
     */
    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    /**
     *
     * @return
     */
    public String getGOROD() {
        return this.GOROD;
    }

    /**
     *
     * @param GOROD
     */
    public void setGOROD(String GOROD) {
        this.GOROD = GOROD;
    }

    /**
     *
     * @return
     */
    public String getDOLZNOST() {
        return this.DOLZNOST;
    }

    /**
     *
     * @param DOLZNOST
     */
    public void setDOLZNOST(String DOLZNOST) {
        this.DOLZNOST = DOLZNOST;
    }

    /**
     *
     * @return
     */
    public String getDT() {
        return this.DT;
    }

    /**
     *
     * @param DT
     */
    public void setDT(String DT) {
        this.DT = DT;
    }       
       
}
