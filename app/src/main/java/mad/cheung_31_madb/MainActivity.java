package mad.cheung_31_madb;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Define all the variables:
    private Button startGameButton, chooseLevelButton ; // 'Start Game' button and 'Choose level' button
    private ImageButton aboutMeButton, introButton;  // 'About Me' button, and 'Introduction' button
    private TextView levelTextView; // A TextView for showing the current level
    private String currentLevel = "Easy"; // Default level
    MediaPlayer player_typing; // MP3 Player
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // line to the element from the layout:
        startGameButton = findViewById(R.id.startGameButton);
        chooseLevelButton = findViewById(R.id.chooseLevelButton);
        levelTextView = findViewById(R.id.levelTextView);
        aboutMeButton = findViewById(R.id.button_about_me);
        introButton = findViewById(R.id.button_intro);
        // Initialize MediaPlayers with null check
        try {
            // MP3 Player (create the MediaPlayer object for different situation):
            player_typing = MediaPlayer.create(this, R.raw.typing); // When user types the button
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set initial level text
        levelTextView.setText(String.format(getString(R.string.level_label), currentLevel));

        // Start Game button click
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                // Go to the game page and pass the current level to this page
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("level", currentLevel);
                startActivity(intent);
            }
        });

        // Choose Level button click
        chooseLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the sound when click the color button
                if (player_typing != null) {
                    player_typing.start();
                }
                // Cycle through levels: Easy -> Medium -> Hard -> Easy
                if (currentLevel.equals("Easy")) {
                    currentLevel = "Medium";
                } else if (currentLevel.equals("Medium")) {
                    currentLevel = "Hard";
                } else {
                    currentLevel = "Easy";
                }
                // Set the new level text
                levelTextView.setText(String.format(getString(R.string.level_label), currentLevel));
            }
        });

        // About me button click
        aboutMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the about me page
                Intent intent = new Intent(MainActivity.this, AboutMe.class);
                startActivity(intent);
            }
        });

        // About me button click
        introButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the introduction page
                Intent intent = new Intent(MainActivity.this, Introduction.class);
                startActivity(intent);
            }
        });
    }

}