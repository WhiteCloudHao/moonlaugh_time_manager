package com.moonlaugh.manager.meeting.service;


import com.moonlaugh.manager.meeting.api.dto.NotiInsert;
import com.moonlaugh.manager.meeting.api.dto.request.NotiUpdateRequest;
import com.moonlaugh.manager.meeting.api.dto.response.NotiResponse;
import com.moonlaugh.manager.meeting.persistence.noti.Notification;
import com.moonlaugh.manager.meeting.persistence.noti.NotificationRepository;
import com.moonlaugh.manager.meeting.persistence.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class NotiService {
    private final AuthenticationService authenticationService;
    private final NotificationRepository notiRepo;

    @Autowired
    NotiService(NotificationRepository notiRepo, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.notiRepo = notiRepo;
    }

    public NotiResponse addNoti(NotiInsert notiInsert) {
        Integer userId = authenticationService.getUserId();
        User user = authenticationService.getUser();
        Notification notification = Notification.builder()
                .day(notiInsert.getDay())
                .month(notiInsert.getMonth())
                .year(notiInsert.getYear())
                .hour(notiInsert.getHour())
                .minute(notiInsert.getMinute())
                .title(notiInsert.getTitle())
                .description(notiInsert.getDescription())
                .tag(notiInsert.getTag())
                .user_id(userId)
                .build();
        Notification notificationresponse = notiRepo.save(notification);
        System.out.println("ok");
        String  date = notificationresponse.getDay() + "/" + (notificationresponse.getMonth() + 1) + "/" + notificationresponse.getYear();
        String status;
        LocalDateTime notiTime = LocalDateTime.of(notificationresponse.getYear(), (notificationresponse.getMonth() + 1) , notificationresponse.getDay(), notificationresponse.getHour(), notificationresponse.getHour());
        LocalDateTime now = LocalDateTime.now();
        if (notiTime.isAfter(now)) {
            status = "0";
        } else {
            status = "1";
        }
        NotiResponse notiResponse =
                NotiResponse.builder()
                        .id(notificationresponse.getId())
                        .day(notificationresponse.getDay())
                        .month(notificationresponse.getMonth())
                        .year(notificationresponse.getYear())
                        .hour(notificationresponse.getHour())
                        .minute(notificationresponse.getMinute())
                        .title(notificationresponse.getTitle())
                        .description(notificationresponse.getDescription())
                        .tag(notificationresponse.getTag())
                        .status(status)
                        .expDate(date)
                        .build();
        return notiResponse;
    }

    public List<Notification> getListNoti() {
        Integer userId = authenticationService.getUserId();
        Map<String, Object> resInfo = new HashMap<>();
        List<Notification> resList;
        resList = notiRepo.findByUserId(userId);
        return resList;
    }

    public Map<String, String> updateNoti(NotiUpdateRequest notiUpdate) throws Exception {
        Map<String, String> response = new HashMap<>();
        Integer userId = authenticationService.getUserId();
        Notification notification = Notification.builder()
                .id(notiUpdate.getId())
                .year(notiUpdate.getYear())
                .month(notiUpdate.getMonth())
                .day(notiUpdate.getDay())
                .hour(notiUpdate.getHour())
                .minute(notiUpdate.getMinute())
                .title(notiUpdate.getTitle())
                .description(notiUpdate.getDescription())
                .tag(notiUpdate.getTag())
                .user_id(userId)
                .build();
        notiRepo.save(notification);
        response.put("message", "update noti success");
        return response;
    }
    public Notification getDetail(Integer id) {
        Notification notiDetail = notiRepo.findById(id).orElseThrow(() -> new NotFoundException("Notification not found with id: " + id));
        return  notiDetail;
    }
    public boolean delete(Integer id) {
        notiRepo.deleteById(id);
        return true;
    }
}
