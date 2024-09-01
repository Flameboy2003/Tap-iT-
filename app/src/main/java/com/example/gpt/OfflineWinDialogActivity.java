package com.example.gpt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class OfflineWinDialogActivity extends Dialog {

    private final String message;
    private final OfflineMainActivity offlineMainActivity;

    public OfflineWinDialogActivity(@NonNull Context context, String message, OfflineMainActivity offlineMainActivity) {
        super(context);
        this.message = message;
        this.offlineMainActivity = offlineMainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_win_dialog);

        final TextView messageTxt = findViewById(R.id.messageTxt);
        final Button startAgainBtn = findViewById(R.id.startAgainBtn);

        messageTxt.setText(message);
        startAgainBtn.setOnClickListener(v -> {
            offlineMainActivity.restartMatch();
            dismiss();
        });
    }
}
