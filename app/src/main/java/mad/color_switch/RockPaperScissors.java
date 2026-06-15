package mad.color_switch;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

// A RockPaperScissors class for determining whether player continues the Color Switch Game
public class RockPaperScissors extends AppCompatActivity {
    // Define all the variables:
    private int score;   // For storing the current score from Color Switch Game
    private String level; // For storing the current level from Color Switch Game
    private TextView result;    // The result of the Rock Paper Scissors Game
    private ImageView imbScissors, imbRock, imbPaper; // The image button of Rock, Paper, and Scissors
    private ImageView ivComputer;                    // The opponent (it may be a Rock, Paper, or Scissors )
    private MediaPlayer player_win, player_lose, player_draw, player_start; // MP3 Player
    private ImageButton homeButton; // Go to home button
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rock_paper_scissors);
        // line to the element from the layout:
        result = findViewById(R.id.tv_result);
        ivComputer = findViewById(R.id.iv_computer);
        imbScissors = findViewById(R.id.imb_scissors);
        imbPaper = findViewById(R.id.imb_paper);
        imbRock = findViewById(R.id.imb_rock);
        homeButton = findViewById(R.id.rps_button_home);

        // Get the score and level from GameActivity
        score = getIntent().getIntExtra("score", 0);
        level = getIntent().getStringExtra("level");

        // Set the text color
        // When play win:
        String win = getString(R.string.win);
        SpannableString win2 = new SpannableString(win);
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN); // Get the ForegroundColorSpan of green color
        win2.setSpan(fcsGreen, 0, win.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Set the text color
        // When play lose:
        String lose = getString(R.string.lose);
        SpannableString lose2 = new SpannableString(lose);
        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED); // Get the ForegroundColorSpan of red color
        lose2.setSpan(fcsRed, 0, lose.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Set the text color

        // MP3 Player (create the MediaPlayer object for different situation):
        player_win = MediaPlayer.create(this, R.raw.correct);  // When player win
        player_lose = MediaPlayer.create(this, R.raw.wrong2);    // When player lose
        player_draw = MediaPlayer.create(this, R.raw.draw);    // When player and opponent draw
        player_start = MediaPlayer.create(this, R.raw.typing);  // When start the game

        // When user chooses Rock:
        imbRock.setOnClickListener(v-> {

                player_start.start(); // Play sound
                int iComPlay = (int)(Math.random() * 3 + 1); // Random number (Represent opponent, Scissors = 1, Rock = 2, Paper = 3)

                switch (iComPlay) {
                    case 1: // Win (Rock beats Scissors)
                        player_win.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.scissors); // Set the image of opponent
                        result.setText(win2); // Set the result (Show win)
                        mainHandler.postDelayed(this::continueColorSwitch, 1300); // Continue the color switch game
                        break;
                    case 2: // Draw (Rock vs Rock)
                        player_draw.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.rock); // Set the image of opponent
                        result.setText(getString(R.string.draw)); // Set the result (Show draw)
                        break;
                    case 3: // Lose (Rock loses to Paper)
                        player_lose.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.paper); // Set the image of opponent
                        result.setText(lose2); // Set the result (Show lose)
                        mainHandler.postDelayed(this::gameOver, 1800); // Go to gram over page
                        break;
                }

        });

        // When user chooses Scissors:
        imbScissors.setOnClickListener(v-> {

                int iComPlay = (int)(Math.random() * 3 + 1); // Random number (Represent opponent, Scissors = 1, Rock = 2, Paper = 3)

                switch (iComPlay) {
                    case 1: // Draw (Scissors vs Scissors)
                        player_draw.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.scissors); // Set the image of opponent
                        result.setText(getString(R.string.draw)); // Set the result (Show draw)
                        break;
                    case 2: // Lose (Scissors loses to Rock)
                        player_lose.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.rock); // Set the image of opponent
                        result.setText(lose2); // Set the result (Show lose)
                        mainHandler.postDelayed(this::gameOver, 1800); // Go to gram over page
                        break;
                    case 3: // Win (Scissors beats Paper)
                        player_win.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.paper); // Set the image of opponent
                        result.setText(win2);  // Set the result (Show win)
                        mainHandler.postDelayed(this::continueColorSwitch, 1300); // Continue the color switch game
                        break;
                }

        });

        // When user chooses Paper:
        imbPaper.setOnClickListener(v-> {

                int iComPlay = (int)(Math.random() * 3 + 1); // Random number (Represent opponent, Scissors = 1, Rock = 2, Paper = 3)

                switch (iComPlay) {
                    case 1: // Lose (Paper loses to Scissors)
                        player_lose.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.scissors); // Set the image of opponent
                        result.setText(lose2); // Set the result (Show lose)
                        mainHandler.postDelayed(this::gameOver, 1800); // Go to gram over page
                        break;
                    case 2: // Win (Paper beats Rock)
                        player_win.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.rock); // Set the image of opponent
                        result.setText(win2); // Set the result (Show win)
                        mainHandler.postDelayed(this::continueColorSwitch, 1300); // Continue the color switch game
                        break;
                    case 3: // Draw (Paper vs Paper)
                        player_draw.start(); // Play sound
                        ivComputer.setImageResource(R.drawable.paper); // Set the image of opponent
                        result.setText(getString(R.string.draw)); // Set the result (Show draw)
                        break;
                }
        });

        // Go to home page
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the home page
                Intent intent = new Intent(RockPaperScissors.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // Win the game: Continue the color switch game (go back the game page)
    private void continueColorSwitch() {
        Intent intent = new Intent(RockPaperScissors.this, GameActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("level", level); // Pass the level back to continue at the same difficulty
        startActivity(intent);
        finish();
    }

    // Lose the game: Go to game over page
    private void gameOver() {
        Intent intent = new Intent(RockPaperScissors.this, GameOverActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("level", level); // Pass the level back to continue at the same difficulty
        startActivity(intent);
        finish();
    }

}