package com.shravana.jobtracker.repository;

import com.shravana.jobtracker.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // 🔥 Fetch jobs for a specific user
    List<Job> findByUsername(String username);
}