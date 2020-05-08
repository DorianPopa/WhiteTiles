package com.qualitysoftware.whitetiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Context context;

    private Thread drawingThread = null;
    private boolean canDraw = false;

    private Canvas canvas;
    private SurfaceHolder surfaceHolder;


    public GameView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context _context){
        context = _context;
        surfaceHolder = getHolder();
        setZOrderOnTop(true);

    }

    private void drawGuideLines(Canvas canvas){
        int lineWidth = 5;

        int tileWidth = canvas.getWidth() / 4;
        int tileHeight = canvas.getHeight() / 4;

        Paint newPaint = new Paint();
        newPaint.setColor(Color.GREEN);
        newPaint.setStrokeWidth(lineWidth);

        canvas.drawLine(tileWidth - lineWidth / 2, 0, tileWidth - lineWidth / 2, canvas.getHeight(), newPaint);

        canvas.drawLine(2 * tileWidth - lineWidth / 2, 0, 2 * tileWidth - lineWidth / 2, canvas.getHeight(), newPaint);

        canvas.drawLine(3 * tileWidth - lineWidth / 2, 0, 3 * tileWidth - lineWidth / 2, canvas.getHeight(), newPaint);
    }

    @Override
    public void run() {
        Log.d("GameView","Run method called\n");

        while(canDraw){
            if(!surfaceHolder.getSurface().isValid())   continue;
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(0);
            drawGuideLines(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        Log.d("GameView","Pause method called\n");

        canDraw = false;
        while(true){
            try {
                drawingThread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        drawingThread = null;
    }

    public void resume(){
        Log.d("GameView","Resume method called\n");

        canDraw = true;
        drawingThread = new Thread(this);
        drawingThread.start();
    }
}
