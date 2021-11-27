package com.example.mymvvmnoteapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymvvmnoteapp.databinding.ActivityAddNoteBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE =
            "com.example.mymvvmnoteapp.EXTRA_TITLE";

    public static final String EXTRA_DESCRIPTION =
            "com.example.mymvvmnoteapp.EXTRA_DESCRIPTION";

    public static final String EXTRA_PRIORITY =
            "com.example.mymvvmnoteapp.EXTRA_PRIORITY";

    private ActivityAddNoteBinding addNoteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNoteBinding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(addNoteBinding.getRoot());

        //set number picker min and max value
        addNoteBinding.numberPickerPriority.setMinValue(1);
        addNoteBinding.numberPickerPriority.setMaxValue(10);

        //Support Action Toolbar, add close icon
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");
    }

    //function to save note
    private void saveNote() {
        String title = addNoteBinding.editTextTitle.getText().toString().trim();
        String description = addNoteBinding.editTextDescription.getText().toString().trim();
        int priority = addNoteBinding.numberPickerPriority.getValue();

        if (title.isEmpty() || description.isEmpty()) {
            addNoteBinding.textInputLayoutTitle.setError("This field must be filled!");
            addNoteBinding.textInputLayoutDescription.setError("This field must be filled!");
        }  else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_PRIORITY, priority);
            overridePendingTransition(0,0);

            setResult(RESULT_OK, data);
            finish();

//            Snackbar.make(findViewById(R.id.constraint_layout_add_note),
//                    "All field valid!",
//                    Snackbar.LENGTH_SHORT)
//                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}