package com.example.gpt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AiWinDialogActivity extends Dialog {

    private String message;
    private AiGameActivity aiGameActivity;

    public AiWinDialogActivity(Context context, String message, AiGameActivity aiGameActivity) {
        super(context);
        this.message = message;
        this.aiGameActivity = aiGameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_win_dialog);

        // Set the message
        TextView messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setText(message);

        // Set up the close button
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            dismiss();
            aiGameActivity.restartMatch(); // Restart the game after closing the dialog
        });

        setCancelable(false); // Prevent the dialog from being dismissed by clicking outside
    }
}
