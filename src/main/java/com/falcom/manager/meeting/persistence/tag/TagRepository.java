package com.falcom.manager.meeting.persistence.tag;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(
            value = "SELECT * FROM tag u WHERE u.user_id = ?1",
            nativeQuery = true)
    List<Tag> findByUserId(Integer userId);
}
