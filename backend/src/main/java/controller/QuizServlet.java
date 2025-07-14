package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

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

@WebServlet({"/quiz", "/quiz-json"})
public class QuizServlet extends HttpServlet {

  private Gson gson = new Gson();

  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    String uri = request.getRequestURI();
    
    if (uri.endsWith("quiz-json")) {
      String subject = request.getParameter("subject");
      String name = request.getParameter("name");
      response.setContentType("application/json");

      try (var conn = ConnectionFactory.getConnection()) {
        QuizDao dao = new QuizDao(conn);
        List<Quiz> quizzes;

        if (subject != null && !subject.isBlank()) {quizzes = dao.findBySubject(subject);}
        else if (name != null && !name.isBlank()) { quizzes = dao.findByName(name); } 
        else { quizzes = dao.findAll(); }

        response.getWriter().write(gson.toJson(quizzes));
      } 
      catch (Exception e) {
        e.printStackTrace();
        response.sendError(500, "Erro ao listar quizzes.");
      }
      
      return;
    }
    
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
    String uri = request.getRequestURI();
    if (uri.endsWith("quiz-json")) {

      response.setContentType("application/json");

      try (var conn = ConnectionFactory.getConnection()) {
      
        QuizDao dao = new QuizDao(conn);
        Quiz quiz = gson.fromJson(request.getReader(), Quiz.class);
        dao.save(quiz);
        response.getWriter().write("{\"success\":true}");
      
      } 
      catch (Exception e) {
        e.printStackTrace();
        response.setStatus(500);
        response.getWriter().write("{\"success\":false,\"error\":\"Erro ao criar quiz\"}");
      }

      return;
    }

    String delete = request.getParameter("delete");
    if (delete != null && delete.equals("1")) {
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

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String uri = request.getRequestURI();

    //? ----------- Endpoint Simples ----------- 
    
    if (uri.endsWith("quiz-json")) {
      response.setContentType("application/json");

      try (var conn = ConnectionFactory.getConnection()) {
        StringBuilder sb = new StringBuilder();
        String line;
        
        java.io.BufferedReader reader = request.getReader();
        
        while ((line = reader.readLine()) != null) sb.append(line);
        String json = sb.toString();
        
        System.out.println("[QuizServlet][PUT] JSON recebido: " + json);
        QuizDao dao = new QuizDao(conn);
        Quiz quiz = gson.fromJson(json, Quiz.class);
        
        System.out.println("[QuizServlet][PUT] Editando quiz ID: " + quiz.getId());
        dao.update(quiz);
        
        response.getWriter().write("{\"success\":true}");
      } 
      catch (Exception e) {
        e.printStackTrace();

        System.err.println("[QuizServlet][PUT] Erro ao editar quiz: " + e.getMessage());
        response.setStatus(500);
        response.getWriter().write("{\"success\":false,\"error\":\"Erro ao editar quiz\"}");
      }
      return;
    }

    response.sendError(405, "Método não permitido");
  }
  
}