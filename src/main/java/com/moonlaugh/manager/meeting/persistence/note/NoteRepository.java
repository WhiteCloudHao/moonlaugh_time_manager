package com.moonlaugh.manager.meeting.persistence.note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    @Query(
            value = "SELECT * FROM note u WHERE u.user_id = ?1",
            nativeQuery = true)
    List<Note> findByUserId(Integer userId);
}
