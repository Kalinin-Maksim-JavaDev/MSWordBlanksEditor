/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms;

import java.io.IOException;
import java.sql.SQLException;
import static java.util.Comparator.comparing;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ru.org.cicada.beans.UserAccount;
import ru.org.cicada.beans.printForms.Form;
import ru.org.cicada.beans.printForms.FormGroup;
import ru.org.cicada.beans.printForms.Forms;
import static ru.org.cicada.utils.MyUtils.getLoginedUser;
import static ru.org.cicada.utils.MyUtils.getStoredConnection;

/**
 *
 * @author Kalinin Maksim
 */
@WebServlet(name = "PrintFormsEditor", urlPatterns = {"/printForms"})
public class Manager extends HttpServlet {

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

        // Проверить, вошел ли пользователь в систему (login) или нет.
        HttpSession session = request.getSession();
        UserAccount loginedUser = getLoginedUser(session);
        // Если еще не вошел в систему (login).
        if (loginedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
        
            Map<FormGroup, List<Form>> formsHierarchy = Forms.getFormsHierarchy(getStoredConnection(request));
            request.setAttribute("formsHierarchy", formsHierarchy);
            request.setAttribute("formGroupsList", formsHierarchy
                                                    .keySet()
                                                    .stream()
                                                    .sorted(comparing(FormGroup::getName))
                                                    .collect(Collectors.toList()));
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

        String path = "/WEB-INF/views/printForms/manager.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
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
