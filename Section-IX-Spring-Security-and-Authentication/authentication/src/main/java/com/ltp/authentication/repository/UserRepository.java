package com.ltp.authentication.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.ltp.authentication.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}