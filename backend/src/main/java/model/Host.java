package model;

public class Host {

  private int id;
  private String name;
  private String password;

  public Host(int id, String name, String password){
    this.id = id;
    this.name = name;
    this.password = password;
  }

  // ----------- Gettters and Setters -----------

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  
}