package model;

public class Option {

  private int id;
  private String description;

  public Option(int id, String description){
    this.id = id;
    this.description = description;
  }
  public Option(String description){
    this.description = description;
  }

  // ----------- Gettters and Setters -----------

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  
}