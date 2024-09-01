package com.example.gpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startOfflineGame(View view) {
        Intent intent = new Intent(this, OfflineAddPlayersActivity.class);
        intent.putExtra("mode", "offline");
        startActivity(intent);
    }

    public void withBotPlay(View view) {
        Intent intent = new Intent(this, AiGameActivity.class);
        String userName = "User"; // You should replace this with the actual user name
        intent.putExtra("playerOneName", "Player1");
        intent.putExtra("playerTwoName", "Bot");
        startActivity(intent);
    }
}
