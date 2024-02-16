package com.moonlaugh.manager.meeting.service;

import com.moonlaugh.manager.meeting.api.dto.request.NoteInsertRequest;
import com.moonlaugh.manager.meeting.api.dto.request.NoteUpdateRequest;
import com.moonlaugh.manager.meeting.persistence.note.Note;
import com.moonlaugh.manager.meeting.persistence.note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteService {
    private final NoteRepository noteRepo;
    private final AuthenticationService authenticationService;
    @Autowired
    NoteService(NoteRepository noteRepo, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.noteRepo = noteRepo;
    }

    public Map<String, String> addNote(NoteInsertRequest noteRequest) {
        Map<String, String> res = new HashMap<>();
        Integer userId = authenticationService.getUserId();
        Note note = Note.builder()
                .title(noteRequest.getTitle())
                .description(noteRequest.getDescription())
                .user_id(userId)
                .build();
        res.put("message", "success");
        noteRepo.save(note);
        return  res;
    }
    public Map<String, String> updateNote(NoteUpdateRequest noteRequest) throws Exception {
        Map<String, String> response = new HashMap<>();
        Integer userId = authenticationService.getUserId();
        Note note = Note.builder()
                .id(noteRequest.getId())
                .title(noteRequest.getTitle())
                .description(noteRequest.getDescription())
                .user_id(userId)
                .build();
        noteRepo.save(note);
        response.put("message", "update tag success");
        return response;
    }
    public boolean delete(Integer id) {
        noteRepo.deleteById(id);
        return true;
    }
    public List<Note> getListNote() {
        Integer userId = authenticationService.getUserId();
        Map<String, Object> resInfo = new HashMap<>();
        List<Note> resList;
        resList = noteRepo.findByUserId(userId);
        return resList;
    }
    public Note getDetail(Integer id) {
        Note noteDetail = noteRepo.findById(id).orElseThrow(() -> new NotFoundException("Notification not found with id: " + id));
        return  noteDetail;
    }
}
