package com.SEproject.guiderapp.extra;

import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Disables all touches for a RecyclerView
 * Used before displaying tutorial
 */
public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return true;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}