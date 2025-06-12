package controller;


import java.io.IOException;
import java.util.List;

import dao.ConnectionFactory;
import dao.OptionDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Option;

@WebServlet("/option")
public class OptionServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int questionId = Integer.parseInt(request.getParameter("questionId"));

        try (var conn = ConnectionFactory.getConnection()) {
            OptionDao dao = new OptionDao(conn);
            List<Option> options = dao.findByQuestionId(questionId);
            request.setAttribute("options", options);
            request.setAttribute("questionId", questionId);
            RequestDispatcher dispatcher = request.getRequestDispatcher("option-list.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Erro ao listar opções.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        String description = request.getParameter("description");

        Option option = new Option(questionId, description);

        try (var conn = ConnectionFactory.getConnection()) {
            OptionDao dao = new OptionDao(conn);
            dao.save(option, questionId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("success.jsp");
    }
}

