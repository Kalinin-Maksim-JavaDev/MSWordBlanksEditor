/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
import ru.org.cicada.beans.printForms.Forms;
import static ru.org.cicada.utils.MyUtils.getLoginedUser;
import static ru.org.cicada.utils.MyUtils.getStoredConnection;

/**
 *
 * @author Kalinin Maksim
 */
@WebServlet(name = "Form", urlPatterns = {"/printForms/form"})
public class FormPage extends HttpServlet {

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

            String path = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Optional<Form> formOp = Forms.getByID(getStoredConnection(request), id);
            Form form = formOp.orElseThrow(NullPointerException::new);
            request.setAttribute("form", form);
            
            List<Form> groups = Forms.getGroups(getStoredConnection(request));
            request.setAttribute("groups", groups);

            if (!form.isGroup()) {
                path = "/WEB-INF/views/printForms/form.jsp";
            } else {
                path = "/WEB-INF/views/printForms/group.jsp";
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

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
