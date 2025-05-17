import Pyro5.api
from credit_card_interface import CreditCardSystem

daemon = Pyro5.api.Daemon()
uri = daemon.register(CreditCardSystem)
print("URI del objeto:", uri)

ns = Pyro5.api.locate_ns()
ns.register("credit.card.system", uri)

print("Servidor en ejecución...")
daemon.requestLoop()
