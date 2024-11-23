package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.InputData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface InputDataRepository extends JpaRepository<InputData, Long> {
    List<InputData> findByCourseDateBetween(Date courseDateAfter, Date courseDateBefore);

    @Query("SELECT DISTINCT teacher1 FROM InputData")
    List<String> findTeacher1();

    @Query("SELECT DISTINCT teacher2 FROM InputData")
    List<String> findTeacher2();

    @Query("SELECT DISTINCT teacher3 FROM InputData")
    List<String> findTeacher3();
}
