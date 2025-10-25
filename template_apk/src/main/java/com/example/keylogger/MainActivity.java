package com.example.demokeylogger; // ← change your package name
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText inputEditText;
    private Button submitButton, clearButton;
    private TextView logTextView;
    private ScrollView logScroll;

    // In-memory log store (only lives while app is running)
    private final ArrayList<String> logs = new ArrayList<>();

    // Formatter for timestamps
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // ensure this matches your XML filename

        // Bind UI elements
        inputEditText = findViewById(R.id.inputEditText);
        submitButton = findViewById(R.id.submitButton);
        clearButton = findViewById(R.id.clearButton);
        logTextView = findViewById(R.id.logTextView);
        logScroll = findViewById(R.id.logScroll);

        // Initially clear logs view
        refreshLogView();

        // 1) TextWatcher: logs every change inside this EditText (educational only)
        inputEditText.addTextChangedListener(new TextWatcher() {
            // beforeTextChanged not used but required by interface
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            // onTextChanged is called while user types — we capture the latest value
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Avoid logging every single keystroke noise: here we capture meaningful changes.
                // For demo, we'll log the full current text each time (you may adjust granularity).
                String current = s.toString();
                if (current.length() > 0) {
                    addLog("Typed (live): \"" + current + "\"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // 2) Submit button — logs the current text when user presses Submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputEditText.getText().toString();
                addLog("Submitted: \"" + text + "\"");
                // Optionally clear input after submit:
                // inputEditText.setText("");
            }
        });

        // 3) Clear logs button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logs.clear();
                refreshLogView();
            }
        });
    }

    /**
     * Adds a log entry with current timestamp, updates UI.
     * This stores logs only in memory; nothing is written to disk.
     */
    private void addLog(String message) {
        String ts = sdf.format(new Date());
        String entry = ts + " — " + message;
        logs.add(entry);
        refreshLogView();
    }

    /**
     * Refreshes the TextView showing logs and scrolls to bottom.
     */
    private void refreshLogView() {
        if (logs.isEmpty()) {
            logTextView.setText("Logs will appear here...");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String l : logs) {
                sb.append(l).append("\n\n");
            }
            logTextView.setText(sb.toString());
            // scroll to bottom to show latest log
            logScroll.post(new Runnable() {
                @Override
                public void run() {
                    logScroll.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }
}
