package com.example.gpt;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AiGameActivity extends AppCompatActivity {
    private boolean isGameOver = false;

    private final List<int[]> combinationsList = new ArrayList<>();
    private final List<Integer> playerOneMoves = new ArrayList<>();
    private final List<Integer> botMoves = new ArrayList<>();

    private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int playerTurn = 1; // 1 for player, 2 for bot

    private LinearLayout playerOneLayout;
    private TextView playerOneName;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_game);

        playerOneName = findViewById(R.id.playerOneName);
        playerOneLayout = findViewById(R.id.playerOneLayout);

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
        if (getPlayerOneName != null && !getPlayerOneName.trim().isEmpty()) {
            playerOneName.setText(getPlayerOneName);
        } else {
            playerOneName.setText("Player1");
        }

        setupImageViewClickListeners();
    }

    private void setupImageViewClickListeners() {
        image1.setOnClickListener(v -> handleBoxClick(0));
        image2.setOnClickListener(v -> handleBoxClick(1));
        image3.setOnClickListener(v -> handleBoxClick(2));
        image4.setOnClickListener(v -> handleBoxClick(3));
        image5.setOnClickListener(v -> handleBoxClick(4));
        image6.setOnClickListener(v -> handleBoxClick(5));
        image7.setOnClickListener(v -> handleBoxClick(6));
        image8.setOnClickListener(v -> handleBoxClick(7));
        image9.setOnClickListener(v -> handleBoxClick(8));
    }

    private void handleBoxClick(int position) {
        if (isGameOver || !isBoxSelectable(position)) {
            return;
        }

        performAction(getImageViewByPosition(position), position);

        if (!isGameOver && playerTurn == 2) {
            botPlay();
        }
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        if (isGameOver) {
            return; // Prevent any further actions if the game is over
        }

        boxPositions[selectedBoxPosition] = playerTurn;
        imageView.setImageResource(playerTurn == 1 ? R.drawable.cross : R.drawable.record);

        if (playerTurn == 1) {
            playerOneMoves.add(selectedBoxPosition);
            if (playerOneMoves.size() > 3) {
                int oldestMove = playerOneMoves.remove(0);
                boxPositions[oldestMove] = 0;
                getImageViewByPosition(oldestMove).setImageResource(R.drawable.transparent);
            }
        } else {
            botMoves.add(selectedBoxPosition);
            if (botMoves.size() > 3) {
                int oldestMove = botMoves.remove(0);
                boxPositions[oldestMove] = 0;
                getImageViewByPosition(oldestMove).setImageResource(R.drawable.transparent);
            }
        }

        if (checkPlayerWin()) {
            showWinDialog((playerTurn == 1 ? playerOneName.getText().toString() : "Bot") + " has won the match");
            isGameOver = true;
        } else if (isDraw()) {
            showWinDialog("It's a Draw!");
            isGameOver = true;
        } else {
            changePlayerTurn(playerTurn == 1 ? 2 : 1);
        }
    }

    private int evaluateBoard(int[] board) {
        // Check for all possible winning combinations
        for (int[] combination : combinationsList) {
            if (board[combination[0]] == 2 && board[combination[1]] == 2 && board[combination[2]] == 2) {
                return 10; // Bot wins
            } else if (board[combination[0]] == 1 && board[combination[1]] == 1 && board[combination[2]] == 1) {
                return -10; // Player wins
            }
        }
        return 0; // No winner, it's a neutral position
    }


    private int minimax(int[] board, int depth, boolean isBot) {
        int score = evaluateBoard(board);

        if (score == 10 || score == -10)
            return score;

        if (isDraw())
            return 0;

        if (isBot) {
            int best = Integer.MIN_VALUE;

            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) {
                    board[i] = 2;
                    best = Math.max(best, minimax(board, depth + 1, false));
                    board[i] = 0;
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) {
                    board[i] = 1;
                    best = Math.min(best, minimax(board, depth + 1, true));
                    board[i] = 0;
                }
            }
            return best;
        }
    }

    private int findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < boxPositions.length; i++) {
            if (boxPositions[i] == 0) {
                boxPositions[i] = 2;
                int moveVal = minimax(boxPositions, 0, false);
                boxPositions[i] = 0;

                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }

        return bestMove;
    }


    private void botPlay() {
        if (isGameOver) {
            return;
        }

        new Handler().postDelayed(() -> {
            int bestMove = findBestMove();
            performAction(getImageViewByPosition(bestMove), bestMove);
        }, 2000);
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
        isGameOver = false; // Reset the game over flag
        playerOneMoves.clear();
        botMoves.clear();

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
            case 0:
                return image1;
            case 1:
                return image2;
            case 2:
                return image3;
            case 3:
                return image4;
            case 4:
                return image5;
            case 5:
                return image6;
            case 6:
                return image7;
            case 7:
                return image8;
            case 8:
                return image9;
            default:
                return null;
        }
    }

    private void showWinDialog(String message) {
        AiWinDialogActivity aiWinDialogActivity = new AiWinDialogActivity(AiGameActivity.this, message, AiGameActivity.this);
        aiWinDialogActivity.show();
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            playerOneLayout.setBackgroundResource(R.drawable.round_back_blue_border);
        }
    }

    private boolean checkPlayerWin() {
        for (int[] combination : combinationsList) {
            if (boxPositions[combination[0]] == playerTurn &&
                    boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }
}