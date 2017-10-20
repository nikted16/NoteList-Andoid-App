package com.example.nikhil.notelist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by nikhil on 8/7/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DatabaseName ="notes.db";
    private static final int DatabseVersion=1;
    public static final String Table_note = "notes";
    public static final String Note_ID ="_id";
    public static final String Note_Text ="noteText";
    public static final String Note_Created="noteCreated";
    public static final String[] ALL_COLOUMNS ={Note_ID,Note_Text,Note_Created};

    private static final String Create_Table = "CREATE TABLE " + Table_note + " (" + Note_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Note_Text + " TEXT, " + Note_Created + " TEXT default CURRENT_TIMESTAMP" + ")" ;

    public DatabaseHandler(Context context) {
        super(context, DatabaseName, null, DatabseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + Create_Table);
        onCreate(sqLiteDatabase);
    }


}
