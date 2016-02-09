package com.genericcorp.intervaltimer;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startTimer();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void startTimer() {
        final double startTime = GregorianCalendar.getInstance().getTimeInMillis();
        final Timer timer = new Timer();
        TextView intervalSecTime1 = (TextView) findViewById(R.id.int1Sec);
        TextView intervalSecTime2 = (TextView) findViewById(R.id.int2Sec);
        TextView intervalMinTime1 = (TextView) findViewById(R.id.int1Min);
        TextView intervalMinTime2 = (TextView) findViewById(R.id.int2Min);
        TextView intervalField = (TextView) findViewById(R.id.intervals);
        TextView startTimeView = (TextView) findViewById(R.id.textView4);
        final int intervalSec1 = Integer.parseInt(compensateForNull(intervalSecTime1)) * 1000;
        final int intervalSec2 = Integer.parseInt(compensateForNull(intervalSecTime2)) * 1000;
        final int intervalMin1 = Integer.parseInt(compensateForNull(intervalMinTime1)) * 60000;
        final int intervalMin2 = Integer.parseInt(compensateForNull(intervalMinTime2)) * 60000;
        final int intervals;
        final TextView timeView = (TextView) findViewById(R.id.timeView);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        final Stack counter1 = new Stack();

        //make sure there is something in there or default to 1
        if (intervalField.getText().length() < 1)
            intervals = 1;
        else
            intervals = Integer.parseInt(intervalField.getText().toString());

        for (int i = intervals * 2 + 1; i > 0; i--)
            counter1.push(i);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                timeView.setText("Canceled!");
            }
        });

        TimerTask task = new TimerTask() {
            private int counter = (int) counter1.pop();
            private Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            private Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            private MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.store_door);

            @Override
            public void run() {

                if (GregorianCalendar.getInstance().getTimeInMillis() > startTime + ((Math.ceil(counter / 2.0) * (intervalMin1 + intervalSec1)) + (counter / 2 * (intervalMin2 + intervalSec2)))) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (determineOdd(counter))
                                r.play();
                            else
                                mp.start();
                            counter = (int) counter1.pop();
                        }

                    });
                    if (counter >= intervals * 2) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeView.setText(GregorianCalendar.getInstance().getTime().toString() + " - Time's Up!");
                            }
                        });
                        timer.cancel();
                    }
                } else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeView.setText(DateFormat.getTimeInstance().format(GregorianCalendar.getInstance().getTime()));
                        }
                    });

            }
        };

        startTimeView.setText(DateFormat.getTimeInstance().format(startTime));
        timer.schedule(task, 0, 50);
    }

    private String compensateForNull(TextView tv){

        if (tv.getText().length() < 1)
            return "0";

        return tv.getText().toString();
    }

    private boolean determineOdd(int number) {
        if (number % 2 == 0)
            return false;
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.genericcorp.intervaltimer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.genericcorp.intervaltimer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
