import Pyro5.api

# Buscar el objeto remoto por nombre
credit_system = Pyro5.api.Proxy("PYRONAME:credit.card.system")

# Pruebas
print(credit_system.create_account("Juan", 500))
print(credit_system.charge("Juan", 100))
print(credit_system.get_balance("Juan"))

print(credit_system.charge("Juan", 1000))  # Debería rechazar por fondos insuficientes
print(credit_system.get_balance("Juan"))
