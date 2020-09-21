package ru.org.cicada.beans;

import java.sql.Timestamp;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author r.i.konoplev
 */

public class UserAccount {
    private static final Logger LOG = getLogger(UserAccount.class.getName());
    private static final Logger LOGGER = getLogger(UserAccount.class.getName());

    private String userName;
    private String password;
    private String SV;
    private String fio;
    private String date_vxod;
    private Timestamp DATE_BIRTH;
    private String SEX;
    private String DOPUSK;
    private String ADMIN;
    private int WORK_FLAG;
    private String gorod;
    private String DOLZNOST;
    private String DEPART;
    private String MOB_PHONE;
    private String WORK_PHONE;
    private String EMAIL;
    private String URL_PHOTO;

    /**
     *
     */
    public UserAccount() {
    }

    /**
     *
     * @return
     */
    public String getFio() {
        return this.fio;
    }

    /**
     *
     * @param fio
     */
    public void setFio(String fio) {
        this.fio = fio;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getSV() {
        return this.SV;
    }

    /**
     *
     * @param SV
     */
    public void setSV(String SV) {
        this.SV = SV;
    }

    /**
     *
     * @return
     */
    public String getDate_vxod() {
        return this.date_vxod;
    }

    /**
     *
     * @param date_vxod
     */
    public void setDate_vxod(String date_vxod) {
        this.date_vxod = date_vxod;
    }

    /**
     *
     * @return
     */
    public Timestamp getDATE_BIRTH() {
        return this.DATE_BIRTH;
    }

    /**
     *
     * @param DATE_BIRTH
     */
    public void setDATE_BIRTH(Timestamp DATE_BIRTH) {
        this.DATE_BIRTH = DATE_BIRTH;
    }

    /**
     *
     * @return
     */
    public String getSEX() {
        return this.SEX;
    }

    /**
     *
     * @param SEX
     */
    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    /**
     *
     * @return
     */
    public String getDOPUSK() {
        return this.DOPUSK;
    }

    /**
     *
     * @param DOPUSK
     */
    public void setDOPUSK(String DOPUSK) {
        this.DOPUSK = DOPUSK;
    }

    /**
     *
     * @return
     */
    public String getADMIN() {
        return this.ADMIN;
    }

    /**
     *
     * @param ADMIN
     */
    public void setADMIN(String ADMIN) {
        this.ADMIN = ADMIN;
    }

    /**
     *
     * @return
     */
    public int getWORK_FLAG() {
        return this.WORK_FLAG;
    }

    /**
     *
     * @param WORK_FLAG
     */
    public void setWORK_FLAG(int WORK_FLAG) {
        this.WORK_FLAG = WORK_FLAG;
    }

    /**
     *
     * @return
     */
    public String getGorod() {
        return this.gorod;
    }

    /**
     *
     * @param gorod
     */
    public void setGorod(String gorod) {
        this.gorod = gorod;
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
    public String getDEPART() {
        return this.DEPART;
    }

    /**
     *
     * @param DEPART
     */
    public void setDEPART(String DEPART) {
        this.DEPART = DEPART;
    }

    /**
     *
     * @return
     */
    public String getMOB_PHONE() {
        return this.MOB_PHONE;
    }

    /**
     *
     * @param MOB_PHONE
     */
    public void setMOB_PHONE(String MOB_PHONE) {
        this.MOB_PHONE = MOB_PHONE;
    }

    /**
     *
     * @return
     */
    public String getWORK_PHONE() {
        return this.WORK_PHONE;
    }

    /**
     *
     * @param WORK_PHONE
     */
    public void setWORK_PHONE(String WORK_PHONE) {
        this.WORK_PHONE = WORK_PHONE;
    }

    /**
     *
     * @return
     */
    public String getEMAIL() {
        return this.EMAIL;
    }

    /**
     *
     * @param EMAIL
     */
    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getURL_PHOTO() {
        return URL_PHOTO;
    }

    public void setURL_PHOTO(String URL_PHOTO) {
        this.URL_PHOTO = URL_PHOTO;
    }

}
