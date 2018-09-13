package com.mzdhr.roomvssqlite;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mzdhr.roomvssqlite.SQLite.DatabaseContract;
import com.mzdhr.roomvssqlite.SQLite.Note;

public class Main2Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = Main2Activity.class.getSimpleName();
    private static final int ALL_NOTE_LOADER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // Preparing our note objects
        Note note1 = new Note("Note Title", "Note Body");
        Note note2 = new Note("Note Title", "Note Body");

        // Inserting:
         insertNote(note1);
         insertNote(note2);

        // Updating:
         Note updateNote = new Note("User", "This is user one");
         updateNote(updateNote, 1);

        // Deleting:
         deleteNote(2);


        getLoaderManager().initLoader(ALL_NOTE_LOADER, null, this).forceLoad();
    }


    private void deleteNote(int noteId) {
        Uri deleteUri = ContentUris.withAppendedId(DatabaseContract.NoteEntry.CONTENT_URI_NOTE, noteId);
        int rowsDeleted = getContentResolver().delete(deleteUri, null, null);
        if (rowsDeleted == 0 ) {
            Log.d(TAG, "deleteNote: Delete failed");
        } else {
            Log.d(TAG, "deleteNote: delete successful");
        }
    }

    private void insertNote(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.NoteEntry.COLUMN_NOTE_TITLE, note.getTitle());
        contentValues.put(DatabaseContract.NoteEntry.COLUMN_NOTE_BODY, note.getBody());

        Uri insertUri = getContentResolver().insert(DatabaseContract.NoteEntry.CONTENT_URI_NOTE, contentValues);

        if (insertUri == null) {
            Log.d(TAG, "insertNote: Insert failed!");
        } else {
            Log.d(TAG, "insertNote: Insert successful");
            int insertedNoteId = Integer.valueOf(insertUri.getLastPathSegment());
            Log.d(TAG, "insertNote: Insert ID: " + insertedNoteId);
        }

    }

    private void updateNote(Note note, int noteId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.NoteEntry.COLUMN_NOTE_TITLE, note.getTitle());
        contentValues.put(DatabaseContract.NoteEntry.COLUMN_NOTE_BODY, note.getBody());

        Uri updateUri = ContentUris.withAppendedId(DatabaseContract.NoteEntry.CONTENT_URI_NOTE, noteId);

        int rowsUpdated = getContentResolver().update(updateUri, contentValues, null, null);
        if (rowsUpdated == 0) {
            Log.d(TAG, "updateNote: update failed");
        } else {
            Log.d(TAG, "updateNote: update successful");
        }
    }

    /**
     * Loader Section
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                DatabaseContract.NoteEntry.COLUMN_NOTE_TITLE,
                DatabaseContract.NoteEntry.COLUMN_NOTE_BODY,
        };

        return new CursorLoader(this,
                DatabaseContract.NoteEntry.CONTENT_URI_NOTE,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            Log.d(TAG, "onLoadFinished: Cursor is null or there is less than 1 row in the cursor");
            return;
        }

        for (int i = 0; i < cursor.getCount(); i++) {
            // Getting Indexes
            cursor.moveToNext();
            int noteTitleIndex = cursor.getColumnIndex(DatabaseContract.NoteEntry.COLUMN_NOTE_TITLE);
            int noteBodyIndex = cursor.getColumnIndex(DatabaseContract.NoteEntry.COLUMN_NOTE_BODY);

            // Getting Values
            String noteTitle = cursor.getString(noteTitleIndex);
            String noteBody = cursor.getString(noteBodyIndex);

            Log.d(TAG, "Title: " + noteTitle + " Body: " + noteBody);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
