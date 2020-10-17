package com.example.modernnotesapp.callback;

import com.example.modernnotesapp.database.Note;

public interface OnNotesListener {

    void OnNoteClickListener(Note note, int position);
}
