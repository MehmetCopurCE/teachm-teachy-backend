package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
