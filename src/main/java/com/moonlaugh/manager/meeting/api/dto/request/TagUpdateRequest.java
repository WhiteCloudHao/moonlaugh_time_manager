package com.moonlaugh.manager.meeting.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagUpdateRequest {
    public Integer id;
    private String name;
}
