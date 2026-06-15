package mad.color_switch;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private static final String TAG = "GameOverActivity"; // Page title
    // Define all the variables:
    private TextView finalScoreTextView; // A textView for showing the final score
    private TextView levelTextView; // A textView for showing the level
    private Button homeButton, playAgainButton; // 'Home' button and 'Play Again' button
    private int score; // For storing the current score from Color Switch Game
    private String level; // For storing the current level from Color Switch Game
    MediaPlayer player_typing; // MP3 Player
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        // line to the element from the layout:
        finalScoreTextView = findViewById(R.id.finalScoreTextView);
        homeButton = findViewById(R.id.homeButton);
        playAgainButton = findViewById(R.id.playAgainButton);
        levelTextView = findViewById(R.id.levelTextView);
        // Initialize MediaPlayers with null check
        try {
            // MP3 Player (create the MediaPlayer object for different situation):
            player_typing = MediaPlayer.create(this, R.raw.typing); // When user types the button
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Get the selected level from one of the mini game page
        level = getIntent().getStringExtra("level");
        // Get the score from one of the mini game page
        score = getIntent().getIntExtra("score", 0);

        // Log the received data (for debug)
        Log.d(TAG, "Score received: " + score);
        Log.d(TAG, "Level received: " + level);

        // Display the level
        levelTextView.setText(String.format(getString(R.string.level_label),level));
        // Display the final score
        finalScoreTextView.setText(String.format(getString(R.string.your_score), score));

        // Home button click
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                // Go to the home page
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Play Again button click
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                // Go back the color switch game and pass the current level  the this page
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                intent.putExtra("level", level); // Use the same level
                intent.putExtra("score", 0); // Reset score for a new game
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack
                startActivity(intent);
                finish();
            }
        });
    }
}