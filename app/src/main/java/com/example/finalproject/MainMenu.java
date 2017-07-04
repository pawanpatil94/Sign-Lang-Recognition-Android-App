package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;

/**
 * Created by pawan on 6/25/17.
 */

public class MainMenu extends AppCompatActivity {

    private Button trainButton, testButton, tutorialButton, connectButton;
    final Context context = this;
    static HashSet<String> availableTest = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        testButton = (Button) findViewById(R.id.TestButton);
        tutorialButton = (Button) findViewById(R.id.tutorialButton);
        trainButton = (Button) findViewById(R.id.mainTrain);

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

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainScreen = new Intent(context, TrainScreenActivity.class);
                startActivity(trainScreen);
            }
        });
    }
}
