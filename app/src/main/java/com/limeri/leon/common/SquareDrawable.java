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
        p.setStrokeWidth(3);
        canvas.drawLine(this.getBounds().right,this.getBounds().bottom,this.getBounds().left, this.getBounds().top,p);



    }


    public void drawRect(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(this.getBounds(), p);
        //canvas.drawLine(this.getBounds().left,this.getBounds().bottom,this.getBounds().right, this.getBounds().top,p);



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

    public void renderClear(Canvas c) {
        drawRect( c );

    }



}
