package com.mateo.springsecurity.repository;

import com.mateo.springsecurity.model.Role;
import com.mateo.springsecurity.model.UserSec;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {
    Optional<UserSec> findUserEntityByUsername(String username);
}
