package com.chiknas.votingsystem.data.repositories;

import com.chiknas.votingsystem.data.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.chiknas.votingsystem.data.repositories.PollRepository, created on 06/11/2019 10:29 <p>
 * @author NikolaosK
 */
public interface PollRepository extends JpaRepository<Poll, Long> {
}
