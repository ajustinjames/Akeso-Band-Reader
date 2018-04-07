package com.akeso.akesobandreader;


import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.nfc.Tag;
import android.nfc.tech.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


    private TextView[] displays;
    NfcAdapter nAdapter;
    Tag tag;
    Context context;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];


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

        //set up textviews so we can edit what they display
        displays = new TextView[8];
        displays[0] = (TextView) findViewById(R.id.name1);
        displays[1] = (TextView) findViewById(R.id.allergies1);
        displays[2] = (TextView) findViewById(R.id.medication1);
        displays[3] = (TextView) findViewById(R.id.conditions1);
        displays[4] = (TextView) findViewById(R.id.contact1);
        displays[5] = (TextView) findViewById(R.id.contact2);
        displays[6] = (TextView) findViewById(R.id.notes1);
        displays[7] = (TextView) findViewById(R.id.main);



    }


    @Override
    protected void onNewIntent(Intent intent){
        Toast.makeText(this, "NFC Tag Found", Toast.LENGTH_LONG).show();
        super.onNewIntent(intent);
    }


    @Override
    protected void onResume(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[] {};

        nAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter,
                null);

        super.onResume();
    }

    @Override
    protected void onPause() {

        nAdapter.disableForegroundDispatch(this);

        super.onPause();
    }





    private void displayScan(NdefMessage[] msgs){
        if (msgs == null || msgs.length == 0){
            return;
        }

        NdefRecord[] recs = msgs[0].getRecords();
        if (recs.length != 8){
            Toast.makeText(getApplicationContext(),"Non Akeso Band Scanned", Toast.LENGTH_LONG);
            return;
        }

        for (int i=0; i<8; i++){
            byte[] payload = recs[i].getPayload();
            String text = new String(payload);
            displays[i].setText(text);
        }
    }
}
