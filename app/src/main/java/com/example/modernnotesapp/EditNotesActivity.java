package com.example.modernnotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.modernnotesapp.databinding.ActivityEditNotesBinding;

public class EditNotesActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditNotesBinding editNotesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editNotesBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_notes);
        editNotesBinding.backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        onBackPressed();
    }
}