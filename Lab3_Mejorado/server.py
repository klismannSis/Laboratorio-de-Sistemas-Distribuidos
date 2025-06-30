import socket
import threading

class ClientThread(threading.Thread):
    def __init__(self, client_socket, client_address, server):
        threading.Thread.__init__(self)
        self.client_socket = client_socket
        self.client_address = client_address
        self.server = server
        self.username = None
        self.running = True

    def run(self):
        try:
            self.username = self.client_socket.recv(1024).decode()
            welcome_msg = f"*** {self.username} se ha unido al chat. ***"
            self.server.broadcast(welcome_msg, self)
            while self.running:
                data = self.client_socket.recv(1024).decode()
                if not data:
                    break
                if data == "LOGOUT":
                    self.server.remove_client(self)
                    self.running = False
                    break
                elif data == "WHOISIN":
                    self.send_message("Usuarios conectados:")
                    for client in self.server.clients:
                        self.send_message(f"- {client.username}")
                else:
                    message = f"{self.username}: {data}"
                    self.server.broadcast(message, self)
        except:
            pass
        finally:
            self.client_socket.close()

    def send_message(self, message):
        try:
            self.client_socket.sendall(message.encode())
        except:
            pass

class ChatServer:
    def __init__(self, host='localhost', port=1500):
        self.clients = []
        self.host = host
        self.port = port
        self.server_socket = None

    def start(self):
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.bind((self.host, self.port))
        self.server_socket.listen()
        print(f"Servidor iniciado en {self.host}:{self.port}")
        try:
            while True:
                client_socket, client_address = self.server_socket.accept()
                client_thread = ClientThread(client_socket, client_address, self)
                self.clients.append(client_thread)
                client_thread.start()
        except KeyboardInterrupt:
            print("Servidor apagado.")
        finally:
            self.server_socket.close()

    def broadcast(self, message, sender):
        print(message)
        for client in self.clients:
            if client != sender:
                client.send_message(message)

    def remove_client(self, client):
        if client in self.clients:
            self.clients.remove(client)
            msg = f"*** {client.username} salió del chat. ***"
            self.broadcast(msg, client)

if __name__ == "__main__":
    server = ChatServer()
    server.start()
