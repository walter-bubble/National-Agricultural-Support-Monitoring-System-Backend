package com.Farm.NASMS.Repository;

import com.Farm.NASMS.User;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SecurityProperties.User,Long> {
    Optional<Object> findUserByUserName(String username);
}
