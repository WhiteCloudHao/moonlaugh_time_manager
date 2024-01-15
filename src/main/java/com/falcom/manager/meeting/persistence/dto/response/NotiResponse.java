package com.falcom.manager.meeting.persistence.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotiResponse {
    public Integer id;
    public Integer day;
    public Integer month;
    public Integer year;
    public Integer hour;
    public Integer minute;

    public String title;
    public String description;
    public String tag;
    public Integer user_id;
}
