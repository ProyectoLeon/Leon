package com.limeri.leon.common;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.limeri.leon.R;

public interface Renderable {
    void render( Canvas c );
    Rect getBounds();
}
