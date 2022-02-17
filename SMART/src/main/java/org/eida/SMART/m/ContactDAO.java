package org.eida.SMART.m;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactDAO extends JpaRepository<Contact, Integer> {
    List<Contact> findByUser(User user);
}
