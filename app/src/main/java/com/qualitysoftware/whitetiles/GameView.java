package com.qualitysoftware.whitetiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GameView extends SurfaceView implements Runnable {

    private Context context;

    private Thread drawingThread = null;
    private boolean canDraw = false;

    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private  TileRowGenerator rowGenerator;
    List<Tile> tileList = new ArrayList<Tile>();

    private boolean gameHasEnded = false;
    private int gameSpeed = 5;

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

    private void drawBackground(Canvas canvas){
        canvas.drawColor(Color.BLACK);
    }

    private void updateTiles(){
        ListIterator<Tile> iter = tileList.listIterator();
        while(iter.hasNext()){
            Tile t = iter.next();
            if(t.isOutsideOfScreen()){
                if(!t.isClicked()){
                    Log.d("Tile Update", "Tile " + t.toString() + " is not clicked and outside of screen, game has ended\n");
                    //gameHasEnded = true;
                    //break;
                }
                else{
                    Log.d("Tile Update", "Tile: " + t.toString() + " is not clicked and outside of screen, will be removed\n");
                    iter.remove();
                }
            }
            else{
                t.moveAbsolute(gameSpeed, 0);
                t.draw();
            }
        }
    }

    @Override
    public void run() {
        Log.d("GameView","Run method called\n");

        int tick = 0;

        while(canDraw && !gameHasEnded){
            tick++;
            if(!surfaceHolder.getSurface().isValid())   continue;
            canvas = surfaceHolder.lockCanvas();
            if(rowGenerator == null)    rowGenerator = new TileRowGenerator(canvas);

            drawBackground(canvas);

            if(tick % ((canvas.getHeight() / 4) / gameSpeed) == 0){
                tileList.addAll(rowGenerator.GenerateRandomTileRow(1, 2));
                tick = 0;
            }

            updateTiles();

            drawGuideLines(canvas);
            // drawScore();
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
