package com.example.modernnotesapp.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.modernnotesapp.R;
import com.example.modernnotesapp.callback.OnNotesListener;
import com.example.modernnotesapp.database.Note;
import com.example.modernnotesapp.databinding.NoteItemBinding;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private NoteItemBinding noteItemBinding;
    private OnNotesListener notesListener;


    public NotesAdapter(List<Note> noteList, OnNotesListener notesListener) {
        this.noteList = noteList;
        this.notesListener = notesListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        noteItemBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


        return new NoteViewHolder(noteItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNoteContent(noteList.get(position));
        noteItemBinding.noteItemRoot.setOnClickListener(v ->
                notesListener.OnNoteClickListener(noteList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private NoteViewHolder(@NonNull NoteItemBinding itemBinding) {
            super(itemBinding.getRoot());
        }

        public void setNoteContent(Note note){
            noteItemBinding.noteItemTitle.setText(note.getNoteTitle());
            noteItemBinding.noteItemSubtitle.setText(note.getNoteSubtitle());
            noteItemBinding.noteItemDateTime.setText(note.getNoteDateTime());

            GradientDrawable rootBackground = (GradientDrawable) noteItemBinding.noteItemRoot.getBackground();
            if(note.getNoteColor() != null){
                rootBackground.setColor(Color.parseColor(note.getNoteColor()));
            }else{
                rootBackground.setColor(Color.parseColor("#333333"));
            }

            if(note.getNoteImagePath()!=null){
                noteItemBinding.noteRoundedImage.setImageBitmap(BitmapFactory.decodeFile(note.getNoteImagePath()));
                noteItemBinding.noteRoundedImage.setVisibility(View.VISIBLE);
            }else{
                noteItemBinding.noteRoundedImage.setVisibility(View.GONE);
            }


        }
    }
}
