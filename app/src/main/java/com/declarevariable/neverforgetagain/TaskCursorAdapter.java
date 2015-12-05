package com.declarevariable.neverforgetagain;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by mushahid on 11/29/2015.
 */
public class TaskCursorAdapter extends CursorAdapter {

    public TaskCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String text = cursor.getString(cursor.getColumnIndex(DbHelper.TASK_TEXT));
        int pos = text.indexOf(10);
        if (pos != -1){
            text = text.substring(0, pos) + "....";
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_text);
        textView.setText(text);
    }
}
