package com.dprol.analytic_service.repository;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository

public interface AnalyticRepository extends JpaRepository<Analytic, Long> {

    Stream<Analytic> findByReceiverIdAndType(Long receiverId, Type type);
}
