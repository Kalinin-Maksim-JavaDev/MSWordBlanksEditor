/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import ru.org.cicada.beans.DBSotr;
import ru.org.cicada.beans.UserAccount;

/**
 *
 * @author r.i.konoplev
 */
public class MyUtils {

    /**
     *
     */
    public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";
    private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

    // Сохранить Connection в attribute в request.
    // Данная информация хранения существует только во время запроса (request)
    // до тех пор, пока данные возвращаются приложению пользователя.
    /**
     *
     * @param request
     * @param conn
     */
    public static void storeConnection(ServletRequest request, Connection conn) {
        request.setAttribute(ATT_NAME_CONNECTION, conn);
    }

    // Получить объект Connection сохраненный в attribute в request.
    /**
     *
     * @param request
     * @return
     */
    public static Connection getStoredConnection(ServletRequest request) {
        Connection conn = (Connection) request.getAttribute(ATT_NAME_CONNECTION);
        return conn;
    }
    private static final long serialVersionUID = 1L;
    // Сохранить информацию пользователя, который вошел в систему (login) в Session.

    /**
     *
     * @param session
     * @param loginedUser
     * @param dbsotr
     */
    public static void storeLoginedUser(HttpSession session, UserAccount loginedUser, List<DBSotr> dbsotr) {

        // В JSP можно получить доступ через ${loginedUser}
        session.setAttribute("loginedUser", loginedUser);
        session.setAttribute("dbsotr", dbsotr);
    }
    // Сохранить информацию пользователя, который вошел в систему (login) в Session.

    /**
     *
     * @param session
     * @param loginedUser
     */
    public static void storeLoginedUser(HttpSession session, UserAccount loginedUser) {
        // В JSP можно получить доступ через ${loginedUser}
        session.setAttribute("loginedUser", loginedUser);
    }

    // Удалить сессию
    /**
     *
     * @param session
     * @param loginedUser
     */
    public static void deleteLoginedUser(HttpSession session, UserAccount loginedUser) {
        // В JSP можно получить доступ через ${loginedUser}
        session.setAttribute("loginedUser", null);
    }

    // Получить информацию пользователя, сохраненная в Session.
    /**
     *
     * @param session
     * @return
     */
    public static UserAccount getLoginedUser(HttpSession session) {
        UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
        return loginedUser;
    }

    // Сохранить информацию пользователя в Cookie.
    /**
     *
     * @param response
     * @param user
     */
    public static void storeUserCookie(HttpServletResponse response, UserAccount user) {
       
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
        // 1 день (Конвертированный в секунды)
        cookieUserName.setMaxAge(24 * 60 * 60);
        response.addCookie(cookieUserName);
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getUserNameInCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Удалить Cookie пользователя
    /**
     *
     * @param response
     */
    public static void deleteUserCookie(HttpServletResponse response) {
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);
        // 0 секунд. (Данный Cookie будет сразу недействителен)
        cookieUserName.setMaxAge(0);
        response.addCookie(cookieUserName);
    }
    private static final Logger LOG = getLogger(MyUtils.class.getName());

    private MyUtils() {
    }
    private static final Logger LOGGER = getLogger(MyUtils.class.getName());

    public static String md5Apache(String st) {
        String md5Hex = DigestUtils.md5Hex(st);
        return md5Hex;
    }

    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        huc.connect();
        return huc.getResponseCode();
    }
}
