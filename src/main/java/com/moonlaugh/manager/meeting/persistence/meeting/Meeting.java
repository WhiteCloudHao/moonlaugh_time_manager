package com.moonlaugh.manager.meeting.persistence.meeting;

import com.moonlaugh.manager.meeting.persistence.room.Room;
import com.moonlaugh.manager.meeting.persistence.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Meeting {
    @Id
    @GeneratedValue
    private Integer id;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private Integer status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_create_id")
    public User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    public Room room;

}
