package com.example.ethicalwarningapp; // ← your package name here

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AlertDialog.Builder 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("🛑 Ethical Warning / नैतिक चेतावनी");
        builder.setMessage("This app is for educational purposes only.\n\n" +
                "Installing this on a device you do not own is illegal and unethical. " +
                "Press 'Agree' only if you own this device and are testing for " +
                "learning purposes.\n\n" +
                "क्या आप सहमत हैं कि आप इस ऐप का उपयोग केवल शैक्षिक उद्देश्यों के लिए " +
                "अपने स्वयं के डिवाइस पर कर रहे हैं?");

        builder.setPositiveButton("Agree (मैं सहमत हूँ)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Disagree (असहमत हूँ)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

       
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
