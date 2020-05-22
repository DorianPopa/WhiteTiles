package com.qualitysoftware.whitetiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class HighscoresActivity extends AppCompatActivity {

    private ListView scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        scoreList = (ListView)findViewById(R.id.scoreList);

        ArrayList<ScoreEntry> internalScores = InternalStorage.readScoresFromInternal(this);
        ScoreListAdapter adapter = new ScoreListAdapter(this, internalScores);
        scoreList.setAdapter(adapter);

        setClickListenersForButtons();
    }

    private void setClickListenersForButtons() {
        findViewById(R.id.resetScoresButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InternalStorage.deleteAllScores(HighscoresActivity.this);
                Intent intent = new Intent(HighscoresActivity.this, HighscoresActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
