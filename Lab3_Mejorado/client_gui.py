
import socket
import threading
import tkinter as tk
from tkinter import simpledialog, scrolledtext, messagebox
import sys

class ChatClientGUI:
    def __init__(self, username=None, host='localhost', port=1500):
        # Parámetros de conexión 
        self.host = host #Directorio del servidor
        self.port = port
        self.username = username
        self.client_socket = None
        self.running = True

        #  Paleta de colores 
        self.colors = {
            "background": "#f5f7fa",       # Fondo general claro
            "text_bg": "#ffffff",          # Fondo del chat
            "text_fg": "#1f2937",          # Texto normal
            "entry_bg": "#e2e8f0",         # Entrada de texto
            "button_bg": "#074E0A",        # Botón azul real
            "button_fg": "#ffffff",        # Texto de botón
            "highlight_fg": "#220B02",     # Naranja vibrante para títulos
            "msg_me_bg": "#d1fae5",        # Verde claro (mensajes propios)
            "msg_other_bg": "#fef3c7",     # Amarillo claro (otros)
        }

        self.fonts = {
            "title": ("Segoe UI", 14, "bold"),
            "normal": ("Segoe UI Emoji", 10),
            "button": ("Segoe UI", 10, "bold")
        }

        #  Ventana
        self.root = tk.Tk()
        self.root.title(f"💬 Chat - {self.username or 'Cliente'}")
        self.root.geometry("580x480")
        self.root.configure(bg=self.colors["background"])
        self.root.resizable(True, True)

        #  Título
        self.title_label = tk.Label(self.root, text="🌍 Chatea Conmigo",
                                    font=self.fonts["title"], bg=self.colors["background"],
                                    fg=self.colors["highlight_fg"])
        self.title_label.pack(pady=(10, 5))

        #  Área de chat
        self.chat_area = scrolledtext.ScrolledText(
            self.root,
            wrap=tk.WORD,
            state='disabled',
            bg=self.colors["text_bg"],
            fg=self.colors["text_fg"],
            font=self.fonts["normal"],
            relief="flat",
            height=16,
            bd=2
        )
        self.chat_area.pack(padx=20, pady=10, fill=tk.BOTH, expand=True)

        # 📥 Parte inferior
        self.bottom_frame = tk.Frame(self.root, bg=self.colors["background"])
        self.bottom_frame.pack(fill=tk.X, padx=20, pady=(0, 10))

        #  Entrada de mensaje
        self.msg_entry = tk.Entry(self.bottom_frame, font=self.fonts["normal"],
                                  bg=self.colors["entry_bg"], relief="flat")
        self.msg_entry.pack(side=tk.LEFT, padx=(0, 8), ipady=6, fill=tk.X, expand=True)
        self.msg_entry.bind("<Return>", self.send_message)

        #  Botón de enviar
        self.send_btn = tk.Button(self.bottom_frame, text="📨 Enviar",
                                  bg=self.colors["button_bg"], fg=self.colors["button_fg"],
                                  font=self.fonts["button"], command=self.send_message,
                                  activebackground="#1d4ed8", relief="flat", padx=12, pady=6)
        self.send_btn.pack(side=tk.LEFT)

        # Conectar
        self.connect_to_server()

        # Hilo de recepción
        threading.Thread(target=self.receive_messages, daemon=True).start()

        #  Menú
        self.create_menu()

        #  Cierre seguro
        self.root.protocol("WM_DELETE_WINDOW", self.on_close)
        self.root.mainloop()

    # Crear menú
    def create_menu(self):
        menu_bar = tk.Menu(self.root)
        self.root.config(menu=menu_bar)

        opciones = tk.Menu(menu_bar, tearoff=0)
        opciones.add_command(label="🧹 Limpiar Chat", command=self.clear_chat)
        opciones.add_separator()
        opciones.add_command(label="🔚 Salir", command=self.on_close)

        menu_bar.add_cascade(label="⚙ Opciones", menu=opciones)

    # Conectar al servidor
    def connect_to_server(self):
        if not self.username:
            self.username = simpledialog.askstring("Nombre de usuario", "Ingrese su nombre:", parent=self.root)
            if not self.username:
                messagebox.showerror("Error", "Nombre requerido")
                self.root.destroy()
                return
        
        try:
            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) # Se crea el socket del cliente
            self.client_socket.connect((self.host, self.port)) #se crea socket y se conecta al servidor 
            self.client_socket.sendall(self.username.encode()) # Enviar nombre de usuario al servidor
        except Exception as e:
            messagebox.showerror("Conexión fallida", f"No se pudo conectar:\n{e}")
            self.root.destroy()

    # Recibir mensajes del servidor
    def receive_messages(self):
        while self.running:
            try:
                message = self.client_socket.recv(1024).decode()
                if message:
                    self.display_message(message)
            except:
                break
    
    # Mostrar mensajes en el área de chat
    def display_message(self, message):
        self.chat_area.configure(state='normal')

        if message.startswith(f"{self.username}:"):
            self.chat_area.insert(tk.END, message + "\n", "me")
        else:
            self.chat_area.insert(tk.END, message + "\n", "other")

        self.chat_area.tag_config("me", background=self.colors["msg_me_bg"])
        self.chat_area.tag_config("other", background=self.colors["msg_other_bg"])

        self.chat_area.configure(state='disabled')
        self.chat_area.yview(tk.END)

    # Enviar mensaje al servidor
    # Si el mensaje es "LOGOUT", cierra la conexión
    def send_message(self, event=None):
        message = self.msg_entry.get().strip()
        if message:
            try:
                self.client_socket.sendall(message.encode())
                if message.upper() == "LOGOUT":
                    self.running = False
                    self.root.quit()
            except:
                messagebox.showerror("Error", "No se pudo enviar el mensaje.")
            self.msg_entry.delete(0, tk.END)

    # Limpiar el área de chat
    def clear_chat(self):
        self.chat_area.configure(state='normal')
        self.chat_area.delete(1.0, tk.END)
        self.chat_area.configure(state='disabled')

    # Cerrar la conexión y salir
    def on_close(self):
        try:
            self.client_socket.sendall("LOGOUT".encode())
        except:
            pass
        self.running = False
        self.client_socket.close()
        self.root.destroy()

# Ejecutar la GUI del cliente
if __name__ == "__main__":
    username = sys.argv[1] if len(sys.argv) > 1 else None
    ChatClientGUI(username)
