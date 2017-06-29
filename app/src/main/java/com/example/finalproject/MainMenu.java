package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by pawan on 6/25/17.
 */

public class MainMenu extends AppCompatActivity {

    private Button testButton, tutorialButton;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        testButton = (Button) findViewById(R.id.TestButton);
        tutorialButton = (Button) findViewById(R.id.tutorialButton);

        // Re-diect to test screen
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testScreen = new Intent(context, TestScreenActivity.class);
                startActivity(testScreen);
            }
        });

        // Re-diect to tutorial screen
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tutorialScreen = new Intent(context, TutorialScreenActivity.class);
                startActivity(tutorialScreen);
            }
        });

    }
}
