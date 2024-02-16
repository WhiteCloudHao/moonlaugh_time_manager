package com.moonlaugh.manager.meeting.service;

import com.moonlaugh.manager.meeting.persistence.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
//@RequiredArgsConstructor

public class RoomServiceImpl implements  RoomService{
    Map<String, Object> reqInfo = new HashMap<>();
    private final RoomRepository roomRepository;
    @Autowired
    public RoomServiceImpl (RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    @Override
    public Map<String, Object> getListRoom() {
            long count = roomRepository.count();
            List resList = roomRepository.findAll();
            reqInfo.put("total", count);
            reqInfo.put("data", resList);
            return reqInfo;
    }

}
