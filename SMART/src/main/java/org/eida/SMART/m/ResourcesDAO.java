package org.eida.SMART.m;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourcesDAO extends JpaRepository<Resources, Integer> {
    List<Resources> findByUser(User user);
    List<Resources> findByCategory(Category category);
}
