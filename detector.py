# find Accessibility Service
import subprocess
import re

def check_accessibility_services():
    """
    Uses ADB to check for enabled accessibility services on a connected device.
    """
    print("Connecting to device via ADB...")
    try:
        # Run the ADB command to get enabled services
        # This command dumps the "accessibility" service's state
        result = subprocess.run(
            ['adb', 'shell', 'dumpsys', 'accessibility'],
            capture_output=True,
            text=True,
            check=True
        )
        
        output = result.stdout
        
        print("\n--- [ Enabled Accessibility Services ] ---")
        
        # Regex to find lines like "Service: ComponentInfo{...}"
        # This pattern might need adjustment based on Android version
        service_pattern = re.compile(r"Service:\s+ComponentInfo\{([^/]+)/")
        
        enabled_services = []
        
        # A simpler way to check is to look for 'mEnabledServices'
        for line in output.splitlines():
            if 'mEnabledServices' in line:
                # Extract service names, which are often in [com.package/com.service] format
                services = re.findall(r'\[(.*?)\]', line)
                if services:
                    enabled_services.extend(services[0].split(', '))

        if not enabled_services:
            # Fallback for different Android versions
            # Look for ComponentInfo lines that are part of the service list
            # This is a bit more complex, let's refine
            
            # A more reliable method:
            services_result = subprocess.run(
                ['adb', 'shell', 'settings', 'get', 'secure', 'enabled_accessibility_services'],
                capture_output=True,
                text=True,
                check=True
            )
            
            enabled_list = services_result.stdout.strip()
            
            if enabled_list:
                print("Found enabled services:")
                services = enabled_list.split(':')
                for i, service in enumerate(services):
                    print(f"  {i+1}. {service}")
                print("---------------------------------------------")
                print("WARNING: Review these services. Unknown services")
                print("could pose a security risk.")
            else:
                print("No enabled accessibility services found.")
                print("---------------------------------------------")

        
    except FileNotFoundError:
        print("\n[ERROR] 'adb' command not found.")
        print("Please ensure ADB (Android Debug Bridge) is installed and in your system's PATH.")
    except subprocess.CalledProcessError as e:
        if "no devices/emulators found" in e.stderr:
            print("\n[ERROR] No Android device or emulator found.")
            print("Please connect a device and enable USB Debugging.")
        else:
            print(f"\n[ERROR] ADB command failed: {e.stderr}")

if __name__ == "__main__":
    check_accessibility_services()
