package com.akeso.akesobandreader;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import android.nfc.NdefMessage;

public class MainActivity extends AppCompatActivity {


    private TextView[] displays;
    NfcAdapter nAdapter;

    //set up the view by checking NFC of the device and catching the textviews
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        nAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nAdapter == null) {
            //nfc not supported by your advice
            Toast.makeText(getApplicationContext(), "NFC not supported by this device", Toast.LENGTH_LONG).show();
            this.finish();
            ;
        }

        if (!nAdapter.isEnabled()) {
            //nfc disabled
            Toast.makeText(getApplicationContext(), "NFC radio is off, please turn it on.", Toast.LENGTH_LONG).show();
            this.finish();
        }

        //set up textviews so we can edit what they display
        displays = new TextView[10];
        displays[0] = findViewById(R.id.name1);
        displays[1] = findViewById(R.id.allergies1);
        displays[2] = findViewById(R.id.conditions1);
        displays[3] = findViewById(R.id.medications1);
        displays[4] = findViewById(R.id.contact1);
        displays[5] = findViewById(R.id.contact2);
        displays[6] = findViewById(R.id.insurance1);
        displays[7] = findViewById(R.id.insurance2);
        displays[8] = findViewById(R.id.notes1);
        displays[9] = findViewById(R.id.main);



    }

    //handles when an nfc tag is tapped to the phone
    @Override
    protected void onNewIntent(Intent intent){
        Toast.makeText(this, "Akeso Band Found", Toast.LENGTH_SHORT).show();
        super.onNewIntent(intent);
        Parcelable[] raw = intent.getParcelableArrayExtra(nAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msg = null;
        if(raw != null){
            msg = new NdefMessage[raw.length];
            for (int i=0; i< raw.length; i++){
                msg[i] = (NdefMessage) raw[i];
            }
        }

        displayScan(msg);
    }

    //resumes nfc watching when navigating back to the app
    @Override
    protected void onResume(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[] {};
        nAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
        super.onResume();
    }

    //pauses nfc when leaving the app
    @Override
    protected void onPause() {
        nAdapter.disableForegroundDispatch(this);
        super.onPause();
    }




    //responsible for reading the scanned nfc tag, making sure its an Akeso band, and then displaying the information scanned
    private void displayScan(NdefMessage[] msgs){
        if (msgs == null || msgs.length == 0){
            return;
        }

        NdefRecord[] recs = msgs[0].getRecords();
        if (recs.length != 9){
            Toast.makeText(getApplicationContext(),"Non Akeso Band Scanned", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i=0; i<9; i++){
            byte[] payload = recs[i].getPayload();
            String text = new String(payload);
            text = text.substring(3,text.length());
            displays[i].setText(text);
            displays[i].setBackgroundResource(R.color.highlight);
        }

        Date date = new Date();
        String scan = "Scanned: " + date.toString();
        displays[9].setText(scan);

    }

    //handles the clear button
    public void toClear(View v){
        clear();
    }

    //clears the textviews
    public void clear(){
        for (int i = 0; i<9; i++){
            displays[i].setText("");
            displays[i].setBackgroundResource(R.color.whit);
        }
        displays[9].setText(R.string.waiting);
    }
}
