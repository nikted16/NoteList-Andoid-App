package com.example.nikhil.notelist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.Intent.ACTION_EDIT;

public class AddNoteActivity extends AppCompatActivity  {

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editor =(EditText)findViewById(R.id.editText3);
        img = (ImageView) findViewById(R.id.image_view);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesHandler.Content_Item_Type);
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_note));
        }
        else{
            action = Intent.ACTION_INSERT;
            noteFilter = DatabaseHandler.Note_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,DatabaseHandler.ALL_COLOUMNS,noteFilter,null,null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DatabaseHandler.Note_Text));
            editor.setText(oldText);
            editor.requestFocus();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            switch (id){
                case R.id.home:
                    finishEditing();
                    break;
                case R.id.action_delete:
                    deleteNote();
                    break;
                case R.id.take_pic:
                    camera();
            }
         return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         Bitmap bp =(Bitmap) data.getExtras().get("data");
          img.setImageBitmap(bp);
    }

    private void camera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }

    private void deleteNote() {
        getContentResolver().delete(NotesHandler.CONTENT_URI,noteFilter,null);
        Toast.makeText(this, R.string.note_deleted,Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void finishEditing(){
        String newText = editor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if(newText.length() == 0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    insertNote(newText);
                }
                break;
            case ACTION_EDIT:
                if(newText.length()==0){
                    deleteNote();
                }
                else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }
                else{
                    updateNote(newText);
                }
        }
        finish();
    }

    private void updateNote(String noteText) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.Note_Text,noteText);
        getContentResolver().update(NotesHandler.CONTENT_URI,contentValues,noteFilter,null);
        Toast.makeText(this, getString(R.string.note_updated),Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.Note_Text,noteText);
        getContentResolver().insert(NotesHandler.CONTENT_URI,contentValues);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
