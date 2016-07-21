package com.limeri.leon.common;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nico on 15/05/2016.
 */
public final class DragAndDropSource implements View.OnTouchListener{
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
