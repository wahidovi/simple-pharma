package com.simplepharma.backend.repository;

import com.simplepharma.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPasswordAndUserType(String email, String password, String userType);

    Optional<User> findByUsername(String username);
}
