package com.qualitysoftware.whitetiles;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class ScoreListAdapter extends ArrayAdapter<ScoreEntry> {
    private Context context;
    private ArrayList<ScoreEntry> scoreEntries;

    public ScoreListAdapter(@NonNull Activity _context, ArrayList<ScoreEntry> _scoreEntries) {
        super(_context, R.layout.score_list_item, _scoreEntries);

        context = _context;
        scoreEntries = _scoreEntries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ScoreEntry currentEntry = scoreEntries.get(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.score_list_item, null);

            TextView scoreValue = convertView.findViewById(R.id.scoreValue);
            TextView scoreDate = convertView.findViewById(R.id.scoreDate);
            scoreValue.setText(currentEntry.getValue());
            scoreDate.setText(currentEntry.getTime());
        }
        return convertView;
    }
}
