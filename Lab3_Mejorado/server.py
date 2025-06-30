import socket
import threading
import mysql.connector
from mysql.connector import Error

# Clase para manejar cada cliente conectado al servidor
class ClientThread(threading.Thread):
    def __init__(self, client_socket, client_address, server):
        threading.Thread.__init__(self)
        self.client_socket = client_socket
        self.client_address = client_address
        self.server = server
        self.username = None
        self.running = True
    
    # Este método maneja la recepción de mensajes del cliente y la lógica del chat.
    
    def run(self):
        try:
            self.username = self.client_socket.recv(1024).decode()
            # Registrar usuario en la BD
            self.server.register_user(self.username)
            welcome_msg = f"*** {self.username} se ha unido al chat. ***"
            self.server.broadcast(welcome_msg, self)

            # Recibir mensajes del cliente 
            while self.running:
                data = self.client_socket.recv(1024).decode()
                if not data:
                    break

                if data == "LOGOUT": # cierra la conexión 
                    self.server.remove_client(self)
                    self.running = False
                    break
                elif data == "WHOISIN":
                    self.send_message("Usuarios conectados:")
                    for client in self.server.clients:
                        self.send_message(f"- {client.username}")
                else:
                    message = f"{self.username}: {data}"
                    self.server.save_message(self.username, data)
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

# Clase servidor de chat
class ChatServer:
    def __init__(self, host='localhost', port=1500):
        self.clients = []
        self.host = host
        self.port = port
        self.server_socket = None
        # Conexión a la base de datos
        try:
            self.db = mysql.connector.connect(
                host='localhost',
                user='root',
                password='root',
                database='chatdb'
            )
            self.cursor = self.db.cursor()
            self.create_tables()
        except Error as e:
            print(f"Error al conectar a la base de datos: {e}")
            self.db = None
            self.cursor = None

    def create_tables(self):
        # Crear tabla de usuarios
        self.cursor.execute('''CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(255) UNIQUE NOT NULL
        )''')
        # Crear tabla de mensajes
        self.cursor.execute('''CREATE TABLE IF NOT EXISTS messages (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(255) NOT NULL,
            message TEXT NOT NULL,
            timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )''')
        self.db.commit()

    def register_user(self, username):
        try:
            self.cursor.execute("INSERT IGNORE INTO users (username) VALUES (%s)", (username,))
            self.db.commit()
        except Error as e:
            print(f"Error al registrar usuario: {e}")

    def save_message(self, username, message):
        try:
            self.cursor.execute("INSERT INTO messages (username, message) VALUES (%s, %s)", (username, message))
            self.db.commit()
        except Error as e:
            print(f"Error al guardar mensaje: {e}")

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
            if self.db:
                self.cursor.close()
                self.db.close()

    # MODIFICADO: ahora también envía al remitente
    def broadcast(self, message, sender):
        print(message)
        for client in self.clients:
            client.send_message(message)

    def remove_client(self, client):
        if client in self.clients:
            self.clients.remove(client)
            msg = f"*** {client.username} salió del chat. ***"
            self.broadcast(msg, client)

if __name__ == "__main__":
    server = ChatServer()
    server.start()
