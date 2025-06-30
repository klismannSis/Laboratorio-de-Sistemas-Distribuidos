import subprocess
import sys
import os
import time

def run_client(name):
    if sys.platform.startswith('win'):
        # Windows
        subprocess.Popen(["python", "client_gui.py", name], creationflags=subprocess.CREATE_NEW_CONSOLE)
    elif sys.platform.startswith('linux') or sys.platform.startswith('darwin'):
        # Linux / macOS
        subprocess.Popen(["x-terminal-emulator", "-e", f"python3 client_gui.py {name}"])
    else:
        print("Sistema no compatible para abrir múltiples terminales.")

if __name__ == "__main__":
    run_client("Usuario1")
    time.sleep(1)  # Pequeña pausa para separación visual
    run_client("Usuario2")





