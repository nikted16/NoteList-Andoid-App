package com.example.nikhil.notelist;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener

{
    public static final int EDITOR_REQUEST_CODE = 100;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        insertNote("New Note");

        cursorAdapter = new NoteCursorAdapter(this, null, 0);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                        Uri uri = Uri.parse(NotesHandler.CONTENT_URI + "/" + l);
                        intent.putExtra(NotesHandler.Content_Item_Type, uri);
                        startActivityForResult(intent, EDITOR_REQUEST_CODE);
                    }
                }
        );

        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // final FloatingActionButton fab1 =(FloatingActionButton) findViewById(R.id.fab1);
        // final FloatingActionButton fab2 =(FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditorForNewNote();
               /* fab1.show();
                fab2.show();
                int id = view.getId();
                switch (id){
                    case R.id.fab2:
                        fab2.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openEditorForNewNote();
                                    }
                                }
                        );
                        break;
                    case R.id.fab1:
                        fab1.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(MainActivity.this,"Camera",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        break;
                }*/
            }
        });
    }

    private void insertNote(String noteText) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.Note_Text, noteText);
        Uri uri = getContentResolver().insert(NotesHandler.CONTENT_URI, contentValues);
        assert uri != null;
        Log.d("MainActivity", "Inserted Note " + uri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
       MenuItem searchItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));
        searchView.setIconifiedByDefault(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_sample:
                insertSampleNote();
                break;
            case R.id.action_delete_all:
                deleteAllNote();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNote() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(NotesHandler.CONTENT_URI, null, null);
                restartLoader();

                if (i == DialogInterface.BUTTON_POSITIVE) {
                    Toast.makeText(MainActivity.this, getString(R.string.all_delete), Toast.LENGTH_SHORT).show();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_u_sure)).setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener).show();
    }

    private void insertSampleNote() {
        insertNote("Welcome Note");
        insertNote("Multi-line\nnote");
        insertNote("This note is going to be too long ,it exceeds Screen width");

        restartLoader();
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new android.content.CursorLoader(this, NotesHandler.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void openEditorForNewNote() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
