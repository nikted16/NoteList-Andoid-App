package com.example.nikhil.notelist;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nikhil on 8/9/2016.
 */
public class NoteCursorAdapter extends CursorAdapter {
    public NoteCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
          String noteText = cursor.getString(
                  cursor.getColumnIndex(DatabaseHandler.Note_Text));

        int pos = noteText.indexOf(10);
        if(pos != -1)
        {
            noteText = noteText.substring(0,pos) + "....";
        }

        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);

    }
}
