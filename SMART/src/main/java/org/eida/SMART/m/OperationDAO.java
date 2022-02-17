package org.eida.SMART.m;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationDAO extends JpaRepository<Operation, Integer> {
    List<Operation> findByOperationAccount (Account id);
}
