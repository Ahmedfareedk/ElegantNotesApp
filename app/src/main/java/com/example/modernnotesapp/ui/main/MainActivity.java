package com.example.modernnotesapp.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.modernnotesapp.R;
import com.example.modernnotesapp.adapter.NotesAdapter;
import com.example.modernnotesapp.callback.OnNotesListener;
import com.example.modernnotesapp.database.Note;
import com.example.modernnotesapp.database.NotesDatabase;
import com.example.modernnotesapp.databinding.ActivityMainBinding;
import com.example.modernnotesapp.ui.notesEdit.EditNotesActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnNotesListener {
    private static final int ADD_NOTE_REQUEST_CODE = 1;
    private static final int UPDATE_NOTE_REQUEST_CODE = 2;
    private int selectedNotePosition = -1;
    private List<Note> notesList;
    private NotesAdapter notesAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.addNoteBtn.setOnClickListener(this);
        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList,this);
        binding.notesRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.notesRecyclerview.setAdapter(notesAdapter);
        getNotes();
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(MainActivity.this, EditNotesActivity.class), ADD_NOTE_REQUEST_CODE);
    }

    private void getNotes() {
        @SuppressLint("StaticFieldLeak")
        class GetAllNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getInstance(getApplicationContext()).NotesDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (notesList.isEmpty()) {
                    notesList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else {
                    notesList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                }
                binding.notesRecyclerview.smoothScrollToPosition(0);
                Log.d("Notes Info", notes.toString());
            }
        }
        new GetAllNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK){
            getNotes();
        }
    }

    @Override
    public void OnNoteClickListener(Note note, int position) {
        Intent intent = new Intent(getApplicationContext(), EditNotesActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, UPDATE_NOTE_REQUEST_CODE);
    }
}