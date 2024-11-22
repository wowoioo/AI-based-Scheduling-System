package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.InputData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface InputDataRepository extends JpaRepository<InputData, Long> {
    List<InputData> findByCourseDateBetween(Date courseDateAfter, Date courseDateBefore);
}
