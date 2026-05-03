package com.shravana.jobtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shravana.jobtracker.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
