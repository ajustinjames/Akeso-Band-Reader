package com.akeso.akesobandreader;

/**
 * Created by Aaron James on 3/18/2018.
 */

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends Activity {

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text1);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        } else {
            mTextView.setText(R.string.waiting);
        }

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            TagReader(tag);

        } else {
            //nothing to do
        }
    }

    public void TagReader(Tag tag){
        Ndef ndef = Ndef.get(tag);

        try {
            ndef.connect();

            Parcelable[] messages = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messages == null){
                //empty tag
                mTextView.setText("No information on this tag.");
            }

            NdefMessage[] ndefMessages = new NdefMessage[messages.length];
            for (int i = 0; i<messages.length; i++){
                ndefMessages[i] = (NdefMessage) messages[i];
            }
            NdefRecord record = ndefMessages[0].getRecords()[0];

            byte[] payload = record.getPayload();
            String text = new String(payload);
            mTextView.setText(text);
        } catch (Exception e){
            //Cannot read tag
        }
    }
}