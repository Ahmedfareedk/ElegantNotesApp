package com.example.modernnotesapp.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static NotesDatabase notesDatabaseInstance;
    public abstract NotesDao NotesDao();

    public static synchronized NotesDatabase getInstance(Context context){

        if(notesDatabaseInstance == null){
            notesDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "notes_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return notesDatabaseInstance;
    }

}
