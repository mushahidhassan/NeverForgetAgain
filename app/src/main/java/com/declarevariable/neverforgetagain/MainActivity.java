package com.declarevariable.neverforgetagain;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_CODE_TASK_DETAILS_ACTIVITY = 1001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        displayAds();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                // insertSampleTask();
                Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TASK_DETAILS_ACTIVITY);
            }
        });


        Cursor cursor = getContentResolver().query(TasksProvider.CONTENT_URI, DbHelper.ALL_COLUMNS, null, null, null, null);
        //String[] from = {DbHelper.TASK_TEXT};
        //int[] to = {R.id.tv_text};//{android.R.id.text1};
        //cursorAdapter = new SimpleCursorAdapter(this, R.layout.task_list_item/*android.R.layout.simple_list_item_1*/, cursor, from, to, 0);
        cursorAdapter = new TaskCursorAdapter(this, null, 0);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                Uri uri = Uri.parse(TasksProvider.CONTENT_URI + "/" + id);
                intent.putExtra(TasksProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, REQUEST_CODE_TASK_DETAILS_ACTIVITY);
            }
        });
        restartLoader();
    }


    private void insetTask( String taskContent ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TASK_TEXT, taskContent);
        Uri taskUri = getContentResolver().insert(TasksProvider.CONTENT_URI, contentValues);
        Log.d("MainActivity", "new task inserted " + taskUri.getLastPathSegment());
    }
    private void insertSampleTask(){
        insetTask("Dummy Task");
        insetTask(getString(R.string.lorem_ipsum));
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void deleteAllNotes(){
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            getContentResolver().delete(TasksProvider.CONTENT_URI, null, null);//will delete all
                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.sure_delete))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
         if (id == R.id.action_delete_all){
            deleteAllNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TasksProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TASK_DETAILS_ACTIVITY && resultCode == RESULT_OK){
            restartLoader();
        }
    }


    /*
    public void displayAds() {

        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("352136067380143")  // My Galaxy Nexus test phone
                .build();
        mAdView.loadAd(request);
    }*/
    public void displayAds()
    {
        try {
            AdView mAdView = (AdView) findViewById(R.id.adView);
            // mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdListener(new AdListener() {

                @Override
                public void onAdClosed() {
                    // TODO Auto-generated method stub
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // TODO Auto-generated method stub
                    super.onAdFailedToLoad(errorCode);
                    switch (errorCode) {
                        case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                            break;
                        case AdRequest.ERROR_CODE_INVALID_REQUEST:
                            break;
                        case AdRequest.ERROR_CODE_NETWORK_ERROR:
                            break;
                        case AdRequest.ERROR_CODE_NO_FILL:
                            break;
                    }
                }

                @Override
                public void onAdLeftApplication() {
                    // TODO Auto-generated method stub
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdLoaded() {
                    // TODO Auto-generated method stub
                    super.onAdLoaded();
                }

                @Override
                public void onAdOpened() {
                    // TODO Auto-generated method stub
                    super.onAdOpened();
                }

            });
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("352136067380143").build();
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            Log.e("displayAd:MainActivity", e.getLocalizedMessage());
        }
    }


}
