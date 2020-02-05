package com.example.notes.UI;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Ignore;

import com.example.notes.Model.Note;
import com.example.notes.Data.NoteDao;
import com.example.notes.Data.NoteDatabase;
import com.example.notes.Data.Repositry;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    Repositry repo ;
    LiveData<List<Note>> allNotes = new MutableLiveData<>();
    NoteDatabase noteDatabase;


      public NoteViewModel(Application context) {
        super(context);
        repo = new Repositry(context);
        allNotes=repo.getAllNotes();

    }


    public void insert(Note note){
        repo.insert(note);
    }

    public void update(Note note){
       repo.update(note);
    }

    public void delete(Note note){
      repo.delete(note);
    }

    public void deleteAll(){
        repo.deleteAll();
    }

    public LiveData<List<Note>> getAllNotes (Context context){

        return allNotes;
    }


}

