package com.moonlaugh.manager.meeting.persistence.noti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(
            value = "SELECT * FROM notification u WHERE u.user_id = ?1",
            nativeQuery = true)
    List<Notification> findByUserId(Integer userId);
}
