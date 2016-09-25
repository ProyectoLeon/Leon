package com.limeri.leon.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {

    public class CanvasThread extends Thread {

        private SurfaceHolder mSurfaceHolder;

        private boolean mRun;

        public CanvasThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        public void run() {
            long now = System.currentTimeMillis();
            long lastTime = now;
            while (mRun) {
                Canvas c = null;
                now = System.currentTimeMillis();
                update(now - lastTime);
                lastTime = now;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    doDraw(c);
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setRunning(boolean running) {
            mRun = running;
        }

    }

    private CanvasThread thread;

    private List<Renderable> renderables;
    private List<Updateable> updateables;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.setFormat(PixelFormat
                .TRANSPARENT);
        holder.addCallback(this);

        thread = new CanvasThread(holder);

        renderables = new ArrayList<Renderable>();
        updateables = new ArrayList<Updateable>();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;

            } catch (InterruptedException e) {
            }
        }

    }

    public void addRenderable(Renderable r) {
        synchronized (renderables) {
            renderables.add(r);
        }
    }


    public void addUpdateable(Updateable u) {
        synchronized (updateables) {
            updateables.add(u);
        }
    }


    private void doDraw(Canvas c) {
        //c.drawARGB(0, 0, 0, 0);
        synchronized (renderables) {
            for (Renderable r : renderables) {
                r.render(c);
            }
        }
    }


    private void update(long elapsed) {
        synchronized (updateables) {
            for (Updateable u : updateables) {
                u.update(elapsed);
            }
        }
    }

}
