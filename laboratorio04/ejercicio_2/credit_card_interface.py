import Pyro5.api

@Pyro5.api.expose
class CreditCardSystem:
    def __init__(self):
        self.accounts = {}  # formato: {usuario: saldo}

    def create_account(self, user, balance):
        if user in self.accounts:
            return f"La cuenta de {user} ya existe."
        self.accounts[user] = balance
        return f"Cuenta creada para {user} con saldo S/{balance}."

    def get_balance(self, user):
        if user not in self.accounts:
            return "Cuenta no encontrada."
        return f"Saldo de {user}: S/{self.accounts[user]}"

    def charge(self, user, amount):
        if user not in self.accounts:
            return "Cuenta no encontrada."
        if self.accounts[user] < amount:
            return "Fondos insuficientes."
        self.accounts[user] -= amount
        return f"Compra realizada. Nuevo saldo: S/{self.accounts[user]}"
