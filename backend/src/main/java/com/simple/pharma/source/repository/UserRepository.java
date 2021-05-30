package com.simple.pharma.source.repository;

import java.util.Optional;

import com.simple.pharma.source.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndPasswordAndUsertype(String email, String password, String usertype);

	Optional<User> findByUsername(String username);

}
