package com.garv.SpringSecEx.Repository;

import com.garv.SpringSecEx.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
