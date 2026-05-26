package mad.cheung_31_madb;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GuessingGame extends AppCompatActivity {
    // Define all the variables:
    private int score;   // For storing the current score from Color Switch Game
    private String level; // For storing the current level from Color Switch Game
    Button confirm; // A button to confirm the answer
    private ImageButton homeButton; // Go to home button
    TextView number; // A TextView for showing current range of answers
    TextView result; // A TextView for showing the result (win or lose)
    TextView answer; // A TextView for showing the answer for testing
    EditText userInput; // A EditText for users to enter their answer
    int count=3; // A int for Limiting the number of times the user can enter an answer
    int first = 1,last=100; // The 3

    int userInput2; // A variable for converting user input to int
    int iComPlay = (int)(Math.random()*98+2); // Random answer
    MediaPlayer player_win, player_lose, player_confirm; // MP3 Player
    String IComPlay;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guessing_game);
        // line to the element from the layout:
        confirm = findViewById(R.id.button_confirm);
        number = findViewById(R.id.tv_number);
        result = findViewById(R.id.tv_result);
        userInput = findViewById(R.id.et_user_input);
        answer = findViewById(R.id.tv_answer);
        homeButton = findViewById(R.id.gg_button_home);

        // Initialize MediaPlayers with null check
        try {
            // MP3 Player (create the MediaPlayer object for different situation):
            player_win = MediaPlayer.create(this, R.raw.correct); // When win
            player_lose = MediaPlayer.create(this, R.raw.wrong2);    // When lose
            player_confirm = MediaPlayer.create(this, R.raw.typing); // When user types the button
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Get the score from RockPaperScissors
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


        // Make the random number to String
        IComPlay = String.valueOf(iComPlay);
        // Display the answer
        answer.setText(IComPlay);
        // Displays the number of times the user entered the answer
        result.setText(getString(R.string.chance) + count);
        // Display the range of the answers
        number.setText(first + " - " + last);

        confirm.setOnClickListener(v-> {
                // Play the sound when click the confirm button
                if (player_confirm != null) {
                    player_confirm.start();
                }
                try {
                    // Get user input and converting it to int
                    userInput2 = Integer.parseInt(userInput.getText().toString());
                    // Empty EditText
                    userInput.setText("");
                    // Limit the user input must be higher than 0
                    if(count > 0 )
                    {
                        // Limit user input must be between the range
                        if( userInput2 > first && userInput2 < last ) {
                            // The last time the user entered an answer, the answer was incorrect
                            if (count == 1 && userInput2 != iComPlay) {
                                result.setText(lose2);
                                number.setText(IComPlay);
                                // Play lose sound
                                if (player_lose != null) {
                                    player_lose.start();
                                }
                                mainHandler.postDelayed(this::gameOver, 1800); // Go to gram over page

                            }
                            // The user input is correct
                            else if (userInput2 == iComPlay) {
                                // Play win sound
                                if (player_win != null) {
                                    player_win.start();
                                }
                                result.setText(win2);
                                number.setText(IComPlay);
                                mainHandler.postDelayed(this::continueColorSwitch, 1300); // Continue the color switch game

                            }
                            // The user input is higher than the answer
                            else if (userInput2 > iComPlay) {
                                last = userInput2;
                                count--;
                                result.setText(getString(R.string.chance) + count);
                                number.setText(first + " - " + last);
                            }
                            // The user input is lower than the answer
                            else if (userInput2 < iComPlay) {
                                first = userInput2;
                                count--;
                                result.setText(getString(R.string.chance) + count);
                                number.setText(first + " - " + last);
                            }
                        }
                        // Show error message when the user input is not between the range
                        else
                        {
                            Toast error =Toast.makeText(getApplicationContext(),"The number must be between" + first +" - " + last + "!" ,Toast.LENGTH_SHORT);
                            error.setGravity(Gravity.TOP|Gravity.LEFT,160,50);                         //Error message
                            error.show();
                        }
                    }
                }catch (Exception e)
                {   // Show error message when the input is not integer
                    Toast error =Toast.makeText(getApplicationContext(),R.string.error,Toast.LENGTH_SHORT);     // Error message
                    error.setGravity(Gravity.TOP|Gravity.LEFT,260,50);
                    error.show();
                }

        });
// Go to home page
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the home page
                Intent intent = new Intent(GuessingGame.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // Win the game: Continue the color switch game (go back the game page)
    private void continueColorSwitch() {
        Intent intent = new Intent(GuessingGame.this, GameActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("level", level); // Pass the level back to continue at the same difficulty
        startActivity(intent);
        finish();
    }

    // Lose the game: Go to game over page
    private void gameOver() {
        Intent intent = new Intent(GuessingGame.this, GameOverActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("level", level); // Pass the level back to continue at the same difficulty
        startActivity(intent);
        finish();
    }
}