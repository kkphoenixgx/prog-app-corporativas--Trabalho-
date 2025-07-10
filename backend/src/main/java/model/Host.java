package model;

public class Host {

  private int id;
  private String name;
  private String email;
  private String password;
  private String fontColor;
  private String backgroundColor;

  public Host(int id, String name, String email, String password, String fontColor, String backgroundColor) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.fontColor = fontColor;
    this.backgroundColor = backgroundColor;
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

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public String getFontColor() {
    return fontColor;
  }
  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }
  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }
}