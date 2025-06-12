package model;

import java.util.ArrayList;
import java.util.List;

public class Question {

  private int id;
  private int correctOption;
  private String description;
  private List<Option> options = new ArrayList<>(); 

  public Question(int id, int correctOption, String description, List<Option> options){
    this.id = id;
    this.correctOption = correctOption;
    this.description = description;
    this.options = options;
  }

  public Question(int correctOption, String description, List<Option> options){
    this.correctOption = correctOption;
    this.description = description;
    this.options = options;
  }

  public Question(int id, int correctOption, String description){
    this.id = id;
    this.correctOption = correctOption;
    this.description = description;
  }
  
  public Question(int correctOption, String description){
    this.correctOption = correctOption;
    this.description = description;
  }

  public void addOption(Option newOption){
    this.options.add(newOption);
  }
  public void removeOptionById(int id){
    this.options.removeIf(option -> option.getId() == id);
  }
  public void removeOptionByIndex(int index){
    this.options.remove(index);
  }
  public void removeOption(Option option){
    this.options.remove(option);
  }

  // ----------- getters and setters -----------

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCorrectOption() {
    return correctOption;
  }

  public void setCorrectOption(int correctOption) {
    this.correctOption = correctOption;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Option> getOptions() {
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }



}