package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Option;

public class OptionDao {
  private final Connection conn;

  public OptionDao(Connection conn) {
    this.conn = conn;
  }

  public void save(Option option, int questionId) throws SQLException {
    String sql = "INSERT INTO OptionTable (description, question_id) VALUES (?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, option.getDescription());
        stmt.setInt(2, questionId);
        stmt.executeUpdate();
    }
  }

  public List<Option> findByQuestionId(int questionId) throws SQLException {
    List<Option> options = new ArrayList<>();
    String sql = "SELECT id, description FROM OptionTable WHERE question_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, questionId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                options.add(new Option(rs.getInt("id"), rs.getString("description")));
            }
        }
    }
    return options;
  }

  public void deleteById(int id) throws SQLException {
    String sql = "DELETE FROM OptionTable WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
  }

  public void deleteByQuestionId(int questionId) throws SQLException {
    String sql = "DELETE FROM OptionTable WHERE question_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, questionId);
        stmt.executeUpdate();
    }
  }
}