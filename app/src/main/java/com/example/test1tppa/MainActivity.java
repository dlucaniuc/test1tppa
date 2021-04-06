package com.example.test1tppa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFromSpinner();
    }


    public void initFromSpinner(){
        Spinner spinnerFromUnit = (Spinner) findViewById(R.id.fromUnit);
        Spinner spinnerToUnit = (Spinner) findViewById(R.id.toUnit);

        ArrayAdapter<CharSequence> adapterFromUnit = ArrayAdapter.createFromResource(this,R.array.units, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterToUnit = ArrayAdapter.createFromResource(this,R.array.units, android.R.layout.simple_spinner_item);

        adapterFromUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterToUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromUnit.setAdapter(adapterFromUnit);
        spinnerToUnit.setAdapter(adapterToUnit);
    }

    public void msgAllertConversion(String message){
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setTitle("Rezultat");
        bulider.setMessage(message);
        bulider.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareConversionResult(message);
            }
        });
        bulider.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetValue();
            }
        });

        AlertDialog alertDialog = bulider.create();
        alertDialog.show();
    }

    public String getSrcUnit(){
        Spinner mySpinner = (Spinner) findViewById(R.id.fromUnit);
        return mySpinner.getSelectedItem().toString();
    }

    public String getDstUnit(){
        Spinner mySpinner = (Spinner) findViewById(R.id.toUnit);
        return mySpinner.getSelectedItem().toString();
    }

    public int getValue(){
        TextView myTextView = (TextView) findViewById(R.id.editTextNumber);
        if( myTextView.getText().length() != 0){
            return Integer.parseInt(myTextView.getText().toString());
        }
        return 0;
    }
    public void resetValue(){
        TextView myTextView = (TextView) findViewById(R.id.editTextNumber);
        myTextView.setText("");
    }


    public Double getConversionResult(String fromUnit, String toUnit, int value){
        if (value == 0){
            return new Double(0);
        }
        Hashtable<String, Double> conversionTable = new Hashtable<String, Double>();
        conversionTable.put("mm", 0.1);
        conversionTable.put("cm", 1.0);
        conversionTable.put("m", 100.0);
        conversionTable.put("km", 100000.0);
        conversionTable.put("inch", 2.54);

        Double resultInCm = value * conversionTable.get(fromUnit);
        return resultInCm / conversionTable.get(toUnit);
    }

    public void convertUnits(View view){
        String srcUnit = getSrcUnit();
        String dstUnit = getDstUnit();
        int valueToConvert = getValue();
        Double resultOfConversion = getConversionResult(srcUnit, dstUnit, valueToConvert);
        if(srcUnit == dstUnit){
            Toast.makeText(this,srcUnit +" same as "+ dstUnit,Toast.LENGTH_SHORT).show();
        }
        else{
            String messageToAlert = String.format("%s %s = %s %s",valueToConvert, srcUnit, resultOfConversion, dstUnit);
            msgAllertConversion(messageToAlert);
        }
    }

    public void shareConversionResult(String messageToShare){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, messageToShare);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}