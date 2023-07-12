package com.navv.repository;

import com.navv.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
//    @Query(value = "select f from Feedback f where f.name like:key " ,nativeQuery = true)
    List<Feedback> findByName(@Param("key") String name);
}
