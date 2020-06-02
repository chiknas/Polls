package com.chiknas.votingsystem.data.services;

import com.chiknas.votingsystem.data.model.Answer;
import com.chiknas.votingsystem.data.model.Poll;
import com.chiknas.votingsystem.data.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * com.chiknas.votingsystem.data.services.PollService, created on 06/11/2019 10:38 <p>
 * @author NikolaosK
 */
@Service
public class PollService {

  private final PollRepository pollRepository;

  @Autowired
  public PollService(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  public Poll getPollFromId(Long id) {
    return pollRepository.findById(id).orElse(null);
  }

  public Poll save(Poll poll) {
    return pollRepository.save(poll);
  }

  public List<Poll> getAllPolls() {
    return pollRepository.findAll();
  }

  public void deleteById(Long id) {pollRepository.deleteById(id);}

  public String getWinningAnswer(Long pollId) {
    TreeSet<Answer> answerTreeSet = new TreeSet<>(Comparator.comparingInt(o -> o.getSelectedByUsers().size()));
    pollRepository.findById(pollId).ifPresent(poll -> answerTreeSet.addAll(poll.getAnswers()));
    return answerTreeSet.last().getTitle();
  }

  /**
   * Returns a map that contains as a key the voter's name and param the voter's selected answer
   */
  public List<String> getVoters(Long pollId) {
    List<String> result = new ArrayList<>();
    pollRepository.findById(pollId).ifPresent(poll -> {
      poll.getAnswers().forEach(answer -> result.addAll(answer.getSelectedByUsers()));
    });
    return result;
  }
}
