package com.akeso.akesobandreader;


import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.nfc.Tag;
import android.nfc.tech.*;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import android.nfc.NdefMessage;

public class MainActivity extends AppCompatActivity {

    public String[] userData;
    public boolean running;


    private TextView name;
    private TextView allergies;
    private TextView medications;
    private TextView conditions;
    private TextView contact1;
    private TextView contact2;
    private TextView notes;
    private TextView main;

    private NfcAdapter nAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        nAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nAdapter == null) {
            //nfc not supported by your advice
            Toast.makeText(getApplicationContext(), "NFC not supported by this device", Toast.LENGTH_LONG);
            this.finish();
            ;
        }

        if (!nAdapter.isEnabled()) {
            //nfc disabled
            Toast.makeText(getApplicationContext(), "NFC radio is off, please turn it on.", Toast.LENGTH_LONG);
            this.finish();
        }

        //wait for an nfc tag to be tapped to the device
        handleIntent(getIntent());

    }


    //nfc tag has been tapped to device
    public void handleIntent(Intent intent){

    }
}
