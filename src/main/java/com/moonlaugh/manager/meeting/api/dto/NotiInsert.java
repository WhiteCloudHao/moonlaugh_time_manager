package com.moonlaugh.manager.meeting.api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotiInsert {
    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;

    public String title;
    public String description;
    public String tag;

}
