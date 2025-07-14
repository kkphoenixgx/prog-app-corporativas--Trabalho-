
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

    //? ----------- Create -----------

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

    public int createQuiz(Quiz quiz) throws SQLException {
        String sql = "INSERT INTO Quiz (subject, answered_at) VALUES (?, ?)";
        
        int errorFlag = -1;

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
                    return quizId;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return errorFlag;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return errorFlag;
        }

        return errorFlag;
    }


    //? ----------- Read -----------

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

    public List<Quiz> findBySubject(String subject) throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT id, subject, answered_at FROM Quiz WHERE subject LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + subject + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String quizSubject = rs.getString("subject");
                    Date answaredAt = new Date(rs.getTimestamp("answered_at").getTime());
                    List<Question> questions = questionDAO.findByQuizId(id);
                    quizzes.add(new Quiz(id, quizSubject, answaredAt, questions));
                }
            }
        }
        return quizzes;
    }

    public List<Quiz> findByName(String name) throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT id, subject, answered_at FROM Quiz WHERE subject LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String subject = rs.getString("subject");
                    Date answaredAt = new Date(rs.getTimestamp("answered_at").getTime());
                    List<Question> questions = questionDAO.findByQuizId(id);
                    quizzes.add(new Quiz(id, subject, answaredAt, questions));
                }
            }
        }
        return quizzes;
    }

    //? ----------- Delete -----------

    public void deleteById(int id) throws SQLException {
        // Primeiro deleta as questões (que automaticamente deleta as opções)
        List<Question> questions = questionDAO.findByQuizId(id);
        for (Question question : questions) {
            questionDAO.deleteById(question.getId());
        }
        
        // Depois deleta o quiz
        String sql = "DELETE FROM Quiz WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    //? ----------- Update -----------

    public void update(Quiz quiz) throws SQLException {
        // Atualiza o quiz
        String sql = "UPDATE Quiz SET subject = ?, answered_at = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quiz.getSubject());
            stmt.setTimestamp(2, new Timestamp(quiz.getAnswaredAt().getTime()));
            stmt.setInt(3, quiz.getId());
            stmt.executeUpdate();
        }
        
        // Busca questões existentes
        List<Question> oldQuestions = questionDAO.findByQuizId(quiz.getId());
        
        // Atualiza questões existentes e insere novas
        for (Question newQuestion : quiz.getQuestions()) {
            if (newQuestion.getId() > 0) {
                // Atualiza questão existente
                questionDAO.update(newQuestion);
            } else {
                // Insere nova questão
                questionDAO.save(newQuestion, quiz.getId());
            }
        }
        
        // Remove questões que não estão mais na lista
        for (Question oldQuestion : oldQuestions) {
            boolean stillExists = quiz.getQuestions().stream()
                .anyMatch(q -> q.getId() == oldQuestion.getId());
            if (!stillExists) {
                questionDAO.deleteById(oldQuestion.getId());
            }
        }
    }
}
