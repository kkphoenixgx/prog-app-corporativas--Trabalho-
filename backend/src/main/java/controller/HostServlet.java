package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import dao.HostDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Host;

@WebServlet("/host")
public class HostServlet extends HttpServlet {

    private HostDao hostDao;

    @Override
    public void init() throws ServletException {
        hostDao = new HostDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Host host = hostDao.findById(id);

            if (host != null) {
                out.println("<h2>Host encontrado:</h2>");
                out.println("ID: " + host.getId() + "<br>");
                out.println("Nome: " + host.getName() + "<br>");
                out.println("Senha: " + host.getPassword());
            } else {
                out.println("<h2>Host não encontrado.</h2>");
            }

        } else {
            List<Host> hosts = hostDao.findAll();
            out.println("<h2>Lista de Hosts:</h2>");
            for (Host h : hosts) {
                out.println("ID: " + h.getId() + " | Nome: " + h.getName() + "<br>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Host host = new Host(id, name, password);
        hostDao.insert(host);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>Host inserido com sucesso!</h2>");
    }
}