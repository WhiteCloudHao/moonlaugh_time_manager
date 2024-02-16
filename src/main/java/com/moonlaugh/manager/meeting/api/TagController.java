package com.moonlaugh.manager.meeting.api;


import com.moonlaugh.manager.meeting.api.dto.request.TagInsertRequest;
import com.moonlaugh.manager.meeting.api.dto.request.TagUpdateRequest;
import com.moonlaugh.manager.meeting.api.dto.response.TagResponse;
import com.moonlaugh.manager.meeting.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Tag")
@RestController
@RequestMapping(value = "/api/v1/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addTag(@RequestBody TagInsertRequest tagRequest) {
        Map<String, String> response = new HashMap<>();
        response = tagService.addTag(tagRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTag(@RequestBody
                                             TagUpdateRequest tagRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            response = tagService.updateTag(tagRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteTag(@PathVariable Integer id) throws Exception {
        boolean deleteResult = tagService.delete(id);
        Map<String, String> res = new HashMap<>();

        if(deleteResult) {
            res.put("message", "success");
        } else {
            res.put("message", "failed");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Object> getListNotification() throws Exception {
            Map<String, Object> response = new HashMap<>();
        List<com.moonlaugh.manager.meeting.persistence.tag.Tag> tagList = tagService.getListTag();
        List<TagResponse> tagResponses = tagList.stream()
                .map(tag -> {
                    TagResponse tagResponse =
                            TagResponse.builder()
                                    .id(tag.getId())
                                    .name(tag.getName())
                                    .build();
                    // Đặt các trường của notiResponse dựa trên các trường của notification
                    // Ví dụ: notiResponse.setSomeField(notification.getSomeField());
                    return tagResponse;
                })
                .collect(Collectors.toList());
        response.put("data", tagResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
