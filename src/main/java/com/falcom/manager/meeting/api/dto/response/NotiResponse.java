package com.falcom.manager.meeting.api.dto.response;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class NotiResponse {
    public Integer id;
    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;

    public String title;
    public String description;
    public String tag;
//    status 0: chua het han, 1 het han
    public String status;
    public String expDate;
}
