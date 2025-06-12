
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Statement;

import model.Question;
import model.Quiz;

public class QuizDao {
    private final Connection conn;
    private final QuestionDao questionDAO;

    public QuizDao(Connection conn) {
        this.conn = conn;
        this.questionDAO = new QuestionDao(conn);
    }

    public void save(Quiz quiz) throws SQLException {
        String sql = "INSERT INTO Quiz (subject, answered_at) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quiz.getSubject());
            stmt.setTimestamp(2, new Timestamp(quiz.getAnswaredAt().getTime()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int quizId = rs.getInt(1);
                    for (Question question : quiz.getQuestions()) {
                        questionDAO.save(question, quizId);
                    }
                }
            }
        }
    }

    public List<String> findDistinctSubjects() throws SQLException {
        List<String> subjects = new ArrayList<>();
        String sql = "SELECT DISTINCT subject FROM Quiz ORDER BY subject";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subjects.add(rs.getString("subject"));
            }
        }
        return subjects;
    }

    public List<Quiz> findAll() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT id, subject, answered_at FROM Quiz";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String subject = rs.getString("subject");
                Date answaredAt = new Date(rs.getTimestamp("answered_at").getTime());
                List<Question> questions = questionDAO.findByQuizId(id);
                quizzes.add(new Quiz(id, subject, answaredAt, questions));
            }
        }
        return quizzes;
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Quiz WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
