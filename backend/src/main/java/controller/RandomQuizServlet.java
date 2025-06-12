package controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import dao.ConnectionFactory;
import dao.QuizDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Quiz;

@WebServlet("/random-quiz")
public class RandomQuizServlet extends HttpServlet {
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    String listSubjects = request.getParameter("listSubjects");
    String subject = request.getParameter("subject");
    
    try (var conn = ConnectionFactory.getConnection()) {
      QuizDao dao = new QuizDao(conn);

      if ("1".equals(listSubjects)) {
        List<String> subjects = dao.findDistinctSubjects();
        response.setContentType("text/plain; charset=UTF-8");
        for (String s : subjects) {
          response.getWriter().println(s);
        }
        return;
      }

      List<Quiz> quizzes;
      if (subject != null && !subject.isBlank()) {
        quizzes = dao.findAll().stream()
          .filter(q -> subject.equals(q.getSubject()))
          .collect(Collectors.toList());
      } else {
        quizzes = dao.findAll();
      }

      if (!quizzes.isEmpty()) {
        Quiz randomQuiz = quizzes.get((int)(Math.random() * quizzes.size()));
        request.setAttribute("quiz", randomQuiz);
        RequestDispatcher dispatcher = request.getRequestDispatcher("random-quiz.jsp");
        dispatcher.forward(request, response);
      } else {
        response.sendError(404, "Nenhum quiz encontrado.");
      }

    } catch (Exception e) {
      e.printStackTrace();
      response.sendError(500, "Erro ao buscar quiz aleatório.");
    }
  }
}
