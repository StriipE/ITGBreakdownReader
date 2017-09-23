package com.stripe.itgbreakdownreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;

public class ReaderActivity extends AppCompatActivity {

    private BreakdownReader breakdownReader;
    private TextView currentMesuresDisplay;
    private TextView songNameDisplay;
    private TextView songBPMDisplay;
    private TextView currentStatus;

    private int mesuresLeftValue = 0;
    private boolean displaying = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        songNameDisplay = (TextView) findViewById(R.id.SongName);
        songBPMDisplay = (TextView) findViewById(R.id.bpmValue);
        currentStatus = (TextView) findViewById(R.id.currentStatus);

        songNameDisplay.setText("Division 4");
        songBPMDisplay.setText("180");

        readBreakdown("15 15 32 (16) 16 (32) 16 (26) 64");
        displayStream();

    }

    private void readBreakdown(String breakdown)
    {
        breakdownReader = new BreakdownReader(breakdown);
    }

    private void displayStream()
    {
        currentMesuresDisplay = (TextView) findViewById(R.id.currentMesures);
        int bpm = Integer.parseInt(songBPMDisplay.getText().toString());

        final long mesureDuration = 4 * 60000 / bpm;

        final Handler handler = new Handler();
        Runnable streamCountdown = new Runnable() {
            @Override
            public void run() {
               if (!breakdownReader.isEndOfSong() && mesuresLeftValue == 0) {
                   String nextBlock = breakdownReader.getNextBlock();
                   if (BreakdownReader.isBreak(nextBlock))
                   {
                       mesuresLeftValue = BreakdownReader.getBreakMesures(nextBlock);
                       mesuresLeftValue--; // Offset used because breaks are written in full duration
                       currentStatus.setText("Break");
                       handler.postDelayed(this, mesureDuration);
                       System.out.println("---- New pause : " + mesuresLeftValue + " mesures ----");
                   }
                   else
                   {
                       mesuresLeftValue = BreakdownReader.getNextMesures(nextBlock);
                       currentStatus.setText("Stream");
                       handler.postDelayed(this, mesureDuration);
                       System.out.println("---- New stream : " + mesuresLeftValue + " mesures ----");
                   }
               }
               else if (mesuresLeftValue > 0)
               {
                    mesuresLeftValue--;
                    handler.postDelayed(this, mesureDuration);
                    System.out.println("----" + mesuresLeftValue + " mesures left ----");
               }
               else
               {
                   handler.removeCallbacks(this);
                   System.out.println("End of song");
               }

                currentMesuresDisplay.setText(Integer.toString(mesuresLeftValue));
            }
        };

        handler.post(streamCountdown);
    }
}
