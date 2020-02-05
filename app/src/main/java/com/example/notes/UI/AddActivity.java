package com.example.notes.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.notes.Model.Note;
import com.example.notes.R;

public class AddActivity extends AppCompatActivity {

    EditText Title_editText, Desc_editText;
    NumberPicker PriorityPicker;
    NoteViewModel noteViewModel;
    String Title, Desc;
    int Priority, NoteID;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        initView();

    }

    private void initView() {

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        Title_editText = findViewById(R.id.txt_title);
        Desc_editText = findViewById(R.id.txt_desc);
        PriorityPicker = findViewById(R.id.priority_Picker);
        PriorityPicker.setMaxValue(5);
        PriorityPicker.setMinValue(1);

        intent = getIntent();
        NoteID = intent.getIntExtra("NoteID", 0);
        Title_editText.setText(intent.getStringExtra("title"));
        Desc_editText.setText(intent.getStringExtra("desc"));
        PriorityPicker.setValue(intent.getIntExtra("priority", 1));

        if (intent.hasExtra("NoteID")) { // change the title of activity when edit or add new note
            setTitle("Edit Note");
        } else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                if (intent.hasExtra("NoteID")) {
                    updateNote();
                } else {
                    saveNote();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateNote() {

        Title = Title_editText.getText().toString();
        Desc = Desc_editText.getText().toString();
        Priority = PriorityPicker.getValue();

        if (Title.trim().isEmpty())
            Title_editText.setError("Insert Title");
        else if (Desc.trim().isEmpty())
            Desc_editText.setError("Insert Description");
        else {
            Note note = new Note();
            note.setTitle(Title);
            note.setDescription(Desc);
            note.setPriority(Priority);
            note.setId(NoteID);
            noteViewModel.update(note);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void saveNote() {

        Title = Title_editText.getText().toString();
        Desc = Desc_editText.getText().toString();
        Priority = PriorityPicker.getValue();

        if (Title.trim().isEmpty())
            Title_editText.setError("Insert Title");
        else if (Desc.trim().isEmpty())
            Desc_editText.setError("Insert Description");
        else {
            Note note = new Note();
            note.setTitle(Title);
            note.setDescription(Desc);
            note.setPriority(Priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}