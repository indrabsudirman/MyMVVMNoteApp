package com.example.mymvvmnoteapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mymvvmnoteapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    private NoteViewmodel noteViewmodel;
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // Floating Action Button Click
        fabClick();

        //Set RecyclerView
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.recyclerView.setHasFixedSize(true);
        final NoteAdapter noteAdapter = new NoteAdapter();
        mainBinding.recyclerView.setAdapter(noteAdapter);

        noteViewmodel = new ViewModelProvider(this).get(NoteViewmodel.class);
        noteViewmodel.getAllNotes().observe(this, noteAdapter::setNotes); //Lambda style
    }

    private void fabClick() {
        mainBinding.buttonAddNote.setOnClickListener(view -> {
            mStartForResult.launch(new Intent(this, AddNoteActivity.class));
            overridePendingTransition(0,0);
        });
    }



    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    String title = result.getData().getStringExtra(AddNoteActivity.EXTRA_TITLE);
                    String description = result.getData().getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
                    int priority = result.getData().getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

                    Note note = new Note(title, description, priority);
                    //Insert note to Room database
                    noteViewmodel.insertNote(note);

                    Snackbar.make(findViewById(R.id.constraintLayoutMain),
                            "Request code valid are.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    overridePendingTransition(0,0);
                    Snackbar.make(findViewById(R.id.constraintLayoutMain),
                            "Note not saved!",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
}