/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms.actions;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.org.cicada.beans.printForms.Form;
import ru.org.cicada.beans.printForms.Forms;
import ru.org.cicada.beans.printForms.templatesFillers.WordDocFiller;
import ru.org.cicada.servlet.extensions.filesLoading.Downloader;
import static ru.org.cicada.utils.MyUtils.getStoredConnection;

/**
 *
 * @author Kalinin Maksim
 */
@WebServlet(name = "Print", urlPatterns = {"/printForms/print"})
public class Print extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Print.class.getName());

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
        
        long id = Long.parseLong(request.getParameter("id"));
        
        String[] PARAM = Optional.ofNullable(request.getParameterValues("PARAM")).orElse(new String[0]);
        Connection connection = getStoredConnection(request);
        File file = null;
        try {
            Form form = Forms.getByID(connection, id).orElseThrow(NullPointerException::new);
            file = form.fillBlank(WordDocFiller::fill, connection, PARAM).orElseThrow(NullPointerException::new);;
        } catch (SQLException | RuntimeException ex) {
            throw new ServletException(ex);
        }

        OutputStream ouputStream = response.getOutputStream();

        StringBuilder errors = new StringBuilder();
        try {
            Downloader.send(file,
                            ouputStream, 
                            response,
                            getServletContext(), errors);
           
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
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
