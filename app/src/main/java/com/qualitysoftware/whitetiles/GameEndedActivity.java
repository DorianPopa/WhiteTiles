package com.qualitysoftware.whitetiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    }

    private void initComponents(){
        gameResult = findViewById(R.id.gameResultText);
        scoreDisplay = findViewById(R.id.scoreText);

        gameResult.setText("The game has ended :(");
        scoreDisplay.setText("Final score: " + finalScore);
    }

    private void updateHighscoreStorage(){

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
                Intent intent = new Intent(GameEndedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //findViewById(R.id.highscoresButton)

        //findViewById(R.id.shareOnFacebookButton)


    }
}
