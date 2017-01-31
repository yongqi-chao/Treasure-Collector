package edu.neu.madcourse.yongqichao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DictionaryActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        setTitle("Test Dictionary");

        editText = (EditText) findViewById(R.id.editText);



    }
}
