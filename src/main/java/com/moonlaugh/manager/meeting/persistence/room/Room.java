package com.moonlaugh.manager.meeting.persistence.room;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue
    public Integer id;
    @Column(unique = true)
    private String roomName;
    private Integer sucChua;
    private Integer status;
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", sucChua='" + sucChua +
                '}';
    }

}
