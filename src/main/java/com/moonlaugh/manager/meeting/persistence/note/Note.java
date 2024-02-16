package com.moonlaugh.manager.meeting.persistence.note;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String description;
    private Integer user_id;
}
