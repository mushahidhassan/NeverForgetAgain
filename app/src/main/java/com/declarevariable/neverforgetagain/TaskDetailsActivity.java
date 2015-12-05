package com.declarevariable.neverforgetagain;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TaskDetailsActivity extends AppCompatActivity {

    private String action;
    private EditText et_details;
    private String taskFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_details = (EditText) findViewById(R.id.details);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                performSaveAcitons();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(TasksProvider.CONTENT_ITEM_TYPE);
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Note");
        }
        else{
            action = Intent.ACTION_EDIT;
            taskFilter = DbHelper.TASK_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri, DbHelper.ALL_COLUMNS, taskFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DbHelper.TASK_TEXT));
            setTitle(oldText);
            et_details.setText(oldText);
            et_details.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_task_details, menu);
            //delleteTask();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                performSaveAcitons();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }
        return true;
    }

    private void deleteNote() {
        getContentResolver().delete(TasksProvider.CONTENT_URI, taskFilter,null);
        Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    private  void performSaveAcitons(){
        String taskContent = et_details.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if(taskContent.length() == 0 ){
                    setResult(RESULT_CANCELED);
                }else{
                    insertTask(taskContent);
                }
                break;
            case  Intent.ACTION_EDIT:
                if (taskContent.length()==0){
                    deleteNote();
                }else if(oldText.equals(taskContent)){
                    setResult(RESULT_CANCELED);
                }else {
                    upddateNote(taskContent);
                }
                break;
        }
        finish();
    }

    private void upddateNote( String taskContent ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TASK_TEXT, taskContent);
        getContentResolver().update(TasksProvider.CONTENT_URI, contentValues, taskFilter, null);
        Toast.makeText(getApplicationContext(), "Task updated", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
    }

    private void insertTask( String taskContent ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TASK_TEXT, taskContent);
        Uri taskUri = getContentResolver().insert(TasksProvider.CONTENT_URI, contentValues);
        Log.d("TaskDetailsActivity", "new task inserted " + taskUri.getLastPathSegment());
        setResult(RESULT_OK);
    }
}
