package com.moonlaugh.manager.meeting.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteUpdateRequest {
    public Integer id;
    private String title;
    private String description;
}
