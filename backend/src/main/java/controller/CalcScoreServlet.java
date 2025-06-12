package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.ScoreCalculator;

@WebServlet("/calc-score")
public class CalcScoreServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] correctAnswersArr = request.getParameterValues("correctAnswers");
        String[] totalQuestionsArr = request.getParameterValues("totalQuestions");
        int correctAnswers = 0;
        int totalQuestions = 0;
        try {
            if (correctAnswersArr != null && correctAnswersArr.length > 0) {
                correctAnswers = Integer.parseInt(correctAnswersArr[0]);
            }
            if (totalQuestionsArr != null && totalQuestionsArr.length > 0) {
                totalQuestions = Integer.parseInt(totalQuestionsArr[0]);
            }
        } catch (NumberFormatException e) {
            response.sendError(400, "Parâmetros inválidos");
            return;
        }
        double score = ScoreCalculator.calculateScore(correctAnswers, totalQuestions);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(String.valueOf(score));
    }
}
