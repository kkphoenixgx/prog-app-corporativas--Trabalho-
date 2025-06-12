package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Quiz {

  private int id;
  private String subject;
  private Date answaredAt;
  private List<Question> questions = new ArrayList<>(); 
 

  public Quiz(int id, String subject, Date answaredAt, List<Question> questions) {
    this.id = id;
    this.subject = subject;
    this.answaredAt = answaredAt;
    this.questions = questions;
  }
  
  public Quiz(String subject, Date answaredAt, List<Question> questions) {
    this.subject = subject;
    this.answaredAt = answaredAt;
    this.questions = questions;
  }

  public Quiz(Date answaredAt, int id, String subject) {
    this.answaredAt = answaredAt;
    this.id = id;
    this.subject = subject;
  }

  public Quiz(Date answaredAt, String subject) {
    this.answaredAt = answaredAt;
    this.subject = subject;
  }


  public void addQuestion(Question newQuestion) {
    this.questions.add(newQuestion);
  }

  public void removeQuestion(Question question) {
    this.questions.remove(question);
  }

  public void removeQuestionById(int id) {
    this.questions.removeIf(question -> question.getId() == id);

  }

  // ----------- Getters and Setters -----------

  public int getId() {
    return id;
  }
  public void setId(int id) {
      this.id = id;
  }

  public String getSubject() {
      return subject;
  }
  public void setSubject(String subject) {
      this.subject = subject;
  }

  public Date getAnswaredAt() {
      return answaredAt;
  }
  public void setAnswaredAt(Date answaredAt) {
      this.answaredAt = answaredAt;
  }

  public List<Question> getQuestions() {
    return questions;
  }
  public void setQuestions(List<Question> questions) {
      this.questions = questions;
  }

}