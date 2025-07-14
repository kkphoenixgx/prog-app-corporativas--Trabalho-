import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dao.ConnectionFactory;
import dao.QuizDao;
import model.Option;
import model.Question;
import model.Quiz;

import static org.junit.jupiter.api.Assertions.*;

public class QuizDaoTest {

  private static Connection conn;
  private static QuizDao quizDao;

  @BeforeAll
  public static void setup() {
    
    try {
      conn = ConnectionFactory.getTestConnection();
      if (conn == null) fail("Conexão com o banco de testes falhou.");
      quizDao = new QuizDao(conn);
    } 
    catch (Exception e) {
      e.printStackTrace();
      fail("Erro ao configurar conexão e DAOs: " + e.getMessage());
    }

  }

  @AfterAll
  public static void tearDown() {
 
    try { if ( conn != null && !conn.isClosed() ) conn.close(); } 
    catch (Exception e) { e.printStackTrace(); }
 
  }

  //? ---------- CRUD Tests ----------

  @Test
  public void testCreate() {
    Quiz quiz = createMockQuiz("História");

    try {
      quizDao.save(quiz);
      List<Quiz> quizzes = quizDao.findBySubject("História");
      assertFalse(quizzes.isEmpty(), "Quiz não foi salvo corretamente.");
      System.out.println("[CREATE] Quiz salvo com ID: " + quizzes.get(0).getId());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Erro ao salvar quiz: " + e.getMessage());
    }
  }

  @Test
  public void testReadAll() {
    try {
      List<Quiz> quizzes = quizDao.findAll();
      assertNotNull(quizzes);
      System.out.println("[READ] Total de quizzes: " + quizzes.size());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Erro ao buscar quizzes: " + e.getMessage());
    }
  }
  @Test
  public void testListarUm() {

    try {
      Quiz quiz = createMockQuiz("Matemática");
      quizDao.save(quiz);

      List<Quiz> quizzes = quizDao.findBySubject("Matemática");
      assertFalse(quizzes.isEmpty());

      int id = quizzes.get(0).getId();

      Quiz found = quizDao.findById(id);
      assertNotNull(found);
      assertEquals("Matemática", found.getSubject());
      System.out.println("[LISTAR UM] Quiz encontrado com ID: " + id);
    
    } 
    catch (Exception e) {
      e.printStackTrace();
      fail("Erro no listarUm: " + e.getMessage());
    }

  }


  @Test
  public void testUpdate() {
    try {
      List<Quiz> quizzes = quizDao.findAll();
      assertFalse(quizzes.isEmpty(), "Nenhum quiz encontrado para atualizar.");

      Quiz quiz = quizzes.get(0);
      String oldSubject = quiz.getSubject();
      quiz.setSubject("Atualizado: " + oldSubject);

      quizDao.update(quiz);

      Quiz updated = quizDao.findBySubject("Atualizado").stream()
          .filter(q -> q.getId() == quiz.getId())
          .findFirst()
          .orElse(null);

      assertNotNull(updated, "Quiz não foi atualizado corretamente.");
      assertTrue(updated.getSubject().startsWith("Atualizado"), "Subject não foi modificado.");
      System.out.println("[UPDATE] Atualizado para: " + updated.getSubject());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Erro ao atualizar quiz: " + e.getMessage());
    }
  }

  @Test
  public void testDelete() {
    try {
      List<Quiz> quizzes = quizDao.findAll();
      assertFalse(quizzes.isEmpty(), "Nenhum quiz disponível para deletar.");

      int idToDelete = quizzes.get(0).getId();
      quizDao.deleteById(idToDelete);

      List<Quiz> remaining = quizDao.findAll();
      boolean exists = remaining.stream().anyMatch(q -> q.getId() == idToDelete);

      assertFalse(exists, "Quiz ainda existe após tentativa de deleção.");
      System.out.println("[DELETE] Quiz com ID " + idToDelete + " deletado com sucesso.");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Erro ao deletar quiz: " + e.getMessage());
    }
  }


  //? ---------- HELPERS ----------

  private Quiz createMockQuiz(String subject) {
    Question q1 = new Question(0, "Quem descobriu o Brasil?", List.of(
      new Option("Pedro Álvares Cabral"),
      new Option("Dom Pedro I"),
      new Option("Tiradentes")
    ));
    q1.setCorrectOption(0);

    Question q2 = new Question(0, "Em que ano foi proclamada a República?", List.of(
      new Option("1822"),
      new Option("1889"),
      new Option("1500")
    ));
    q2.setCorrectOption(1);

    return new Quiz(subject, new Date(), List.of(q1, q2));
  }
}
