package com.chiknas.votingsystem.data.model;

import com.chiknas.votingsystem.data.EntityIdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * com.chiknas.votingsystem.data.model.Poll, created on 06/11/2019 09:57 <p>
 * @author NikolaosK
 */
@Entity
public class Poll {
  @Id
  @GeneratedValue(generator = EntityIdGenerator.generatorName)
  @GenericGenerator(name = EntityIdGenerator.generatorName, strategy = "com.chiknas.votingsystem.data.EntityIdGenerator")
  private Long id;

  private String question;

  @OneToMany(mappedBy = "poll", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Answer> answers = new ArrayList<>();

  private boolean isAnonymous = false;

  private String author;

  public Long getId() {
    return id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public Boolean isAnonymous() {
    return isAnonymous;
  }

  public void setIsAnonymous(Boolean isAnonymous) {
    this.isAnonymous = isAnonymous;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void addAnswer(String answer) {
    Answer newAnswer = new Answer();
    newAnswer.setTitle(answer);
    newAnswer.setPoll(this);
    answers.add(newAnswer);
  }

}
