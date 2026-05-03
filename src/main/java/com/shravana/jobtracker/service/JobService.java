package com.shravana.jobtracker.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shravana.jobtracker.model.Job;
import com.shravana.jobtracker.repository.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository repo;

    public Job saveJob(Job job) {
        return repo.save(job);
    }
    

    public List<Job> getAllJobs() {
        return repo.findAll();
    }
    
    public List<Job> getJobsByUser(String username) {
        return repo.findByUsername(username);
    }

    public Job getJobById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void deleteJob(Long id) {
        repo.deleteById(id);
    }
}
