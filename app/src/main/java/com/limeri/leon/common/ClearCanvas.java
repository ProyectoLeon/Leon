package com.limeri.leon.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;


    public class ClearCanvas extends Drawable implements Renderable {


        @Override
        public void draw(Canvas canvas) {
            Paint p = new Paint();
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
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

        public void renderClear(Canvas c) {
            draw( c );

        }

    }

