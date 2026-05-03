package com.shravana.jobtracker.controller;

import com.shravana.jobtracker.model.Job;
import com.shravana.jobtracker.service.JobService;
import com.shravana.jobtracker.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService service;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ GET: Fetch jobs for logged-in user
    @GetMapping
    public List<Job> getJobs(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        return service.getJobsByUser(username);
    }

    // ✅ POST: Add job for logged-in user
    @PostMapping
    public Job addJob(@RequestBody Job job, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        job.setUsername(username); // 🔥 assign user

        return service.saveJob(job);
    }

    // ✅ PUT: Update job (only if belongs to user)
    @PutMapping("/{id}")
    public String updateJob(@PathVariable Long id, @RequestBody Job updatedJob, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        Job existingJob = service.getJobById(id);

        if (!existingJob.getUsername().equals(username)) {
            return "Unauthorized";
        }

        updatedJob.setId(id);
        updatedJob.setUsername(username); // maintain ownership

        service.saveJob(updatedJob);
        return "Job updated successfully";
    }

    // ✅ DELETE: Delete job (only if belongs to user)
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        Job job = service.getJobById(id);

        if (!job.getUsername().equals(username)) {
            return "Unauthorized";
        }

        service.deleteJob(id);
        return "Job deleted successfully";
    }
}