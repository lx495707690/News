package com.topnews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class NewsListView extends ListView {


    public NewsListView(Context context) {
        super(context);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // samsung error
        }
    }
}

