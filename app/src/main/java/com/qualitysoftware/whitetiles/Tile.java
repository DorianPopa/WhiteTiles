package com.qualitysoftware.whitetiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Tile {
    private Canvas canvas;

    private Rect body;
    private Point coords;
    private int height;
    private int width;

    private Paint defaultPaint;
    private Paint clickedPaint;

    private boolean clicked = false;

    public Tile(Canvas _canvas, int _height, int _width){
        canvas = _canvas;

        body = new Rect();
        height = _height;
        width = _width;
        coords = new Point(0,-height);

        defaultPaint = new Paint();
        defaultPaint.setColor(Color.GREEN);
        defaultPaint.setStyle(Paint.Style.FILL);

        clickedPaint = new Paint();
        clickedPaint.setColor(Color.parseColor("#006300"));
        clickedPaint.setStyle(Paint.Style.FILL);
    }

    private void setCoords(Point newCoords){
        coords = newCoords;

        body.top = coords.y;
        body.bottom = coords.y + height;
        body.left = coords.x;
        body.right = coords.x + width;
    }

    public void moveAbsolute(int distanceDown, int distanceRight){
        setCoords(new Point(coords.x + distanceRight, coords.y + distanceDown));
    }

    public void moveToNextPosition(int spacesDown, int spacesRight){
        setCoords(new Point(coords.x + (spacesRight * width), coords.y + (spacesDown * height)));
    }

    public boolean isOutsideOfScreen(){
        if(coords.y + height > canvas.getHeight())
            return true;
        return false;
    }

    public boolean isInsideBody(Point p){
        return body.contains(p.x, p.y);
    }

    public void draw(){
        if(!clicked)
            canvas.drawRect(body, defaultPaint);
        else
            canvas.drawRect(body, clickedPaint);
    }

    boolean isClicked(){
        return clicked;
    }

    public void click(){
        if(!clicked){
            clicked = true;
            // play sound
        }
    }
}
