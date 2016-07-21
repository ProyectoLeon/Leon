package com.limeri.leon;

import android.app.Activity;
import android.os.Bundle;

import com.limeri.leon.common.DragAndDropSource;
import com.limeri.leon.common.DragAndDropTarget;

public class MatricesActivity extends Activity {

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrices);
        findViewById(R.id.imgOpcion1).setOnTouchListener(new DragAndDropSource());
        findViewById(R.id.imgOpcion2).setOnTouchListener(new DragAndDropSource());
        findViewById(R.id.imgOpcion3).setOnTouchListener(new DragAndDropSource());
        findViewById(R.id.imgOpcion4).setOnTouchListener(new DragAndDropSource());
        findViewById(R.id.target).setOnDragListener(new DragAndDropTarget(this));
    }
}
