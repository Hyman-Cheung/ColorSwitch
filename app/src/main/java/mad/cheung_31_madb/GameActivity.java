package mad.cheung_31_madb;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    // Define all the variables:
    private TextView scoreTextView, colorNameTextView, levelTextView; // A textView for showing the color name, user's score, and the level
    private ProgressBar timerBar; // For displaying a limited time progress bar
    private Button redButton, greenButton, blueButton, yellowButton; // Color buttons
    private ImageButton backButton; // Back button
    private int score = 0; // Initialize user's score
    private String currentColorName; // Define a String variable for storing color name
    private CountDownTimer timer; // Timer object
    private long timerDuration; // For storing time limits in milliseconds
    private String level; // For storing the current level from Color Switch Game
    private final String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"}; // Initialize an array of color names
    private final int[] colorValues = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}; // Initialize an array of color
    MediaPlayer player_lose, player_typing; // MP3 Player
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        // line to the element from the layout:
        scoreTextView = findViewById(R.id.scoreTextView);
        colorNameTextView = findViewById(R.id.colorNameTextView);
        timerBar = findViewById(R.id.timerBar);
        redButton = findViewById(R.id.redButton);
        greenButton = findViewById(R.id.greenButton);
        blueButton = findViewById(R.id.blueButton);
        yellowButton = findViewById(R.id.yellowButton);
        backButton = findViewById(R.id.button_back);
        levelTextView = findViewById(R.id.levelTextView);

        // Initialize MediaPlayers with null check
        try {
            // MP3 Player (create the MediaPlayer object for different situation):
            player_lose = MediaPlayer.create(this, R.raw.wrong);    // When lose
            player_typing = MediaPlayer.create(this, R.raw.typing); // When user types the button
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the selected level from MainActivity or RockPaperScissors
        level = getIntent().getStringExtra("level");
        // Get the score from MainActivity or RockPaperScissors
        score = getIntent().getIntExtra("score", 0);

        // Set time limits based on level
        switch (level) {
            case "Easy":
                timerDuration = 3500; // 3.5 seconds
                break;
            case "Medium":
                timerDuration = 2500; // 2.5 seconds
                break;
            case "Hard":
                timerDuration = 1500; // 1.5 seconds
                break;
            default:
                timerDuration = 3500;
        }
        // Display the level:
        levelTextView.setText(String.format(getString(R.string.level_label), level));
        // Initialize score
        scoreTextView.setText(String.format(getString(R.string.score_label), score));

        // Generate first color
        generateNewColor();

        // Set up button listeners
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                checkColor("RED");
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                checkColor("GREEN");
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                checkColor("BLUE");
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                checkColor("YELLOW");
            }
        });

        // Go to home page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the home page
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        // Start the timer
        startTimer();
    }
    // A method for generating and displaying the new color name with new color
    private void generateNewColor() {
        Random random = new Random();
        currentColorName = colors[random.nextInt(colors.length)];
        int randomTextColor = colorValues[random.nextInt(colorValues.length)];
        colorNameTextView.setText(currentColorName);
        colorNameTextView.setTextColor(randomTextColor);
    }

    // A method for stating the timer again
    private void startTimer() {
        timer = new CountDownTimer(timerDuration, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 100 / timerDuration);
                timerBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                // Play lose sound
                if (player_lose != null) {
                    player_lose.start();
                }
                // Time's up, go to RockPaperScissors
                timer.cancel();
                miniGame();
                finish();
            }
        }.start();
    }

    // A method for checking whether user input match the answer
    private void checkColor(String selectedColor) {
        if (selectedColor.equals(currentColorName)) {
            // Correct color
            score++;  // Add one score
            scoreTextView.setText(String.format(getString(R.string.score_label), score)); // Display the new score
            timer.cancel(); // Stop the current timer
            generateNewColor(); // Generate a new color
            startTimer(); // Restart the timer
        } else {
            // Wrong color, go to RockPaperScissors
            // Play lose sound
            if (player_lose != null) {
                player_lose.start();
            }
            timer.cancel(); // Stop the timer
            miniGame();
        }
    }
    // A method for going to the mini game page randomly when losing the color switch game
    private void miniGame(){
        int gameNum = (int)(Math.random() * 3 + 1); // Random number (1 to 3)

        switch (gameNum){
            // Rock Paper Scissors
            case 1:
                Intent intent = new Intent(GameActivity.this, RockPaperScissors.class);
                intent.putExtra("score", score);
                intent.putExtra("level", level); // Pass the current level
                startActivity(intent);
                finish();
                break;
                // Guessing Game
            case 2:
                Intent intent2 = new Intent(GameActivity.this, GuessingGame.class);
                intent2.putExtra("score", score);
                intent2.putExtra("level", level); // Pass the current level
                startActivity(intent2);
                finish();
                break;
                // Dice Game
            case 3:
                Intent intent3 = new Intent(GameActivity.this, DiceGame.class);
                intent3.putExtra("score", score);
                intent3.putExtra("level", level); // Pass the current level
                startActivity(intent3);
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}