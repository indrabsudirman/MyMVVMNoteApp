package com.example.mymvvmnoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewmodel noteViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteViewmodel = new ViewModelProvider(this).get(NoteViewmodel.class);
        noteViewmodel.getAllNotes().observe(this, notes -> {
            //Update RecyclerView
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.constraintLayoutMain), "Note View Model", Snackbar.LENGTH_LONG);
            snackbar.show();
        });
    }
}