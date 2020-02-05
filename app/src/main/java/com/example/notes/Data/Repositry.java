package com.example.notes.Data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.notes.Model.Note;
import java.util.List;

public class Repositry {

    NoteDao noteDao ;
    LiveData<List<Note>> allNotes = new MutableLiveData<>();
    NoteDatabase noteDatabase;

    public Repositry() {
    }

    public Repositry(Context context) {

        noteDao = NoteDatabase.getInstance(context).noteDao();
        allNotes=noteDao.getAllNotes();

    }


    public void insert(Note note){
        noteDao.insert(note);
    }

    public void update(Note note){
        noteDao.update(note);
    }

    public void delete(Note note){
        noteDao.delete(note);
    }

    public void deleteAll(){
        noteDao.deleteAll();
    }



    public LiveData<List<Note>> getAllNotes (){

        return allNotes;
    }



}
