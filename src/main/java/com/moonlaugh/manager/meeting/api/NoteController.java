package com.moonlaugh.manager.meeting.api;

import com.moonlaugh.manager.meeting.api.dto.request.NoteInsertRequest;
import com.moonlaugh.manager.meeting.api.dto.request.NoteUpdateRequest;
import com.moonlaugh.manager.meeting.api.dto.response.NoteResponse;
import com.moonlaugh.manager.meeting.persistence.note.Note;
import com.moonlaugh.manager.meeting.service.NoteService;
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

@Tag(name = "Note")
@RestController
@RequestMapping(value = "/api/v1/note")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addTag(@RequestBody NoteInsertRequest noteRequest) {
        Map<String, String> response = new HashMap<>();
        response = noteService.addNote(noteRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateNote(@RequestBody
                                             NoteUpdateRequest noteRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            response = noteService.updateNote(noteRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteNote(@PathVariable Integer id) throws Exception {
        boolean deleteResult = noteService.delete(id);
        Map<String, String> res = new HashMap<>();

        if(deleteResult) {
            res.put("message", "success");
        } else {
            res.put("message", "failed");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListNote() throws Exception {
        Map<String, Object> response = new HashMap<>();
        List<Note> noteList = noteService.getListNote();
        List<NoteResponse> noteResponses = noteList.stream()
                .map(note -> {
                    NoteResponse noteResponse =
                            NoteResponse.builder()
                                    .id(note.getId())
                                    .title(note.getTitle())
                                    .description(note.getDescription())
                                    .build();
                    // Đặt các trường của notiResponse dựa trên các trường của notification
                    // Ví dụ: notiResponse.setSomeField(notification.getSomeField());
                    return noteResponse;
                })
                .collect(Collectors.toList());
        response.put("data", noteResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping (path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDetail(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        Note noteDetail = noteService.getDetail(id);
        NoteResponse noteResponse =
                NoteResponse.builder()
                        .id(noteDetail.getId())
                        .title(noteDetail.getTitle())
                        .description(noteDetail.getDescription())
                        .build();
        res.put("data", noteResponse);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
