package com.falcom.manager.meeting.api.dto.request;

import lombok.Getter;

@Getter
public class NoteInsertRequest {
    private String title;
    private String description;
}
