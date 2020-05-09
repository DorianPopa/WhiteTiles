package com.qualitysoftware.whitetiles;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileRowGenerator {

    private Canvas canvas;
    private Random r;

    public TileRowGenerator(Canvas _canvas){
        canvas = _canvas;
        r = new Random();
    }

    public List<Tile> GenerateRandomTileRow(int minTiles, int maxTiles){
        int numberOfTiles = r.nextInt((maxTiles - minTiles) + 1) + minTiles;

        ArrayList<Integer> tileIndices = new ArrayList<Integer>();
        while(tileIndices.size() < numberOfTiles){
            int currentTileIndex = r.nextInt(4);
            if(!tileIndices.contains(currentTileIndex))
                tileIndices.add(currentTileIndex);
        }

        Log.d("Tile Generator", "Generating " + numberOfTiles + " with indices: " + tileIndices.toString());

        List<Tile> generatedRow = new ArrayList<Tile>();
        for (int index : tileIndices) {
            Tile newTile = new Tile(canvas, canvas.getHeight() / 4, canvas.getWidth() / 4);
            newTile.moveToNextPosition(0, index);
            generatedRow.add(newTile);
        }

        return generatedRow;
    }
}
