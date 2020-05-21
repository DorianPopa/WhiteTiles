package com.qualitysoftware.whitetiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
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

    SoundManager soundManager;

    private boolean gameHasEnded = false;
    private int gameSpeed = 5;

    private int score = 0;
    private int combo = 0;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context _context){
        context = _context;
        surfaceHolder = getHolder();
        soundManager = new SoundManager(context);
        setZOrderOnTop(true);
    }

    private void drawGuideLines(Canvas canvas){
        int lineWidth = 5;

        int tileWidth = canvas.getWidth() / 4;
        int tileHeight = canvas.getHeight() / 4;

        Paint newPaint = new Paint();
        newPaint.setColor(Color.parseColor("#006300"));
        newPaint.setStrokeWidth(lineWidth);

        canvas.drawLine(tileWidth - lineWidth / 2, 0, tileWidth - lineWidth / 2, canvas.getHeight(), newPaint);

        canvas.drawLine(2 * tileWidth - lineWidth / 2, 0, 2 * tileWidth - lineWidth / 2, canvas.getHeight(), newPaint);

        canvas.drawLine(3 * tileWidth - lineWidth / 2, 0, 3 * tileWidth - lineWidth / 2, canvas.getHeight(), newPaint);
    }

    private void drawScore(Canvas canvas){
        String scoreString = "Score: " + score;
        String speedString = "Speed: " + gameSpeed;

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(32 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);

        int scoreStringWidth = (int) textPaint.measureText(scoreString);
        int speedStringWidth = (int) textPaint.measureText(speedString);

        StaticLayout scoreStaticLayout = new StaticLayout(scoreString, textPaint, scoreStringWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
        StaticLayout speedStaticLayout = new StaticLayout(speedString, textPaint, speedStringWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);

        canvas.translate(0f, 50f);
        scoreStaticLayout.draw(canvas);
        canvas.translate(0f,0f);

        canvas.translate((float)(this.canvas.getWidth() - speedStringWidth), 0f);
        speedStaticLayout.draw(canvas);
        canvas.translate(0f,0f);
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
                    Log.d("Tile Update", "Tile " + t.toString() + " is NOT clicked and outside of screen, game has ended\n");
                    gameHasEnded = true;
                    break;
                }
                else{
                    Log.d("Tile Update", "Tile: " + t.toString() + " is clicked and outside of screen, will be removed\n");
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
    public boolean onTouchEvent(MotionEvent event){
        Point touchCoords = new Point((int)(event.getX()), (int)(event.getY()));
        boolean clickedOnActualTile = false;
        for(Tile t : tileList){
            if(t.isInsideBody(touchCoords)){
                if(!t.isClicked()){
                    t.click();
                    clickedOnActualTile = true;
                    soundManager.playSound(1);

                    score += gameSpeed;
                    combo += 1;
                    if(combo % 10 == 0){
                        gameSpeed *= 1.5;
                        Log.d("GameEvent", "Speed increased to " + gameSpeed);
                    }
                    break;
                }
                else{
                    gameHasEnded = true;
                }
            }
        }
        if(!clickedOnActualTile)
            gameHasEnded = true;

        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
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
            drawScore(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        if(gameHasEnded){
            Intent intent = new Intent(context, GameEndedActivity.class);
            intent.putExtra("score", score);
            this.context.startActivity(intent);
            ((Activity) context).finish();
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
