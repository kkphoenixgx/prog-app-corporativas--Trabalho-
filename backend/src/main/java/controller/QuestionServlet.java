package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.ConnectionFactory;
import dao.QuestionDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Option;
import model.Question;


@WebServlet("/question")
public class QuestionServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    int quizId = Integer.parseInt(request.getParameter("quizId"));

    try (var conn = ConnectionFactory.getConnection()) {
      QuestionDao dao = new QuestionDao(conn);
      List<Question> questions = dao.findByQuizId(quizId);
      request.setAttribute("questions", questions);
      request.setAttribute("quizId", quizId);
      RequestDispatcher dispatcher = request.getRequestDispatcher("question-list.jsp");
      dispatcher.forward(request, response);
      
    } catch (Exception e) {
      e.printStackTrace();
      response.sendError(500, "Erro ao listar perguntas.");
    }

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String delete = request.getParameter("delete");
    if (delete != null && delete.equals("1")) {
      // DELETE Question
      int id = Integer.parseInt(request.getParameter("id"));
      int quizId = Integer.parseInt(request.getParameter("quizId"));
      try (var conn = ConnectionFactory.getConnection()) {
        QuestionDao dao = new QuestionDao(conn);
        dao.deleteById(id);
      } catch (Exception e) {
        e.printStackTrace();
      }
      response.sendRedirect("question?quizId=" + quizId);
      return;
    }
      
    int quizId = Integer.parseInt(request.getParameter("quizId"));
    String description = request.getParameter("description");
    int correctOption = Integer.parseInt(request.getParameter("correctOption"));

    String[] optionDescriptions = request.getParameterValues("options");

    List<Option> options = new ArrayList<>();

    for (String desc : optionDescriptions) {
      options.add(new Option(desc));
    }

    Question question = new Question(quizId, correctOption, description, options);

    try (var conn = ConnectionFactory.getConnection()) {
        QuestionDao dao = new QuestionDao(conn);
        dao.save(question, quizId);
    } catch (Exception e) {
        e.printStackTrace();
    }

    response.sendRedirect("success.jsp");
  }



}

