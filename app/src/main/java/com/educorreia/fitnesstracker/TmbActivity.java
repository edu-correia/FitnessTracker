package com.educorreia.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.educorreia.fitnesstracker.database.DatabaseHelper;

public class TmbActivity extends AppCompatActivity {

    private Button btnCalcTmb;
    private EditText editHeight;
    private EditText editWeight;
    private EditText editAge;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editHeight = findViewById(R.id.edit_tmb_height);
        editWeight = findViewById(R.id.edit_tmb_weight);
        editAge = findViewById(R.id.edit_tmb_age);
        spinner = findViewById(R.id.spn_tmb_lifestyle);
        btnCalcTmb = findViewById(R.id.btn_calc_tmb);

        btnCalcTmb.setOnClickListener((view) -> {
            if(isDataInvalid()){
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            double tmb = calculateTmb();

            AlertDialog dialog = new AlertDialog.Builder(TmbActivity.this)
                    .setMessage(getString(R.string.tmb_response, tmb))
                    .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                        new Thread(() -> {
                            DatabaseHelper db = DatabaseHelper.getInstance(TmbActivity.this);
                            long registerId = db.addRegister("tmb", tmb);

                            runOnUiThread(() -> {
                                if(registerId > 0) {
                                    Toast.makeText(this, R.string.calc_saved, Toast.LENGTH_LONG).show();
                                    openRegistersList();
                                }
                            });
                        }).start();
                    })
                    .setNegativeButton(R.string.close, ((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }))
                    .create();
            dialog.show();

            hideKeyboard(editHeight);
            hideKeyboard(editWeight);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                openRegistersList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openRegistersList(){
        Intent intent = new Intent(TmbActivity.this, RegistersListActivity.class);
        intent.putExtra("type", "tmb");
        startActivity(intent);
    }

    private double calculateTmb(){
        final String strHeight = editHeight.getText().toString();
        final String strWeight = editWeight.getText().toString();
        final String strAge = editAge.getText().toString();

        final int height = Integer.parseInt(strHeight);
        final int weight = Integer.parseInt(strWeight);
        final int age = Integer.parseInt(strAge);

        double tmbTemp = 66 + (weight * 13.8) + (5 * height) - (6.8 * age);

        int spinnerIndex = spinner.getSelectedItemPosition();

        double[] multipliers = new double[]{1.2, 1.375, 1.55, 1.725, 1.9};

        double selectedMultiplier = multipliers[spinnerIndex];

        return tmbTemp * selectedMultiplier;
    }

    private boolean isDataInvalid(){
        final String strHeight = editHeight.getText().toString();
        final String strWeight = editWeight.getText().toString();
        final String strAge = editAge.getText().toString();

        final boolean isHeightEmpty = TextUtils.isEmpty(strHeight);
        final boolean doesHeightStartsWithZero = strHeight.startsWith("0");

        final boolean isWeightEmpty = TextUtils.isEmpty(strWeight);
        final boolean doesWeightStartsWithZero = strWeight.startsWith("0");

        final boolean isAgeEmpty = TextUtils.isEmpty(strAge);
        final boolean doesAgeStartsWithZero = strAge.startsWith("0");

        final boolean isHeightInvalid = isHeightEmpty || doesHeightStartsWithZero;
        final boolean isWeightInvalid = isWeightEmpty || doesWeightStartsWithZero;
        final boolean isAgeInvalid = isAgeEmpty || doesAgeStartsWithZero;

        if(isHeightInvalid || isWeightInvalid || isAgeInvalid) return true;

        return false;
    }

    private void hideKeyboard(EditText edit){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }
}