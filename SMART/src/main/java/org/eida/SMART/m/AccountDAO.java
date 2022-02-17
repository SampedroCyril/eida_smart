package org.eida.SMART.m;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    List<Account> findByUser(User user);
    List<Account> findByType(String type);
    List<Account> findByTypeAndUser(String courant, User user);
}
