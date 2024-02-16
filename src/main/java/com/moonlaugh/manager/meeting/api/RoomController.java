package com.moonlaugh.manager.meeting.api;

import com.moonlaugh.manager.meeting.service.RoomService;
import com.moonlaugh.manager.meeting.untils.ConstantMessages;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Tag(name="Room")
@RestController
@RequestMapping(value = "v1/room")
public class RoomController {
    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Hidden
    @Operation(summary = "Get List Room", description = "Get all room of company")
    @GetMapping(path = "/list")
    public ResponseEntity<Object> getListRoom() {
        Map<String, Object> res = new HashMap<>();
        try {
            res = roomService.getListRoom();
        } catch (Exception ex) {
            res.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
