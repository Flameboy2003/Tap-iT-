    package com.example.gpt;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;

    import java.util.ArrayList;
    import java.util.List;

    public class OfflineMainActivity extends AppCompatActivity {
        private boolean isGameOver = false;

        private final List<int[]> combinationsList = new ArrayList<>();
        private final List<Integer> playerOneMoves = new ArrayList<>();
        private final List<Integer> playerTwoMoves = new ArrayList<>();

        private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        private int playerTurn = 1;
        private int totalSelectedBoxes = 1;

        private LinearLayout playerOneLayout, playerTwoLayout;
        private TextView playerOneName, playerTwoName;
        private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_offline_main);

            playerOneName = findViewById(R.id.playerOneName);
            playerTwoName = findViewById(R.id.playerTwoName);

            playerOneLayout = findViewById(R.id.playerOneLayout);
            playerTwoLayout = findViewById(R.id.playerTwoLayout);

            image1 = findViewById(R.id.image1);
            image2 = findViewById(R.id.image2);
            image3 = findViewById(R.id.image3);
            image4 = findViewById(R.id.image4);
            image5 = findViewById(R.id.image5);
            image6 = findViewById(R.id.image6);
            image7 = findViewById(R.id.image7);
            image8 = findViewById(R.id.image8);
            image9 = findViewById(R.id.image9);

            combinationsList.add(new int[]{0, 1, 2});
            combinationsList.add(new int[]{3, 4, 5});
            combinationsList.add(new int[]{6, 7, 8});
            combinationsList.add(new int[]{0, 3, 6});
            combinationsList.add(new int[]{1, 4, 7});
            combinationsList.add(new int[]{2, 5, 8});
            combinationsList.add(new int[]{2, 4, 6});
            combinationsList.add(new int[]{0, 4, 8});

            final String getPlayerOneName = getIntent().getStringExtra("playerOne");
            final String getPlayerTwoName = getIntent().getStringExtra("playerTwo");

            playerOneName.setText(getPlayerOneName);
            playerTwoName.setText(getPlayerTwoName);

            setupImageViewClickListeners();
        }

        private void setupImageViewClickListeners() {
            image1.setOnClickListener(v -> {
                if (isBoxSelectable(0)) {
                    performAction(image1, 0);
                }
            });

            image2.setOnClickListener(v -> {
                if (isBoxSelectable(1)) {
                    performAction(image2, 1);
                }
            });

            image3.setOnClickListener(v -> {
                if (isBoxSelectable(2)) {
                    performAction(image3, 2);
                }
            });

            image4.setOnClickListener(v -> {
                if (isBoxSelectable(3)) {
                    performAction(image4, 3);
                }
            });

            image5.setOnClickListener(v -> {
                if (isBoxSelectable(4)) {
                    performAction(image5, 4);
                }
            });

            image6.setOnClickListener(v -> {
                if (isBoxSelectable(5)) {
                    performAction(image6, 5);
                }
            });

            image7.setOnClickListener(v -> {
                if (isBoxSelectable(6)) {
                    performAction(image7, 6);
                }
            });

            image8.setOnClickListener(v -> {
                if (isBoxSelectable(7)) {
                    performAction(image8, 7);
                }
            });

            image9.setOnClickListener(v -> {
                if (isBoxSelectable(8)) {
                    performAction(image9, 8);
                }
            });
        }

        private void performAction(ImageView imageView, int selectedBoxPosition) {
            if (isGameOver) {
                return; // Prevent any further actions if the game is over
            }

            boxPositions[selectedBoxPosition] = playerTurn;

            if (playerTurn == 1) {
                imageView.setImageResource(R.drawable.cross);
                playerOneMoves.add(selectedBoxPosition);
                if (playerOneMoves.size() > 3) {
                    int oldestMove = playerOneMoves.remove(0);
                    boxPositions[oldestMove] = 0;
                    getImageViewByPosition(oldestMove).setImageResource(R.drawable.transparent);
                }
                if (checkPlayerWin()) {
                    showWinDialog(playerOneName.getText().toString() + " has won the match");
                    isGameOver = true;
                } else if (isDraw()) {
                    showWinDialog("It's a Draw!");
                    isGameOver = true;
                } else {
                    changePlayerTurn(2);
                    totalSelectedBoxes++;
                }
            } else {
                imageView.setImageResource(R.drawable.record);
                playerTwoMoves.add(selectedBoxPosition);
                if (playerTwoMoves.size() > 3) {
                    int oldestMove = playerTwoMoves.remove(0);
                    boxPositions[oldestMove] = 0;
                    getImageViewByPosition(oldestMove).setImageResource(R.drawable.transparent);
                }
                if (checkPlayerWin()) {
                    showWinDialog(playerTwoName.getText().toString() + " has won the match");
                    isGameOver = true;
                } else if (isDraw()) {
                    showWinDialog("It's a Draw!");
                    isGameOver = true;
                } else {
                    changePlayerTurn(1);
                    totalSelectedBoxes++;
                }
            }
        }

        private boolean isDraw() {
            for (int position : boxPositions) {
                if (position == 0) {
                    return false; // There's still an empty box, so it's not a draw
                }
            }
            return true; // All boxes are filled and no one has won
        }

        public void restartMatch() {
            boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            playerTurn = 1;
            totalSelectedBoxes = 1;
            isGameOver = false; // Reset the game over flag
            playerOneMoves.clear();
            playerTwoMoves.clear();

            image1.setImageResource(R.drawable.transparent);
            image2.setImageResource(R.drawable.transparent);
            image3.setImageResource(R.drawable.transparent);
            image4.setImageResource(R.drawable.transparent);
            image5.setImageResource(R.drawable.transparent);
            image6.setImageResource(R.drawable.transparent);
            image7.setImageResource(R.drawable.transparent);
            image8.setImageResource(R.drawable.transparent);
            image9.setImageResource(R.drawable.transparent);
        }

        private ImageView getImageViewByPosition(int position) {
            switch (position) {
                case 0: return image1;
                case 1: return image2;
                case 2: return image3;
                case 3: return image4;
                case 4: return image5;
                case 5: return image6;
                case 6: return image7;
                case 7: return image8;
                case 8: return image9;
                default: return null;
            }
        }

        private void showWinDialog(String message) {
            OfflineWinDialogActivity winDialog = new OfflineWinDialogActivity(OfflineMainActivity.this, message, OfflineMainActivity.this);
            winDialog.setCancelable(false);
            winDialog.show();
        }

        private void changePlayerTurn(int currentPlayerTurn) {
            playerTurn = currentPlayerTurn;
            if (playerTurn == 1) {
                playerOneLayout.setBackgroundResource(R.drawable.rounded_corners);
                playerTwoLayout.setBackgroundResource(R.drawable.round_back_dark_blue);
            } else {
                playerTwoLayout.setBackgroundResource(R.drawable.rounded_corners);
                playerOneLayout.setBackgroundResource(R.drawable.round_back_dark_blue);
            }
        }

        private boolean checkPlayerWin() {
            for (int[] combination : combinationsList) {
                if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn && boxPositions[combination[2]] == playerTurn) {
                    return true;
                }
            }
            return false;
        }

        private boolean isBoxSelectable(int boxPosition) {
            return boxPositions[boxPosition] == 0;
        }
    }
