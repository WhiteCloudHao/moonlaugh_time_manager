package com.moonlaugh.manager.meeting.persistence.noti;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer hour;
    private Integer minute;
    private String title;
    private String description;
    private String tag;
    private Integer user_id;
//    remove becase when using it, error when getAllByUserId. Why? i don't know
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id")
//    private User user;
}
