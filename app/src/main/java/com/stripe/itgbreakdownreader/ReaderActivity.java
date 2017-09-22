package com.stripe.itgbreakdownreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;

public class ReaderActivity extends AppCompatActivity {

    private BreakdownReader breakdownReader;
    private TextView currentMesuresDisplay;
    private int mesuresLeftValue = 0;
    private boolean displaying = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        readBreakdown("15 15 (7) 15");
        displayStream();

    }

    private void readBreakdown(String breakdown)
    {
        breakdownReader = new BreakdownReader(breakdown);
    }

    private void displayStream()
    {
        currentMesuresDisplay = (TextView) findViewById(R.id.currentMesures);
        final Handler handler = new Handler();
        Runnable streamCountdown = new Runnable() {
            @Override
            public void run() {
               if (!breakdownReader.isEndOfSong() && mesuresLeftValue == 0) {
                   String nextBlock = breakdownReader.getNextBlock();
                   if (BreakdownReader.isBreak(nextBlock))
                   {
                       mesuresLeftValue = BreakdownReader.getBreakMesures(nextBlock);
                       handler.postDelayed(this, 200);
                       System.out.println("---- New pause : " + mesuresLeftValue + " mesures ----");
                   }
                   else
                   {
                       mesuresLeftValue = BreakdownReader.getNextMesures(nextBlock);
                       handler.postDelayed(this, 200);
                       System.out.println("---- New stream : " + mesuresLeftValue + " mesures ----");
                   }
               }
               else if (mesuresLeftValue > 0)
               {
                    mesuresLeftValue--;
                    handler.postDelayed(this, 200);
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
