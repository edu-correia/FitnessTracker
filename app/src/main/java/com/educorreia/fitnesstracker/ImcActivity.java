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
import android.widget.Toast;

import com.educorreia.fitnesstracker.database.DatabaseHelper;

public class ImcActivity extends AppCompatActivity {

    private Button btnCalcImc;
    private EditText editHeight;
    private EditText editWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);
        btnCalcImc = findViewById(R.id.btn_calc_imc);

        btnCalcImc.setOnClickListener((view) -> {
            if(isDataInvalid()){
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            double imc = calculateImc();

            int imcMessageId = imcResponse(imc);

            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                    .setTitle(getString(R.string.imc_response, imc))
                    .setMessage(imcMessageId)
                    .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                        new Thread(() -> {
                            DatabaseHelper db = DatabaseHelper.getInstance(ImcActivity.this);
                            long registerId = db.addRegister("imc", imc);

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
        Intent intent = new Intent(ImcActivity.this, RegistersListActivity.class);
        intent.putExtra("type", "imc");
        startActivity(intent);
    }

    private double calculateImc(){
        final String strHeight = editHeight.getText().toString();
        final String strWeight = editWeight.getText().toString();

        final int height = Integer.parseInt(strHeight);
        final int weight = Integer.parseInt(strWeight);

        final double heightInCentimeters = (double) height / 100;

        return weight / (heightInCentimeters * heightInCentimeters);
    }

    @StringRes
    private int imcResponse(double imc){
        if(imc < 15) return R.string.imc_severely_low_weight;
        else if(imc < 16) return R.string.imc_very_low_weight;
        else if(imc < 18) return R.string.imc_low_weight;
        else if(imc < 25) return R.string.imc_normal;
        else if(imc < 30) return R.string.imc_high_weight;
        else if(imc < 35) return R.string.imc_so_high_weight;
        else if(imc < 40) return R.string.imc_severely_high_weight;
        else return R.string.imc_extreme_weight;
    }

    private boolean isDataInvalid(){
        final String strHeight = editHeight.getText().toString();
        final String strWeight = editWeight.getText().toString();

        final boolean isHeightEmpty = TextUtils.isEmpty(strHeight);
        final boolean doesHeightStartsWithZero = strHeight.startsWith("0");

        final boolean isWeightEmpty = TextUtils.isEmpty(strWeight);
        final boolean doesWeightStartsWithZero = strWeight.startsWith("0");

        final boolean isHeightInvalid = isHeightEmpty || doesHeightStartsWithZero;
        final boolean isWeightInvalid = isWeightEmpty || doesWeightStartsWithZero;

        if(isHeightInvalid || isWeightInvalid) return true;

        return false;
    }

    private void hideKeyboard(EditText edit){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }
}