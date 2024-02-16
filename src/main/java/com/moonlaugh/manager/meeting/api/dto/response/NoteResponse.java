package com.moonlaugh.manager.meeting.api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    public Integer id;
    public String title;
    public String description;
}
