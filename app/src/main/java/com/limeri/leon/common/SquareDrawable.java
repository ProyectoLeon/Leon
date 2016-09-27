package com.limeri.leon.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class SquareDrawable extends Drawable implements Renderable {
    private int color;

    public SquareDrawable(String c) {
        color = Color.parseColor(c);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(this.getBounds(), p);


    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public void render(Canvas c) {
        draw( c );

    }



}
