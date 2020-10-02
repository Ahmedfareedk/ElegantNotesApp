package com.example.modernnotesapp.ui.notesEdit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.modernnotesapp.R;
import com.example.modernnotesapp.database.Note;
import com.example.modernnotesapp.database.NotesDatabase;
import com.example.modernnotesapp.databinding.ActivityEditNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditNotesActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityEditNotesBinding editNotesBinding;
    private String selectedNoteColor;
    private View noteSubtitleIndicator;
    private ImageView viewColor1, viewColor2, viewColor3, viewColor4, viewColor5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editNotesBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_notes);
        editNotesBinding.backBtn.setOnClickListener(this);
        editNotesBinding.textDateTime.setText(new SimpleDateFormat("EEEE, MMMM, dd-yyyy HH:mm a", Locale.getDefault()).format(new Date()));
        editNotesBinding.doneBtn.setOnClickListener(this);

        //default note color
        selectedNoteColor = "#333333";

        initMiscellaneous();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.done_btn:
                saveNoteToDatabase();
                break;

        }

    }

    private void saveNoteToDatabase() {
        if (TextUtils.isEmpty(editNotesBinding.inputNoteTitle.getText())) {
            Toast.makeText(this, "Note Title Cannot be Empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(editNotesBinding.noteSubtitle.getText()) || TextUtils.isEmpty(editNotesBinding.inputNoteContent.getText())) {
            Toast.makeText(this, "Note Content Cannot be Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = editNotesBinding.inputNoteTitle.getText().toString().trim();
        String subtitle = editNotesBinding.noteSubtitle.getText().toString().trim();
        String noteContent = editNotesBinding.inputNoteContent.getText().toString().trim();
        String noteDateTime = editNotesBinding.textDateTime.getText().toString().trim();

        @SuppressLint("StaticFieldLeak")
        class InsertNote extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getInstance(getApplicationContext()).NotesDao().addNote(new Note(title, subtitle,selectedNoteColor, noteDateTime, noteContent));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                super.onPostExecute(aVoid);
            }
        }

        new InsertNote().execute();
    }

    private void initMiscellaneous() {
        LinearLayout layoutMiscellaneous = findViewById(R.id.miscellaneous_layout);
        BottomSheetBehavior<LinearLayout> bottomSheet = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.miscellaneous_text).setOnClickListener(v -> {
            if (bottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        viewColor1 = layoutMiscellaneous.findViewById(R.id.note_1_image_done);
        viewColor2 = layoutMiscellaneous.findViewById(R.id.note_2_image_done);
        viewColor3 = layoutMiscellaneous.findViewById(R.id.note_3_image_done);
        viewColor4 = layoutMiscellaneous.findViewById(R.id.note_4_image_done);
        viewColor5 = layoutMiscellaneous.findViewById(R.id.note_5_image_done);

       setViewImageColorBackground(layoutMiscellaneous.findViewById(R.id.note_1_view), 1);
       setViewImageColorBackground(layoutMiscellaneous.findViewById(R.id.note_2_view), 2);
       setViewImageColorBackground(layoutMiscellaneous.findViewById(R.id.note_3_view), 3);
       setViewImageColorBackground(layoutMiscellaneous.findViewById(R.id.note_4_view), 4);
       setViewImageColorBackground(layoutMiscellaneous.findViewById(R.id.note_5_view), 5);
    }

    private void setViewImageColorBackground(View view, int position){
        view.setOnClickListener(v -> setDoneImage(position));
    }

    private void setDoneImage(int position) {
        switch (position) {
            case 1:
                viewColor1.setImageResource(R.drawable.ic_done);
                viewColor2.setImageResource(0);
                viewColor3.setImageResource(0);
                viewColor4.setImageResource(0);
                viewColor5.setImageResource(0);
                selectedNoteColor = "#333333";
                break;
            case 2:
                viewColor1.setImageResource(0);
                viewColor2.setImageResource(R.drawable.ic_done);
                viewColor3.setImageResource(0);
                viewColor4.setImageResource(0);
                viewColor5.setImageResource(0);
                selectedNoteColor = "#FDBE3B";
                break;
            case 3:
                viewColor1.setImageResource(0);
                viewColor2.setImageResource(0);
                viewColor3.setImageResource(R.drawable.ic_done);
                viewColor4.setImageResource(0);
                viewColor5.setImageResource(0);
                selectedNoteColor = "#FF4842";
                break;
            case 4:
                viewColor1.setImageResource(0);
                viewColor2.setImageResource(0);
                viewColor3.setImageResource(0);
                viewColor4.setImageResource(R.drawable.ic_done);
                viewColor5.setImageResource(0);
                selectedNoteColor = "#3A52FC";
                break;
            case 5:
                viewColor1.setImageResource(0);
                viewColor2.setImageResource(0);
                viewColor3.setImageResource(0);
                viewColor4.setImageResource(0);
                viewColor5.setImageResource(R.drawable.ic_done);
                selectedNoteColor = "#004D40";
                break;

        }
        setNoteSubtitleIndicatorColor();
    }



    private void setNoteSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) editNotesBinding.noteSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
}