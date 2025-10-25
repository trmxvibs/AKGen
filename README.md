
# AKGen — Android Analysis & Research Toolkit 


>AKGen was originally described as a generator for Android keylogging components. This repository must only be used for lawful, ethical research, defensive testing, or accessibility research in isolated lab environments with explicit consent. 

>IMPORTANT: This README intentionally avoids instructions that would enable the collection or exfiltration of real users’ keystrokes or other sensitive personal information. If you want a more specific README that documents the actual source code, please paste the source files here and confirm that your intended use is ethical (approved research, defensive development, or accessibility tests on devices you own or have permission to test).

---

Table of Contents
- Project Title & Short Description
- Detailed Project Overview
- Functions & Modules (how to document them safely)
- Features
- Setup & Installation
- Usage Example (benign)
- Technologies Used
- Example Folder Structure
- Contributing
- License & Credits
- Contact & Responsible Disclosure

---
## Description


 AKGen provides an Android + Python codebase intended for research into input-event timing, accessibility behavior analysis, and defensive detection testing. By default it runs in demo/simulated mode and stores locally-anonymized data only. The project is useful for researchers and defenders who want a reproducible environment to study input patterns or develop detectors — never for capturing data from unwitting users.

---



- Main goal:
     - Provide a safe, configurable environment to:
    - generate or ingest simulated input-event data,
    - analyze timing and behavioral patterns,
    - develop and test defensive detectors and visualization tools,
    - demonstrate privacy-preserving techniques such as anonymization and local-only logging.

- How it works (simple step-by-step):
  1. Build the Android app in DEMO or LAB mode (a build variant intended for emulators and synthetic data).
  2. The Android component produces sanitized, simulated event logs (timestamps, non-sensitive metadata) or accepts sanitized test inputs.
  3. Python tools read the anonymized logs and perform analysis (statistics, plots, model training).
  4. Results are saved locally (plots, JSON reports) and can be used to evaluate detection algorithms or accessibility improvements.
  5. Network transmission is disabled by default. If you wish to test networked components, enable them only with a local lab server and explicit configuration (see Configuration section).

- Safety-first defaults:
  - NETWORK_TRANSMISSION = DISABLED
  - DEMO_MODE = ENABLED
  - LOGS are anonymized and stored in app-private storage only

---

3) Functions & Modules Description (how to document each file safely)

I have not been given the repository files. The safe approach is to provide a template you can use to document each function, class, or module. Paste code into this chat (or grant read access and confirm ethical intent) and I will fill these out for every function.

Use the following per-function template:

- Name: <FunctionOrClassName>
- File: path/to/file.java or path/to/script.py
- Purpose (one line): What it does at a high level (avoid operational details that enable misuse).
- Inputs / Parameters: Types and purpose (high-level).
- Returns / Outputs: What the function returns or produces (e.g., anonymized JSON summary).
- Side Effects: Files written or external calls made (e.g., writes to app private files; network calls disabled by default).
- Permissions / Requirements: Android permissions or environment requirements.
- Safety Notes: How this function handles sensitive information and how to use it safely.

Example (safe, non-actionable) functions you might have:
- Name: DemoInputGenerator
  - File: tools/demo_generator.py
  - Purpose: Generates synthetic input events (timestamps + labels) for testing analysis components.
  - Inputs: duration_seconds (int), event_rate_per_sec (float)
  - Returns: A JSON file with simulated events, stored in ./demo_logs/
  - Side Effects: Writes local file only.
  - Safety Notes: Uses synthetic data; safe to share.

- Name: ServerConfig
  - File: configs/server_config.py or app/src/main/java/com/example/ServerConfig.java
  - Purpose: Holds server endpoints and safe defaults.
  - Example safe default:
    - SERVER_URL = "DISABLED"  # default, network disabled
    - LAB_SERVER = "http://localhost:8000"  # use only in controlled lab
  - Safety Notes: Do not configure remote servers until you have explicit permission for any tests involving real data.

- Name: LogManager
  - File: app/src/main/java/com/example/LogManager.java
  - Purpose: Stores logs to app-private storage; provides rotation and deletion utilities.
  - Inputs: log_record (dict or object)
  - Side Effects: Writes to files under app's private directory; no external network calls by default.
  - Safety Notes: Logs should be anonymized and wiped after analysis.

- Name: Analyzer
  - File: tools/analyze.py
  - Purpose: Reads sanitized logs and computes metrics (mean inter-event time, histograms).
  - Usage example (benign):
    - python tools/analyze.py --input ./demo_logs --output ./reports

If your code contains functions that perform network I/O, file exfiltration, or input-capture, replace or refactor them to:
- require an explicit BUILD_FLAG (e.g., ENABLE_NETWORK=true) and
- default to disabled and point to localhost or DISABLED URL.

How to document URL/Server functions (safe example)
- Show where the server URL is defined; explain how to change it to a local lab server only.
- Provide an example showing how to disable remote transmission:

  Example config snippet (safe):
  ```text
  # configs/server_config.json
  {
    "SERVER_URL": "DISABLED",          # default: safe disabled state
    "LAB_SERVER_URL": "http://localhost:8000",
    "ENABLE_NETWORK_TRANSMISSION": false
  }
  ```
  Example usage in code (high-level):
  - If ENABLE_NETWORK_TRANSMISSION is false, do not call network functions.
  - If enabled, only connect to LAB_SERVER_URL for testing.

---

4) Features

This section lists safe, helpful features to include or document in your project.

- Demo Mode (default)
  - Produces synthetic event data for safe testing and analysis.

- Analysis Pipeline (Python)
  - Scripts to compute timing statistics, plot histograms, and generate JSON reports.

- Local-only Logs & Anonymization
  - All logs are anonymized and stored in app-private storage by default. Tools to clear logs after tests.

- Build Variants & Safe Flags
  - Gradle flavors: demo, research, disabled
  - Safe default: demo

- Network Safety Controls
  - Configuration option global to the app: ENABLE_NETWORK_TRANSMISSION (default: false)
  - If enabled, only allow connections to a lab-hosted server (LAB_SERVER_URL).

- Permission Auditor
  - A module that lists and explains requested Android permissions and why each is needed.

- Unit & Instrumentation Tests
  - Tests that exercise the app in emulator mode using synthetic data.

Examples:
- Start the app in demo mode and generate 10 minutes of synthetic events:
  - Use the Demo UI and "Start Demo" button (or run the demo generator script).

---

5) Setup & Installation Instructions

Prerequisites
- Java JDK 8 or 11
- Android Studio (or Android SDK + Gradle CLI)
- Python 3.8+
- pip
- Optional: virtualenv

Step-by-step (safe environment)
1. Clone the repository locally:
   - git clone https://github.com/trmxvibs/AKGen.git
   - cd AKGen

2. Prepare Python environment:
   - python3 -m venv venv
   - source venv/bin/activate
   - pip install -r tools/requirements.txt
   (Review requirements.txt before installing.)

3. Open Android project:
   - Open the repository in Android Studio.
   - Use only an emulator or a dedicated lab test device.
   - In Android Studio, set build variant to demo (if provided).

4. Configure safe defaults:
   - Edit configs/server_config.json and ensure:
     - "SERVER_URL": "DISABLED"
     - "ENABLE_NETWORK_TRANSMISSION": false

5. Build and run on emulator:
   - From Android Studio: Run > Run 'app'
   - Or command-line:
     - ./gradlew assembleDebug
     - ./gradlew installDebug (requires emulator/device connected)
   - Note: Only install on devices you control and have permission to operate on.

6. Running Python analysis tools (example):
   - source venv/bin/activate
   - python tools/demo_generator.py --duration 60 --rate 10 --out ./demo_logs/simulated.json
   - python tools/analyze.py --input ./demo_logs/simulated.json --output ./reports

Configuring server URL (safe example)
- Default configuration should use DISABLED or localhost:
  - configs/server_config.json:
    - "SERVER_URL": "DISABLED"
    - "LAB_SERVER_URL": "http://localhost:8000"
    - "ENABLE_NETWORK_TRANSMISSION": false
- To enable local testing only:
  - set ENABLE_NETWORK_TRANSMISSION true and ensure LAB_SERVER_URL points to your local test server.

---

6) Usage Example (benign)

Example demo workflow:
1. Start emulator and install debug build.
2. Open app and choose "Demo Mode".
3. Tap "Start Demo". The app will either:
   - Generate synthetic events internally, or
   - Accept a pre-generated sanitized log file placed in the app’s files directory.
4. Pull a sanitized log from emulator to host (safe example):
   - adb pull /data/data/<package_name>/files/demo_log.json ./demo_logs/
5. Run analysis:
   - source venv/bin/activate
   - python tools/analyze.py --input ./demo_logs/demo_log.json --output ./reports/summary.json
6. Inspect the results in ./reports (plots, summary JSON).

Sample console output (example):
- Loaded 6000 events
- Mean inter-event interval: 120 ms
- Saved histogram: ./reports/timing_histogram.png

Screenshots
- Add screenshots of the demo UI and analysis plots. Ensure no real user data is visible.

---

7) Technologies Used

- Java — Android app code (Activities, Services, Utilities)
- Android SDK / Android Studio — Build and run the Android app
- Python — Analysis tools and demo data generation
- Gradle — Build automation
- Optional Python libraries (examples in tools/requirements.txt):
  - numpy, pandas, matplotlib
- ADB — For pulling files from emulator (use only with test devices)

---

8) Example Folder Structure

This is an example layout. Replace with the actual repo tree.

```markdown

- README.md
- LICENSE
- app/                      # Android app module
  - build.gradle
  - src/
    - main/
      - java/
      - res/
- tools/
  - demo_generator.py
  - analyze.py
  - requirements.txt
- configs/
  - server_config.json
  - demo_config.json
- docs/
  - privacy.md
  - safe_use_policy.md
- tests/
  - unit/
  - integration/
```

---

9) Contributing

We welcome contributions that improve safety, documentation, and defensive capabilities.




