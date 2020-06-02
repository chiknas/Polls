package com.chiknas.votingsystem.data.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.HashSet;

/**
 * com.chiknas.votingsystem.data.model.Answer, created on 06/11/2019 10:02 <p>
 * @author NikolaosK
 */
@Entity
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "poll_id")
  private Poll poll;

  private String title;

  private HashSet<String> selectedByUsers = new HashSet<>();

  public Poll getPoll() {
    return poll;
  }

  public void setPoll(Poll poll) {
    this.poll = poll;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public HashSet<String> getSelectedByUsers() {
    return selectedByUsers;
  }

  public void setSelectedByUsers(HashSet<String> selectedByUsers) {
    this.selectedByUsers = selectedByUsers;
  }

  public boolean addUser(String user) {
    return this.selectedByUsers.add(user);
  }
}
