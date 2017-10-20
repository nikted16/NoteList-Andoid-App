package com.example.nikhil.notelist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by nikhil on 8/7/2016.
 */
public class NotesHandler extends ContentProvider {
    private static final String AUTHORITY ="com.example.nikhil.notelist.noteshandler";
    private static final String BASE_PATH ="notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String Content_Item_Type ="Note";

    static{
        uriMatcher.addURI(AUTHORITY,BASE_PATH,NOTE);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + " /#",NOTE_ID);
    }

    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        DatabaseHandler dh = new DatabaseHandler(getContext());
        db = dh.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        if (uriMatcher.match(uri) == NOTE_ID) {
            s = DatabaseHandler.Note_ID + "=" + uri.getLastPathSegment();
        }


            return db.query(DatabaseHandler.Table_note, DatabaseHandler.ALL_COLOUMNS, s, null, null, null, DatabaseHandler.Note_Created + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = db.insert(DatabaseHandler.Table_note,null,contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return db.delete(DatabaseHandler.Table_note,s,strings);

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return db.update(DatabaseHandler.Table_note,contentValues,s,strings);
    }
}
