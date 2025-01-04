package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface InputDataRepository extends JpaRepository<Course, Long> {
    List<Course> findByCourseDateBetween(Date courseDateAfter, Date courseDateBefore);

    @Query("SELECT DISTINCT teacher1 FROM Course")
    List<String> findTeacher1();

    @Query("SELECT DISTINCT teacher2 FROM Course")
    List<String> findTeacher2();

    @Query("SELECT DISTINCT teacher3 FROM Course")
    List<String> findTeacher3();
}
