//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Face-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.microsoft.projectoxford.face.samples.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Locale;
import java.util.ArrayList;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Handler;

import com.microsoft.projectoxford.face.samples.R;
import com.microsoft.projectoxford.face.samples.persongroupmanagement.PersonGroupListActivity;

public class MainActivity extends AppCompatActivity {

    private static final int VR_REQUEST = 999;

    public TextToSpeech prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button helpBtn = (Button) findViewById(R.id.btnHelp);
        helpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btnHelp) {
                    //listen for results

                    prompt.setLanguage(Locale.US);
                    //prompt.speak("Say 'search', to search around you. Or, say 'friend', to add a friend!", TextToSpeech.QUEUE_FLUSH, null);
                    prompt.speak("Say 'search' or say 'friend'!", TextToSpeech.QUEUE_FLUSH, null);

                    listenToSpeech();
                }
            }
        });

        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

        prompt = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                }
                else if (status == TextToSpeech.ERROR) {
                }
            }
        });
    }

    public void listenToSpeech() {

        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say 'search' to search around you. Or, say 'add' to add a friend!");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        //specify number of results to retrieve
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);

        final Intent tempIntent = listenIntent;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 0.5s = 500ms
                startActivityForResult(tempIntent, VR_REQUEST);
            }
        }, 2500);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check speech recognition result
        String answer = "three";

        if (requestCode == VR_REQUEST && resultCode == RESULT_OK) {
            //store the returned word list as a String
            ArrayList<String> userAnswer = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //set the retrieved list to display in the ListView using an ArrayAdapter

            //TextView t = (TextView)findViewById(R.id.textView);
            //t.setText(userAnswer.get(0));

            answer = userAnswer.get(0);
        }

        Log.v("ADAM_MCNEILLY", "Speech: " + answer);

        if (answer.toLowerCase().equals("friend")){
            Intent intent = new Intent(this, PersonGroupListActivity.class);
            startActivity(intent);
            this.finish();
        }
        else if (answer.toLowerCase().equals("search")){
            Intent intent = new Intent(this, VerificationMenuActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void detection(View view) {
        Intent intent = new Intent(this, DetectionActivity.class);
        startActivity(intent);
    }

    public void verification(View view) {
        Intent intent = new Intent(this, VerificationMenuActivity.class);
        startActivity(intent);
    }

    public void identification(View view) {
        //Intent intent = new Intent(this, IdentificationActivity.class);
        //startActivity(intent);

        Intent intent = new Intent(this, PersonGroupListActivity.class);
        startActivity(intent);
    }
}
