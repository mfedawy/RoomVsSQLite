package com.mzdhr.roomvssqlite.Room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private NoteDao mNoteDao;
    private LiveData<List<NoteEntity>> mAllNotes;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mNoteDao = appDatabase.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(final NoteEntity noteEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.insertNote(noteEntity);
            }
        });
    }

    public void update(final NoteEntity noteEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.updateNote(noteEntity);
            }
        });
    }

    public void delete(final NoteEntity noteEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteNote(noteEntity);

            }
        });
    }
}
