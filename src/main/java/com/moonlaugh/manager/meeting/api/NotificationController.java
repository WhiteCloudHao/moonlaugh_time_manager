package com.moonlaugh.manager.meeting.api;


import com.moonlaugh.manager.meeting.api.dto.NotiInsert;
import com.moonlaugh.manager.meeting.api.dto.request.NotiUpdateRequest;
import com.moonlaugh.manager.meeting.api.dto.response.NotiResponse;
import com.moonlaugh.manager.meeting.persistence.noti.Notification;
import com.moonlaugh.manager.meeting.service.NotiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Notification")
@RestController
@RequestMapping(value = "/api/v1/noti")
public class NotificationController {
    private final NotiService notiService;

    @Autowired
    NotificationController(NotiService notiService) {
        this.notiService = notiService;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insertNoti(
            @RequestBody NotiInsert notiInsert
    ) throws Exception {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        Integer day = notiInsert.getDay();
        Integer month = notiInsert.getMonth();
        Integer year = notiInsert.getYear();
        Integer hour = notiInsert.getHour();
        Integer minute = notiInsert.getMinute();
        String title = notiInsert.getTitle();
        String description = notiInsert.getDescription();
//        if(day == null) {
//            errors.put(ConstantMessages.DO_UU_TIEN, ConstantMessages.ERROR_DO_UU_TIEN_MUST_BE_A_NUMBER_AND_IN_1_TO_65000);
//        }

        notiInsert.setTitle(title.trim());
        notiInsert.setDescription(description.trim());
        notiInsert.setTag("test");
        NotiResponse notiResponse = notiService.addNoti(notiInsert);
        response.put("data", notiResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListNotification() throws Exception {
        Map<String, Object> response = new HashMap<>();
//        JSONObject res;
        List<Notification> notiList = notiService.getListNoti();
        List<NotiResponse> notiResponses = notiList.stream()
                .map(notification -> {
                    String  date = notification.getDay() + "/" + (notification.getMonth() + 1) + "/" + notification.getYear();
                    String status;
                    LocalDateTime notiTime = LocalDateTime.of(notification.getYear(), (notification.getMonth() + 1) , notification.getDay(), notification.getHour(), notification.getHour());
                    LocalDateTime now = LocalDateTime.now();
                    if (notiTime.isAfter(now)) {
                        status = "0";
                    } else {
                        status = "1";
                    }
                    NotiResponse notiResponse =
                            NotiResponse.builder()
                                    .id(notification.getId())
                                    .day(notification.getDay())
                                    .month(notification.getMonth())
                                    .year(notification.getYear())
                                    .hour(notification.getHour())
                                    .minute(notification.getMinute())
                                    .title(notification.getTitle())
                                    .description(notification.getDescription())
                                    .tag(notification.getTag())
                                    .status(status)
                                    .expDate(date)
                                    .build();
                    // Đặt các trường của notiResponse dựa trên các trường của notification
                    // Ví dụ: notiResponse.setSomeField(notification.getSomeField());
                    return notiResponse;
                })
                .collect(Collectors.toList());
        response.put("data", notiResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateNoti(@RequestBody
    NotiUpdateRequest notiUpdate) {
        Map<String, String>  res = new HashMap<>();
        if(notiUpdate.getTitle()==null) {
            notiUpdate.setTitle("");
        }
        if(notiUpdate.getDescription() == null) {
            notiUpdate.setDescription("");
        }
        if(notiUpdate.getTag()==null || notiUpdate.getTag()=="") {
            notiUpdate.setTag("Test");
        }
        try {
            res = notiService.updateNoti(notiUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<> (res, HttpStatus.OK);
    }

    @GetMapping (path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDetail(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        Notification notiDetail = notiService.getDetail(id);
        String  date = notiDetail.getDay() + "/" + (notiDetail.getMonth() + 1) + "/" + notiDetail.getYear();
        String status;
        LocalDateTime notiTime = LocalDateTime.of(notiDetail.getYear(), (notiDetail.getMonth() + 1) , notiDetail.getDay(), notiDetail.getHour(), notiDetail.getHour());
        LocalDateTime now = LocalDateTime.now();
        if (notiTime.isAfter(now)) {
            status = "0";
        } else {
            status = "1";
        }
        NotiResponse notiResponse =
                NotiResponse.builder()
                        .id(notiDetail.getId())
                        .day(notiDetail.getDay())
                        .month(notiDetail.getMonth())
                        .year(notiDetail.getYear())
                        .hour(notiDetail.getHour())
                        .minute(notiDetail.getMinute())
                        .title(notiDetail.getTitle())
                        .description(notiDetail.getDescription())
                        .tag(notiDetail.getTag())
                        .status(status)
                        .expDate(date)
                        .build();
        res.put("data", notiResponse);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteNoti(@PathVariable Integer id) throws Exception {
        boolean deleteResult = notiService.delete(id);
        Map<String, String> res = new HashMap<>();

       if(deleteResult) {
           res.put("message", "success");
       } else {
           res.put("message", "failed");
       }
       return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

