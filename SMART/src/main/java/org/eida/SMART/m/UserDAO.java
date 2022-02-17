package org.eida.SMART.m;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findById(int id);
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByActiveAndAccessLevel(boolean active, int accessLevel);
    List<User> findByAccessLevel(int accessLevel);
    List<User> findByAddDate(int accessLevel);
}
