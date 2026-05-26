package mad.cheung_31_madb;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Looper;
import java.lang.ref.WeakReference;

public class DiceGame extends AppCompatActivity {
    // Define all the variables:
    private static final String TAG = "DiceGame"; // The title of the game
    private int score; // For storing the current score from Color Switch Game
    private String level; // For storing the current level from Color Switch Game
    private ImageView imageDice; // A ImageView for showing the answer
    private TextView textResult; // TextView for showing the result
    private TextView textResult2;    // For showing win or lose
    private Button buttonStart; // A button for starting the game
    private ImageButton homeButton; // Go to home button
    private MediaPlayer player_win, player_lose, player_dice, player_star; // MP3 Player
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static class StaticHandler extends Handler {
        // A field for holding a WeakReference to a DiceGame object
        private final WeakReference<DiceGame> mActivity;
        // A constructor for the StaticHandler class
        public StaticHandler(DiceGame activity) {
            // Initializes the mActivity field by creating a new WeakReference that wraps the provided DiceGame activity instance.
            mActivity = new WeakReference<>(activity);
        }
        // A method for analyze the result and set the ImageView
        @Override
        public void handleMessage(Message msg) {
            DiceGame activity = mActivity.get();
            if (activity == null) return;
            // The random result
            int iRand = (int)(Math.random() * 6 + 1);
            // Get the result title
            String s = activity.getString(R.string.dice_result);
            // Display the result
            activity.textResult.setText(s + iRand);

            // Initialize MediaPlayers with null check
            try {
           // When player lose
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Display the image according to the random result
            switch (iRand) {
                case 1:
                    activity.imageDice.setImageResource(R.drawable.dice1);
                    break;
                case 2:
                    activity.imageDice.setImageResource(R.drawable.dice2);
                    break;
                case 3:
                    activity.imageDice.setImageResource(R.drawable.dice3);
                    break;
                case 4:
                    activity.imageDice.setImageResource(R.drawable.dice4);
                    break;
                case 5:
                    activity.imageDice.setImageResource(R.drawable.dice5);
                    break;
                case 6:
                    activity.imageDice.setImageResource(R.drawable.dice6);
                    break;
            }


            // Set the text color
            // When play win:
            String win = activity.getString(R.string.win);
            SpannableString win2 = new SpannableString(win);
            ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN); // Get the ForegroundColorSpan of green color
            win2.setSpan(fcsGreen, 0, win.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Set the text color
            // When play lose:
            String lose = activity.getString(R.string.lose);
            SpannableString lose2 = new SpannableString(lose);
            ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED); // Get the ForegroundColorSpan of red color
            lose2.setSpan(fcsRed, 0, lose.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Set the text color

            // Check result
            if (iRand == 6) {
                if (activity.player_win != null) {
                    activity.player_win.start();
                }
                activity.textResult2.setText(win2);
                // Delay navigation
                activity.mainHandler.postDelayed(activity::continueColorSwitch, 1300); // Go bake to color switch game
            } else {
                if (activity.player_lose != null) {
                    activity.player_lose.start();
                }
                activity.textResult2.setText(lose2);
                // Delay navigation
                activity.mainHandler.postDelayed(activity::gameOver, 1800);  // Go to game over page
            }
        }
    }
    // Create a StaticHandler object for analyzing the game result
    private final StaticHandler mHandler = new StaticHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dice_game);

        // Initialize UI elements
        imageDice = findViewById(R.id.image_dice);
        textResult = findViewById(R.id.text_result);
        buttonStart = findViewById(R.id.button_start);
        textResult2 = findViewById(R.id.text_result2);
        homeButton = findViewById(R.id.dc_button_home);



        // Initialize MediaPlayers with null check
        try {
            // MP3 Player (create the MediaPlayer object for different situation):
            player_win = MediaPlayer.create(this, R.raw.correct); // When win
            player_lose = MediaPlayer.create(this, R.raw.wrong2);    // When lose
            player_dice = MediaPlayer.create(this, R.raw.dice);  // When the animation start
            player_star = MediaPlayer.create(this, R.raw.typing); // When user types the button
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the score and level from GameActivity
        score = getIntent().getIntExtra("score", 0);
        level = getIntent().getStringExtra("level");

        // Log the received data (for debug)
        Log.d(TAG, "Score received: " + score);
        Log.d(TAG, "Level received: " + level);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if players are initialized before starting
                if (player_star != null) player_star.start(); // play the sound (for clicking the button)
                if (player_dice != null) player_dice.start(); // play the sound (for animation)

                // Get the title data from string.xml
                String diceResult = getString(R.string.dice_result);
                // Display the title
                textResult.setText(diceResult);

                // Start animation
                Resources res = getResources();
                final AnimationDrawable animDraw = (AnimationDrawable) res.getDrawable(R.drawable.anim_roll_dice);
                imageDice.setImageDrawable(animDraw);
                animDraw.start();

                // Start the timer, and stop the animation after the timer stop
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2730);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        animDraw.stop(); // Stop the animation
                        mHandler.sendMessage(mHandler.obtainMessage()); // Start to create the analyze the result (whether the random result is equal to 6)
                    }
                }).start();
            }
        });

        // Go to home page
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the home page
                Intent intent = new Intent(DiceGame.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // Win the game: Continue the color switch game (go back the game page)
    private void continueColorSwitch() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("level", level);
        startActivity(intent);
        finish();
    }

    // Lose the game: Go to game over page
    private void gameOver() {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("level", level);
        startActivity(intent);
        finish();
    }

}