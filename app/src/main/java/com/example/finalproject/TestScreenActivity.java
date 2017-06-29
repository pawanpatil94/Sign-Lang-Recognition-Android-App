package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by arjun on 6/28/17.
 */

public class TestScreenActivity extends Activity {

    private Spinner chosseLetterDropDown;
    private Button scanButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_screen);
        scanButton = (Button) findViewById(R.id.scanButton);
        chosseLetterDropDown = (Spinner)findViewById(R.id.choose_letter);

        // https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        String[] lettersList = new String[]{"A", "B", "C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lettersList);
        chosseLetterDropDown.setAdapter(adapter);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chosseLetterDropDown.getSelectedItem().toString();
                Log.d("TestScreen", "Selected Value from dropdown: "+text);
            }
        });


        //    Spinner chosseLetterDropDown = (Spinner) findViewById(R.id.choose_letter);
        //    List<String> list = new ArrayList<>();
        //	list.add("A");
        //	list.add("B");
        //	list.add("C");
        //    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
        //            android.R.layout.simple_spinner_item, list);
        //	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //	chosseLetterDropDown.setAdapter(dataAdapter);

    }

}