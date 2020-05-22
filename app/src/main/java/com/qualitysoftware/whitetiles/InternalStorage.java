package com.qualitysoftware.whitetiles;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class InternalStorage {
    public static void writeScoresToInternal(Context context, ArrayList<ScoreEntry> scores) {
        File scoresFile = new File(context.getFilesDir(), Constants.HIGHSCORES_FILE);
        Collections.sort(scores);

        try {
            if(scoresFile.createNewFile())  Log.d("INTERNAL STORAGE", "New scores file created");
            else                            Log.d("INTERNAL STORAGE", "Scores file found");

            FileOutputStream fos = new FileOutputStream(scoresFile);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(scores);
            os.close();
            fos.close();

            Log.d("INTERNAL STORAGE", "Scores file was successfully updated");
        } catch (IOException e) {
            Log.e("INTERNAL STORAGE", "Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<ScoreEntry> readScoresFromInternal(Context context){
        File scoresFile = new File(context.getFilesDir(), Constants.HIGHSCORES_FILE);
        if(!scoresFile.exists()){
            Log.wtf("INTERNAL STORAGE", "Tried to read scores from a non-existent score file. HOW DID THIS HAPPEN??");
        }

        ArrayList<ScoreEntry> scoreEntries = new ArrayList<ScoreEntry>();

        try {
            FileInputStream fis = new FileInputStream(scoresFile);
            ObjectInputStream is = new ObjectInputStream(fis);
            scoreEntries = (ArrayList<ScoreEntry>) is.readObject();
        } catch (IOException e) {
            Log.e("INTERNAL STORAGE", "Exception caught: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e("INTERNAL STORAGE", "Exception caught: " + e.getMessage());
            e.printStackTrace();
        }

        return scoreEntries;
    }

    public static void addNewScoreEntryIntoInternal(Context context, ScoreEntry scoreEntryToBeAdded){
        ArrayList<ScoreEntry> internalScores = readScoresFromInternal(context);
        internalScores.add(scoreEntryToBeAdded);
        writeScoresToInternal(context, internalScores);
    }

    public static void deleteAllScores(Context context){
        writeScoresToInternal(context, new ArrayList<ScoreEntry>());
    }
}

/*
    TODO: Check the storage flow with a non-existent file (first time run)
 */