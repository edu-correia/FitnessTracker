package com.educorreia.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View btnImc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImc = findViewById(R.id.btn_imc);
        btnImc.setOnClickListener(btnImcOnClick);
    }

    final private View.OnClickListener btnImcOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, ImcActivity.class);
            startActivity(intent);
        }
    };
}