package com.qualitysoftware.whitetiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GameEndedActivity extends AppCompatActivity {

    private TextView gameResult;
    private TextView scoreDisplay;

    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ended);

        finalScore = getIntent().getIntExtra("score", 0);

        initComponents();
        setClickListenersForButtons();

        updateHighscoreStorage();
    }

    private void initComponents(){
        gameResult = findViewById(R.id.gameResultText);
        scoreDisplay = findViewById(R.id.scoreText);

        gameResult.setText("The game has ended :(");
        scoreDisplay.setText("Final score: " + finalScore);
    }

    private void updateHighscoreStorage(){
        ScoreEntry newScore = new ScoreEntry("" + finalScore, generateTimeStamp());
        InternalStorage.addNewScoreEntryIntoInternal(this, newScore);
    }

    private String generateTimeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }

    private void setClickListenersForButtons(){
        findViewById(R.id.restartButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(GameEndedActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.mainMenuButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(GameEndedActivity.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.highscoresButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameEndedActivity.this, HighscoresActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.shareOnFacebookButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Я только что набрал " + finalScore + " очков на WhiteTiles. Побей меня, если сможешь!";
                String title = "WhiteTiles";

                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share your score on..."));
            }
        });


    }
}
