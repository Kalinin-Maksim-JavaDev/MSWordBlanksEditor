/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.org.cicada.servlet.printForms.gets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import static java.net.URLEncoder.encode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import ru.org.cicada.beans.printForms.Form;
import ru.org.cicada.beans.printForms.Forms;
import static ru.org.cicada.utils.MyUtils.getStoredConnection;

/**
 *
 * @author Kalinin Maksim
 */
@WebServlet(urlPatterns = {"/findByName"})
public class FindByName extends HttpServlet {

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

        String NAMEPART = request.getParameter("NAMEPART");
        Connection connection = getStoredConnection(request);
        try {
            List<Form> forms = Forms.getByNameContaining(connection, NAMEPART);

            List<Select2Item> data = forms.stream().map(f -> new Select2Item(f)).collect(Collectors.toList());

            String dataJson = new Gson().toJson(data);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            request.setAttribute("forms", forms);
            out.print(dataJson);
            out.flush();
        } catch (SQLException | RuntimeException ex) {
            throw new ServletException(ex);
        }

    }

    private class Select2Item {

        long id;
        String text;
        private final List<FormsFieldAttr> fieldAttr = new ArrayList<>();

        public Select2Item(Form form) {
            this.id = form.getID();
            this.text = form.getName();
            for (Form.Field f : form.getFields()) {
                fieldAttr.add(new FormsFieldAttr(f.getName(), f.getMark(), f.getDescription()));
            }
        }

        private class FormsFieldAttr {

            private final String name;
            private final String mark;
            private final String description;

            public FormsFieldAttr(String name, String mark, String description) {
                this.name = name;
                this.mark = mark;
                this.description = description;
            }

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
