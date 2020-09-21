/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ru.org.cicada.beans.UserAccount;
import ru.org.cicada.beans.printForms.Form;
import ru.org.cicada.beans.printForms.FormGroup;
import ru.org.cicada.beans.printForms.Forms;
import static ru.org.cicada.servlet.extensions.FiledsExtractor.exctractTableFileds;
import ru.org.cicada.servlet.extensions.filesLoading.Uploader;
import ru.org.cicada.beans.persist.UpdateManager;
import static ru.org.cicada.utils.MyUtils.getLoginedUser;
import static ru.org.cicada.utils.MyUtils.getStoredConnection;

/**
 *
 * @author Kalinin Maksim
 */
@WebServlet(urlPatterns = {"/printForms/save"})
@MultipartConfig(fileSizeThreshold = 1_024 * 1_024 * 1_024, maxFileSize = 1_024 * 1_024 * 1_024, maxRequestSize = 1_024 * 1_024 * 1_024)
public class Save extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Save.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("ID");
        String name = request.getParameter("NAME");
        String description = request.getParameter("DESCRIPTION");

        Form form = null;
        String type = request.getParameter("type");
        switch (type) {
            case "group":
                form = Forms.createGroup(name);
                form.setDescription(description);
                break;
            case "element":
                String query = request.getParameter("QUERY");
                //требуется карту: ключ - имя поля, значение - массив строк, преобразовать в лист имен-значений
                //Например, из карты
                //      Фамилия - [1:Петров,2:Иванов]
                //      Имя - [1:Петр,2:Иван]
                //  получим лист карт
                //      Фамилия - Петров, Имя - Петр
                //      Фамилия - Иванов, Имя - Иван
                List<Map<String, String>> fieldsList = exctractTableFileds(request.getParameterMap());

                String uploadPath = "C:\\\\" + "uploadedFiles" + File.separator + "printForms";
                List<String> fileNames = new Uploader(uploadPath).write(request.getParts());
                fileNames.add("");//default value

                form = Forms.create(name, description, fileNames.get(0))
                        .setQuery(query)
                        .setFields(fieldsList);
                break;

            default:
                throw new ServletException("Неизвестный параметр 'type'");
        }

        String pGroupID = request.getParameter("GROUPID");
        FormGroup formGroup = null;
        if (!pGroupID.isEmpty()) {
            long groupID = Long.parseLong(pGroupID);
            String groupName = request.getParameter("GROUPNAME");
            formGroup = Forms.getGroupBy(groupID, groupName);
        } else {
            formGroup = Forms.getRoot();
        }
        form.setGroup(formGroup);
        if (!id.isEmpty()) {
            form.setID(Long.parseLong(id));
        }

        StringBuilder errors = new StringBuilder();

        try {
            UpdateManager.persist(getStoredConnection(request), form::save, errors);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("form", form);
        response.sendRedirect(request.getContextPath() + "/printForms" + form.getURL());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
