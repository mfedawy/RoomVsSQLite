package com.mzdhr.roomvssqlite.Room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "note_table_name")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int mId;
    private String mTitle;
    private String mBody;

    // Room Constructor
    public NoteEntity(@NonNull int id, String title, String body) {
        mId = id;
        mTitle = title;
        mBody = body;
    }

    // Our Constructor
    @Ignore
    public NoteEntity(String title, String body) {
        mTitle = title;
        mBody = body;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
