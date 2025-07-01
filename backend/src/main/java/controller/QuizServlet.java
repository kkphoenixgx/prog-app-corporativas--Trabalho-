package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dao.ConnectionFactory;
import dao.QuizDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Option;
import model.Question;
import model.Quiz;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    
    try (var conn = ConnectionFactory.getConnection()) {
    
      QuizDao dao = new QuizDao(conn);
      List<Quiz> quizzes = dao.findAll();
      request.setAttribute("quizzes", quizzes);
      RequestDispatcher dispatcher = request.getRequestDispatcher("quiz-list.jsp");
      dispatcher.forward(request, response);
    
    } catch (Exception e) {
      e.printStackTrace();
      response.sendError(500, "Erro ao listar quizzes.");
    }

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String delete = request.getParameter("delete");
    if (delete != null && delete.equals("1")) {
      // DELETE Quiz
      int id = Integer.parseInt(request.getParameter("id"));
      try (var conn = ConnectionFactory.getConnection()) {
        QuizDao dao = new QuizDao(conn);
        dao.deleteById(id);
      } catch (Exception e) {
        e.printStackTrace();
      }
      response.sendRedirect("quiz");
      return;
    }
    String subject = request.getParameter("subject");
    String[] questionDescriptions = request.getParameterValues("question");
    String[][] optionsMatrix = request.getParameterValues("options[]") != null ?
            Arrays.stream(request.getParameterValues("options[]")).map(s -> s.split("\\|")).toArray(String[][]::new)
            : new String[0][];
    String[] correctOptions = request.getParameterValues("correctOption");

    List<Question> questions = new ArrayList<>();

    for (int i = 0; i < questionDescriptions.length; i++) {
        List<Option> options = new ArrayList<>();
        for (String desc : optionsMatrix[i]) {
            options.add(new Option(desc));
        }
        int correctOption = Integer.parseInt(correctOptions[i]);
        questions.add(new Question(correctOption, questionDescriptions[i], options));
    }

    Quiz quiz = new Quiz(subject, new Date(), questions);

    try (var conn = ConnectionFactory.getConnection()) {
        QuizDao dao = new QuizDao(conn);
        dao.save(quiz);
    } catch (Exception e) {
        e.printStackTrace();
    }

    response.sendRedirect("success.jsp");
  }
  


}