# 232CC232Project
<h3>Clase Transaction:</h3>

Representa una transacción con un nombre, un conjunto de registros bloqueados (locks), y un conjunto de transacciones a las que está esperando (waitingFor).
Tiene métodos para bloquear y desbloquear registros, así como para agregar y quitar transacciones a las que está esperando.
<h3>Clase DeadlockDetection:</h3>

Contiene un mapa de transacciones por nombre.
Tiene métodos para ejecutar transacciones a partir de una lista de comandos y para detectar deadlocks.
Las transacciones se ejecutan mediante comandos que especifican la operación ("read", "write", "end"), la transacción y el registro involucrado.
<h3>Método executeTransactions:</h3>

Itera sobre los comandos y ejecuta las operaciones correspondientes, manteniendo un registro de las transacciones en el mapa.
<h3>Métodos readRecord y writeRecord:</h3>

Realizan operaciones de lectura y escritura, bloqueando el registro y actualizando el grafo de espera.
<h3>Método endTransaction:</h3>

Finaliza una transacción, liberando los bloqueos y actualizando el grafo de espera para todas las transacciones restantes.
<h3>Método updateWaitingGraph:</h3>

Actualiza el grafo de espera cuando una transacción bloquea un registro.
<h3>Método detectDeadlock:</h3>

Utiliza el algoritmo de búsqueda en profundidad (DFS) para detectar ciclos en el grafo de espera de las transacciones. Si encuentra un ciclo, interrumpe la transacción más reciente en el ciclo.
<h3>Método findCycle:</h3>

Implementa el algoritmo DFS para encontrar ciclos en el grafo de espera.
<h3>Método getLastTransactionInCycle:</h3>

Obtiene la transacción más reciente en un ciclo dado.
<h3>Método main:</h3>

Ejemplo de uso que ejecuta una serie de comandos de transacción y luego detecta deadlocks.
En el ejemplo del método main, se ejecutan dos transacciones (T1 y T2) que leen y escriben en los registros A y B. Luego, se detecta si hay algún deadlock en el sistema. En este caso, el resultado impreso indicará si se encontró un punto muerto y cuál fue la transacción más reciente en el ciclo.

