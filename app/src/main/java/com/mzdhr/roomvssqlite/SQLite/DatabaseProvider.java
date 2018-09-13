package com.mzdhr.roomvssqlite.SQLite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class DatabaseProvider extends ContentProvider {
    private static final String TAG = DatabaseProvider.class.getSimpleName();

    private static final int NOTES = 1100;
    private static final int NOTE_ID = 1101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private DatabaseHelper mDatabaseHelper;

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_NOTES, NOTES);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_NOTES + "/#", NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTES:
                cursor = database.query(DatabaseContract.NoteEntry.NOTE_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTE_ID:
                selection = DatabaseContract.NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DatabaseContract.NoteEntry.NOTE_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException(TAG + " Cannot query unknown Uri ---> " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTES:
                return insertNote(uri, contentValues);

            default:
                throw new IllegalArgumentException(TAG + " Insertion is not supported for " + uri);
        }
    }

    private Uri insertNote(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        long id = database.insert(DatabaseContract.NoteEntry.NOTE_TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(TAG, "insert: Failed to insert a row for " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTE_ID:
                selection = DatabaseContract.NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deleteNote(uri, selection, selectionArgs);

            default:
                throw new IllegalArgumentException(TAG + "delete: Deletion is not supported for " + uri);
        }
    }

    private int deleteNote(@NonNull Uri uri, @NonNull String selection, @NonNull String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        rowsDeleted = database.delete(DatabaseContract.NoteEntry.NOTE_TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTE_ID:
                selection = DatabaseContract.NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateNote(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated = database.update(DatabaseContract.NoteEntry.NOTE_TABLE_NAME, values, selection, selectionArgs);

        // Notify the UI
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
