# client.py

import socket
import threading

class ChatClient:
    def __init__(self, host='localhost', port=1500):
        self.host = host
        self.port = port
        self.username = input("Introduzca su nombre de usuario: ")
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def receive_messages(self):
        while True:
            try:
                message = self.client_socket.recv(1024).decode()
                if message:
                    print(f"\n{message}")
                else:
                    break
            except:
                break
    

    def start(self):
        try:
            self.client_socket.connect((self.host, self.port))
            self.client_socket.sendall(self.username.encode())
            print("Conectado al servidor de chat.")
            threading.Thread(target=self.receive_messages, daemon=True).start()
            while True:
                message = input()
                if message:
                    self.client_socket.sendall(message.encode())
                    if message == "LOGOUT":
                        break
        except Exception as e:
            print(f"Se produjo un error: {e}")
        finally:
            self.client_socket.close()

if __name__ == "__main__":
    client = ChatClient()
    client.start()
