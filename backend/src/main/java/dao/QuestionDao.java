package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import model.Option;
import model.Question;

public class QuestionDao {
    private final Connection conn;
    private final OptionDao optionDAO;

    public QuestionDao(Connection conn) {
        this.conn = conn;
        this.optionDAO = new OptionDao(conn);
    }

    public void save(Question question, int quizId) throws SQLException {
        String sql = "INSERT INTO Question (description, correctOption, quiz_id) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, question.getDescription());
            stmt.setInt(2, question.getCorrectOption());
            stmt.setInt(3, quizId);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int questionId = rs.getInt(1);
                    for (Option opt : question.getOptions()) {
                        optionDAO.save(opt, questionId);
                    }
                }
            }
        }
    }

    public List<Question> findByQuizId(int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, description, correctOption FROM Question WHERE quiz_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    List<Option> options = optionDAO.findByQuestionId(id);
                    questions.add(new Question(id, rs.getInt("correctOption"), rs.getString("description"), options));
                }
            }
        }
        return questions;
    }

    public void deleteById(int id) throws SQLException {
        // Primeiro deleta todas as opções da questão
        optionDAO.deleteByQuestionId(id);
        // Depois deleta a questão
        String sql = "DELETE FROM Question WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void update(Question question) throws SQLException {
        // Atualiza a questão
        String sql = "UPDATE Question SET description = ?, correctOption = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.getDescription());
            stmt.setInt(2, question.getCorrectOption());
            stmt.setInt(3, question.getId());
            stmt.executeUpdate();
        }
        
        // Remove opções antigas
        optionDAO.deleteByQuestionId(question.getId());
        
        // Insere novas opções
        for (Option option : question.getOptions()) {
            optionDAO.save(option, question.getId());
        }
    }
}

