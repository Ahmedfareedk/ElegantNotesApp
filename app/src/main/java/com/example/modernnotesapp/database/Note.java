package com.example.modernnotesapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String noteTitle;

    @ColumnInfo(name = "subtitle")
    private String noteSubtitle;

    @ColumnInfo(name = "note_web link")
    private String noteWebLink;

    @ColumnInfo(name = "note_color")
    private String noteColor;

    @ColumnInfo(name = "note_image")
    private String noteImage;

    @ColumnInfo(name = "note_date_time")
    private String noteDateTime;

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    @ColumnInfo(name = "note_content")
    private String noteContent;

    public Note(String noteTitle, String noteSubtitle, String noteColor, String noteDateTime, String noteContent) {
        this.noteTitle = noteTitle;
        this.noteSubtitle = noteSubtitle;
        this.noteColor = noteColor;
        this.noteDateTime = noteDateTime;
        this.noteContent = noteContent;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteSubtitle() {
        return noteSubtitle;
    }

    public void setNoteSubtitle(String noteSubtitle) {
        this.noteSubtitle = noteSubtitle;
    }

    public String getNoteWebLink() {
        return noteWebLink;
    }

    public void setNoteWebLink(String noteWebLink) {
        this.noteWebLink = noteWebLink;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

    public String getNoteDateTime() {
        return noteDateTime;
    }

    public void setNoteDateTime(String noteDateTime) {
        this.noteDateTime = noteDateTime;
    }

    @Override
    public String toString() {
        return noteTitle + " : " + noteSubtitle + noteContent + noteDateTime;
    }
}
