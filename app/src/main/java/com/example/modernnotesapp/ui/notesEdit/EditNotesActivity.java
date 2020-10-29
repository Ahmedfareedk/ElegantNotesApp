package com.example.modernnotesapp.ui.notesEdit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.modernnotesapp.R;
import com.example.modernnotesapp.database.Note;
import com.example.modernnotesapp.database.NotesDatabase;
import com.example.modernnotesapp.databinding.ActivityEditNotesBinding;
import com.example.modernnotesapp.databinding.AddUrlCustomBinding;
import com.example.modernnotesapp.databinding.LayoutMiscellaneousBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditNotesActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityEditNotesBinding editNotesBinding;
    private LayoutMiscellaneousBinding miscellaneousBinding;
    private AddUrlCustomBinding customAlertDialogBinding;
    private AlertDialog addUrlAlertDialog;

    private String selectedNoteColor;
    private ImageView viewColor1, viewColor2, viewColor3, viewColor4, viewColor5;
    private ImageView[] colorsImagesArr;
    private View[] noteViewColors;
    String[] colorsArray;
    private String selectedImageUri;
    private final int EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public static final int SELECT_IMAGE_REQUEST_CODE = 2;

    private Note alreadyExistedNote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editNotesBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_notes);
        miscellaneousBinding = DataBindingUtil.bind(editNotesBinding.miscellaneousLayout.getRoot());
        //customAlertDialogBinding = DataBindingUtil.bind(getLayoutInflater().inflate(R.layout.add_url_custom, null));


        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customAlertDialogBinding = AddUrlCustomBinding.inflate(inflater);


        editNotesBinding.backBtn.setOnClickListener(this);
        editNotesBinding.textDateTime.setText(new SimpleDateFormat("EEEE, MMMM, dd-yyyy HH:mm a", Locale.getDefault()).format(new Date()));
        editNotesBinding.doneBtn.setOnClickListener(this);

        editNotesBinding.removeNoteImageButton.setOnClickListener(this);
        editNotesBinding.noteUrlRemove.setOnClickListener(this);


        noteViewColors = new View[]{miscellaneousBinding.note1View, miscellaneousBinding.note2View,
                miscellaneousBinding.note3View, miscellaneousBinding.note4View,miscellaneousBinding.note5View};
        colorsArray  = new String[]{"#333333", "#FDBE3B", "#FF4842", "#3A52FC", "#004D40"};
        //default note color
        selectedNoteColor = "#333333";
        selectedImageUri = " ";


        if(getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyExistedNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        initMiscellaneous();
    }

    private void setViewOrUpdateNote() {
       editNotesBinding.inputNoteTitle.setText(alreadyExistedNote.getNoteTitle());
       editNotesBinding.noteSubtitle.setText(alreadyExistedNote.getNoteSubtitle());
       editNotesBinding.inputNoteContent.setText(alreadyExistedNote.getNoteContent());

       if(alreadyExistedNote.getNoteImagePath() != null && !alreadyExistedNote.getNoteImagePath().trim().isEmpty()){
           editNotesBinding.noteImageView.setImageBitmap(BitmapFactory.decodeFile(alreadyExistedNote.getNoteImagePath()));
           editNotesBinding.noteImageView.setVisibility(View.VISIBLE);
           selectedImageUri = alreadyExistedNote.getNoteImagePath();
           editNotesBinding.removeNoteImageButton.setVisibility(View.VISIBLE);
       }

       if(alreadyExistedNote.getNoteWebLink() != null && !alreadyExistedNote.getNoteWebLink().trim().isEmpty()){
           editNotesBinding.noteUrlTv.setText(alreadyExistedNote.getNoteWebLink());
           editNotesBinding.noteUrlLayout.setVisibility(View.VISIBLE);
       }
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
            case R.id.note_url_remove:
                removeUrl();
                break;
            case R.id.remove_note_image_button:
                removeNoteImage();
                break;
        }

    }

    private void removeNoteImage() {
        editNotesBinding.noteImageView.setImageBitmap(null);
        editNotesBinding.noteImageView.setVisibility(View.GONE);
        selectedImageUri ="";
        editNotesBinding.removeNoteImageButton.setVisibility(View.GONE);

    }

    private void removeUrl() {
        editNotesBinding.noteUrlTv.setText("");
        editNotesBinding.noteUrlLayout.setVisibility(View.GONE);
    }

    private void saveNoteToDatabase() {

        Note note = new Note();
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

        note.setNoteTitle(title)
                .setNoteSubtitle(subtitle)
                .setNoteContent(noteContent)
                .setNoteDateTime(noteDateTime)
                .setNoteColor(selectedNoteColor)
                .setNoteImagePath(selectedImageUri);
                //.create();

        if (editNotesBinding.noteUrlLayout.getVisibility() == View.VISIBLE) {
            note.setNoteWebLink(editNotesBinding.noteUrlTv.getText().toString());
        }
        if(alreadyExistedNote != null){
            note.setId(alreadyExistedNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class InsertNote extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getInstance(getApplicationContext()).NotesDao().addNote(note);/*new Note(title, subtitle, selectedNoteColor, noteDateTime, noteContent, selectedImageUri)*/
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
        BottomSheetBehavior<LinearLayout> bottomSheet = BottomSheetBehavior.from(miscellaneousBinding.layoutMiscellaneous);
        miscellaneousBinding.miscellaneousText.setOnClickListener(v -> {
            if (bottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        viewColor1 = miscellaneousBinding.note1ImageDone;
        viewColor2 = miscellaneousBinding.note2ImageDone;
        viewColor3 = miscellaneousBinding.note3ImageDone;
        viewColor4 = miscellaneousBinding.note4ImageDone;
        viewColor5 = miscellaneousBinding.note5ImageDone;

        setViewImageColorBackground(miscellaneousBinding.note1View, 0);
        setViewImageColorBackground(miscellaneousBinding.note2View, 1);
        setViewImageColorBackground(miscellaneousBinding.note3View, 2);
        setViewImageColorBackground(miscellaneousBinding.note4View, 3);
        setViewImageColorBackground(miscellaneousBinding.note5View, 4);
        setImageColorMarked(noteViewColors);

        miscellaneousBinding.addNoteImageRoot.setOnClickListener(v -> {
            bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            requestPermission();
        });

        miscellaneousBinding.addNoteUrlRoot.setOnClickListener(v -> {
            bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            addUrlAlertDialogBuilder();
        });

    }

    private void setViewImageColorBackground(View view, int position) {
        view.setOnClickListener(v -> setDoneBackground(position));
    }

    private void setDoneBackground(int position) {
        colorsImagesArr = new ImageView[]{viewColor1, viewColor2, viewColor3, viewColor4, viewColor5};
        for (int i = 0; i < colorsImagesArr.length; i++) {
            if (i == position) {
                colorsImagesArr[i].setImageResource(R.drawable.ic_done);
                setNoteSubtitleIndicatorColor(position);
            } else {
                colorsImagesArr[i].setImageResource(0);
            }
        }


    }

    private void setNoteSubtitleIndicatorColor(int position) {

        selectedNoteColor = colorsArray[position];
        GradientDrawable gradientDrawable = (GradientDrawable) editNotesBinding.noteSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));

    }

    private void setImageColorMarked(View[] views){

        if(alreadyExistedNote != null) {
            if (alreadyExistedNote.getNoteColor() != null && !alreadyExistedNote.getNoteColor().trim().isEmpty()) {
                for (int i = 0; i < views.length; i++) {
                    if (alreadyExistedNote.getNoteColor().equals(colorsArray[i])) {
                        views[i].performClick();
                        break;
                    }
                }
            }
        }
    }

    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditNotesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                        editNotesBinding.noteImageView.setImageBitmap(selectedImageBitmap);
                        editNotesBinding.noteImageView.setVisibility(View.VISIBLE);
                        selectedImageUri = getPathFromUri(uri);
                        editNotesBinding.removeNoteImageButton.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            // int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(cursor.getColumnIndex("_data"));
            cursor.close();
        }
        return filePath;
    }

    private void addUrlAlertDialogBuilder() {

        View customView = getLayoutInflater().inflate(R.layout.add_url_custom, null);
        final EditText noteUrlEditText = customView.findViewById(R.id.add_url_edit_text);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditNotesActivity.this);


        dialogBuilder.setView(customView);
        addUrlAlertDialog = dialogBuilder.create();

        if (addUrlAlertDialog.getWindow() != null) {
            addUrlAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        noteUrlEditText.requestFocus();
        addUrlAlertDialog.show();

        customView.findViewById(R.id.add_done_text_view).setOnClickListener(v -> {

            if (TextUtils.isEmpty(noteUrlEditText.getText().toString())) {
                Toast.makeText(this, "Enter URL!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.WEB_URL.matcher(noteUrlEditText.getText().toString()).matches()) {
                Toast.makeText(this, "Enter Valid URL!", Toast.LENGTH_SHORT).show();
            } else {
                editNotesBinding.noteUrlTv.setText(noteUrlEditText.getText().toString().trim());
                editNotesBinding.noteUrlLayout.setVisibility(View.VISIBLE);
                addUrlAlertDialog.dismiss();
            }
        });
        customView.findViewById(R.id.add_cancel_text_view).setOnClickListener(v -> {
            addUrlAlertDialog.dismiss();
        });
    }
}