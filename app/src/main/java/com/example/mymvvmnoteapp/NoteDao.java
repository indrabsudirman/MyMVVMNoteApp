package com.example.mymvvmnoteapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Cocok dengan step di Documentation Google, setelah buat entity buat DAO interface
 * https://developer.android.com/training/data-storage/room/accessing-data
 * di link di atas lengkap, ada penjelasan soal LiveData juga
 */

@Dao
public interface NoteDao {

    @Insert
    void insertNote (Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority ASC")
    LiveData<List<Note>> getAllNotes();
}
