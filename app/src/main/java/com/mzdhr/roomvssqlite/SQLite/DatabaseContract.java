package com.mzdhr.roomvssqlite.SQLite;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    // Private Constructor
    private DatabaseContract(){}

    // Content Provider
    public static final String CONTENT_AUTHORITY = "com.mzdhr.roomvssqlite";

    // Base URI's
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible Paths
    public static final String PATH_NOTES = "notes";


    public static final class NoteEntry implements BaseColumns{

        // Note Table Uri
        public static final Uri CONTENT_URI_NOTE = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);

        // Note Table
        public static final String NOTE_TABLE_NAME = "note_table_name";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_BODY = "note_body";

    }
}
