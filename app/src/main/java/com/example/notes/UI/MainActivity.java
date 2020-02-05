package com.example.notes.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.notes.Model.Note;
import com.example.notes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NoteViewModel noteViewModel ;
    RecyclerView noteRecycler;
    NoteAdapter noteAdapter;
    FloatingActionButton addNoteFloatingButton, speak;
    SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    Intent mSpeechRecognizerIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        swipeRecycler(); //swipe to delete item

    }

    private void initViews(){

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "eg-US");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);


        int orientation=getResources().getConfiguration().orientation;
        noteRecycler =findViewById(R.id.NoteReycler);
        noteAdapter =new NoteAdapter(this);

        if (orientation== Configuration.ORIENTATION_PORTRAIT){
            noteRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        }else {
            noteRecycler.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        }
//        noteRecycler.setLayoutManager(new LinearLayoutManager(this));
        noteRecycler.setAdapter(noteAdapter);
        addNoteFloatingButton =findViewById(R.id.AddNote);
        speak=findViewById(R.id.speak);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes(MainActivity.this).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setList(notes);
            }
        });

        addNoteFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddActivity.class);
                startActivity(i);
            }
        });

        speech();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_notes:
               noteViewModel.deleteAll();
                return true ;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void swipeRecycler(){

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( // delete item with swipe
                ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //get the postion of the selected item and pass to delete
                noteViewModel.delete(noteAdapter.getNotePostion(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(noteRecycler);

    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        } else {

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void speech(){

        speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();

                        break;

                    case MotionEvent.ACTION_DOWN:
                        checkPermission();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return false;
            }
        });


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Toast.makeText(MainActivity.this, "Speak Now", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBeginningOfSpeech() {
//                Toast.makeText(MainActivity.this, "Speak...", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
//                Toast.makeText(MainActivity.this, "Speech Received", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onEndOfSpeech() {
//                Toast.makeText(MainActivity.this, "End Of Speech", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(int i) {
//                Toast.makeText(MainActivity.this, "Error Speech", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {

//                    input.setText(matches.get(0));

                    Note note = new Note();
                    note.setTitle("Quick");
                    note.setDescription(matches.get(0));
                    note.setPriority(1);
                    noteViewModel.insert(note);
                    Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


    }


    }

