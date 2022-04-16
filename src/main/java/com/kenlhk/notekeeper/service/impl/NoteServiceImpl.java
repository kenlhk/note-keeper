package com.kenlhk.notekeeper.service.impl;

import com.kenlhk.notekeeper.exception.ApiRequestException;
import com.kenlhk.notekeeper.model.Note;
import com.kenlhk.notekeeper.repository.NoteRepository;
import com.kenlhk.notekeeper.service.AuthenticationService;
import com.kenlhk.notekeeper.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final AuthenticationService authenticationService;

    @Override
    public List<Note> findAllNotes() {
        return noteRepository.findAllByUserId(authenticationService.findCurrentUser().getId());
    }

    @Override
    public Note findNoteById(long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Note not found.", HttpStatus.NOT_FOUND));
        if (!authenticationService.isCurrentUser(note.getUser())) {
            throw new ApiRequestException("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }
        return note;
    }

    @Override
    public Note saveNote(Note note) {
        note.setUser(authenticationService.findCurrentUser());
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Note note, long id) {
        Note noteFromDb = noteRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Note not found.", HttpStatus.NOT_FOUND));
        if (!authenticationService.isCurrentUser(noteFromDb.getUser())) {
            throw new ApiRequestException("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }
        noteFromDb.setSubject(note.getSubject());
        noteFromDb.setContent(note.getContent());
        noteFromDb.setSource(note.getSource());
        noteFromDb.setTags(note.getTags());
        noteFromDb.setEditedAt(LocalDateTime.now());
        return noteRepository.save(noteFromDb);
    }

    @Override
    public void deleteNote(long id) {
        Note noteFromDb = noteRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Note not found.", HttpStatus.NOT_FOUND));
        if (!authenticationService.isCurrentUser(noteFromDb.getUser())) {
            throw new ApiRequestException("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }
        noteRepository.delete(noteFromDb);
    }
}
