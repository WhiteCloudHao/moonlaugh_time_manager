package com.moonlaugh.manager.meeting.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotiUpdateRequest {
    public Integer id;
    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;

    public String title;
    public String description;
    public String tag;
//    public Integer user_id;
}
