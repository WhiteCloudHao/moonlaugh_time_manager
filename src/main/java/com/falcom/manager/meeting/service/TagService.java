package com.falcom.manager.meeting.service;


import com.falcom.manager.meeting.api.dto.request.NotiUpdateRequest;
import com.falcom.manager.meeting.api.dto.request.TagInsertRequest;
import com.falcom.manager.meeting.api.dto.request.TagUpdateRequest;
import com.falcom.manager.meeting.persistence.noti.Notification;
import com.falcom.manager.meeting.persistence.tag.Tag;
import com.falcom.manager.meeting.persistence.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {
    private final TagRepository tagRepo;
    private final AuthenticationService authenticationService;
    @Autowired
    TagService(TagRepository tagRepo, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.tagRepo = tagRepo;
    }

    public Map<String, String> addTag(TagInsertRequest tagRequest) {
        Map<String, String> res = new HashMap<>();
        Integer userId = authenticationService.getUserId();
        Tag tag = Tag.builder()
                .name(tagRequest.getName())
                .user_id(userId)
                .build();
        res.put("message", "success");
        tagRepo.save(tag);
        return  res;
    }
    public Map<String, String> updateTag(TagUpdateRequest tagRequest) throws Exception {
        Map<String, String> response = new HashMap<>();
        Integer userId = authenticationService.getUserId();
        Tag tag = Tag.builder()
                .id(tagRequest.getId())
                .name(tagRequest.getName())
                .user_id(userId)
                .build();
        tagRepo.save(tag);
        response.put("message", "update tag success");
        return response;
    }
    public boolean delete(Integer id) {
        tagRepo.deleteById(id);
        return true;
    }
    public List<Tag> getListTag() {
        Integer userId = authenticationService.getUserId();
        Map<String, Object> resInfo = new HashMap<>();
        List<Tag> resList;
        resList = tagRepo.findByUserId(userId);
        return resList;
    }
}
