package com.example.mymvvmnoteapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mymvvmnoteapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
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

        //Delete note while swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewmodel.deleteNote(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Snackbar.make(findViewById(R.id.constraintLayoutMain),
                        "Note deleted!",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }



        }).attachToRecyclerView(mainBinding.recyclerView);

        noteAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, AddAndEditNoteActivity.class);
            intent.putExtra(AddAndEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddAndEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddAndEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddAndEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
            mStartForResultUpdate.launch(intent);
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewmodel.deleteAllNotes();
                Snackbar.make(findViewById(R.id.constraintLayoutMain),
                        "All notes success deleted!",
                        Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void fabClick() {
        mainBinding.buttonAddNote.setOnClickListener(view -> {
            mStartForResult.launch(new Intent(this, AddAndEditNoteActivity.class));
            overridePendingTransition(0,0);
        });
    }



    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    String title = result.getData().getStringExtra(AddAndEditNoteActivity.EXTRA_TITLE);
                    String description = result.getData().getStringExtra(AddAndEditNoteActivity.EXTRA_DESCRIPTION);
                    int priority = result.getData().getIntExtra(AddAndEditNoteActivity.EXTRA_PRIORITY, 1);

                    Note note = new Note(title, description, priority);
                    //Insert note to Room database
                    noteViewmodel.insertNote(note);

                    Snackbar.make(findViewById(R.id.constraintLayoutMain),
                            "Success added note.",
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

    ActivityResultLauncher<Intent> mStartForResultUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    int id = result.getData().getIntExtra(AddAndEditNoteActivity.EXTRA_ID, -1);
                    assert result.getData() != null;
                    String title = result.getData().getStringExtra(AddAndEditNoteActivity.EXTRA_TITLE);
                    String description = result.getData().getStringExtra(AddAndEditNoteActivity.EXTRA_DESCRIPTION);
                    int priority = result.getData().getIntExtra(AddAndEditNoteActivity.EXTRA_PRIORITY, 1);

                    Note note = new Note(title, description, priority);
                    note.setId(id);
                    //Update note to Room database
                    noteViewmodel.updateNote(note);

                    Snackbar.make(findViewById(R.id.constraintLayoutMain),
                            "Success updated note.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    overridePendingTransition(0,0);
                    Snackbar.make(findViewById(R.id.constraintLayoutMain),
                            "Note not updated!",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
}