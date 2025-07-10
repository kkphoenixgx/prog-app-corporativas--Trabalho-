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
    System.out.println("[RandomQuizServlet] Iniciando processamento");
    
    String listSubjects = request.getParameter("listSubjects");
    String subject = request.getParameter("subject");
    
    System.out.println("[RandomQuizServlet] Parâmetros - listSubjects: " + listSubjects + ", subject: " + subject);
    
    try (var conn = ConnectionFactory.getConnection()) {
      QuizDao dao = new QuizDao(conn);

      if ("1".equals(listSubjects)) {
        System.out.println("[RandomQuizServlet] Listando assuntos");
        List<String> subjects = dao.findDistinctSubjects();
        response.setContentType("text/plain; charset=UTF-8");
        for (String s : subjects) {
          response.getWriter().println(s);
        }
        return;
      }

      List<Quiz> quizzes;
      if (subject != null && !subject.isBlank()) {
        System.out.println("[RandomQuizServlet] Filtrando por assunto: " + subject);
        quizzes = dao.findAll().stream()
          .filter(q -> subject.equals(q.getSubject()))
          .collect(Collectors.toList());
      } else {
        System.out.println("[RandomQuizServlet] Buscando todos os quizzes");
        quizzes = dao.findAll();
      }

      System.out.println("[RandomQuizServlet] Quizzes encontrados: " + quizzes.size());

      if (!quizzes.isEmpty()) {
        Quiz randomQuiz = quizzes.get((int)(Math.random() * quizzes.size()));
        System.out.println("[RandomQuizServlet] Quiz selecionado: " + randomQuiz.getSubject() + " (ID: " + randomQuiz.getId() + ")");
        request.setAttribute("quiz", randomQuiz);
        RequestDispatcher dispatcher = request.getRequestDispatcher("random-quiz.jsp");
        System.out.println("[RandomQuizServlet] Encaminhando para random-quiz.jsp");
        dispatcher.forward(request, response);
      } else {
        System.out.println("[RandomQuizServlet] Nenhum quiz encontrado");
        response.sendError(404, "Nenhum quiz encontrado.");
      }

    } catch (Exception e) {
      System.err.println("[RandomQuizServlet] Erro: " + e.getMessage());
      e.printStackTrace();
      response.sendError(500, "Erro ao buscar quiz aleatório.");
    }
  }
}
