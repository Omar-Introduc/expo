package uni.aed.bloqueomortal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase que implementa la detección de puntos muertos en un sistema de transacciones y registros.
 */
public class DeadlockDetection {
    private Map<String, Transaction> transactions;  // Mapa de transacciones por nombre

    /**
     * Constructor de la clase DeadlockDetection.
     * Inicializa el mapa de transacciones.
     */
    public DeadlockDetection() {
        transactions = new HashMap<>();
    }

    /**
     * Ejecuta las transacciones dadas en el orden proporcionado.
     *
     * @param commands Lista de comandos de transacción en el formato "operación,transacción,registro"
     */
    public void executeTransactions(List<String> commands) {
        for (String command : commands) {
            String[] parts = command.split(",");
            String transactionName = parts[0].trim();

            if (!transactions.containsKey(transactionName)) {
                Transaction transaction = new Transaction(transactionName);
                transactions.put(transactionName, transaction);
            }

            Transaction transaction = transactions.get(transactionName);
            String operation = parts[1].trim();

            if (operation.equals("read")) {
                String record = parts[2].trim();
                readRecord(transaction, record);
            } else if (operation.equals("write")) {
                String record = parts[2].trim();
                writeRecord(transaction, record);
            } else if (operation.equals("end")) {
                endTransaction(transaction);
            }
        }
    }

    /**
     * Realiza la operación de lectura de un registro por una transacción, bloqueando el registro y actualizando el grafo de espera.
     *
     * @param transaction Transacción que realiza la operación de lectura
     * @param record      Registro que se lee
     */
    private void readRecord(Transaction transaction, String record) {
        transaction.lockRecord(record);
        updateWaitingGraph(transaction, record);
    }

    /**
     * Realiza la operación de escritura en un registro por una transacción, bloqueando el registro y actualizando el grafo de espera.
     *
     * @param transaction Transacción que realiza la operación de escritura
     * @param record      Registro que se escribe
     */
    private void writeRecord(Transaction transaction, String record) {
        transaction.lockRecord(record);
        updateWaitingGraph(transaction, record);
    }

    /**
     * Finaliza una transacción, liberando los bloqueos y actualizando el grafo de espera.
     *
     * @param transaction Transacción que se finaliza
     */
    private void endTransaction(Transaction transaction) {
        transactions.remove(transaction.getName());

        // Liberar los candados y actualizar el grafo de espera
        for (Transaction t : transactions.values()) {
            t.unlockRecord(transaction.getName());
            t.removeWaitingTransaction(transaction.getName());
        }
    }

    /**
     * Actualiza el grafo de espera cuando una transacción bloquea un registro.
     *
     * @param transaction Transacción que bloquea el registro
     * @param record      Registro bloqueado por la transacción
     */
    private void updateWaitingGraph(Transaction transaction, String record) {
        for (Transaction t : transactions.values()) {
            if (t != transaction && t.locks.contains(record)) {
                t.addWaitingTransaction(transaction.getName());
            }
        }
    }

    /**
     * Detecta si hay un punto muerto en las transacciones y muestra la transacción más reciente en el ciclo.
     * Si no se encuentra ningún punto muerto, se muestra un mensaje indicando que no se encontraron puntos muertos.
     */
    void detectDeadlock() {
        Set<Transaction> visited = new HashSet<>();
        Set<Transaction> inProgress = new HashSet<>();

        for (Transaction transaction : transactions.values()) {
            if (!visited.contains(transaction)) {
                Set<Transaction> cycle = findCycle(transaction, visited, inProgress, new HashSet<>());
                if (cycle != null) {
                    // Se ha encontrado un ciclo, se interrumpe la transacción más reciente en el ciclo
                    Transaction lastTransaction = getLastTransactionInCycle(cycle);
                    System.out.println("¡Punto muerto detectado! Se interrumpe la transacción " + lastTransaction.getName());
                    return;
                }
            }
        }

        System.out.println("No se encontraron puntos muertos.");
    }

    /**
     * Encuentra un ciclo en el grafo de espera utilizando el algoritmo de búsqueda en profundidad (DFS).
     *
     * @param transaction Transacción actual en el recorrido DFS
     * @param visited     Conjunto de transacciones visitadas en el recorrido DFS
     * @param inProgress  Conjunto de transacciones en progreso en el recorrido DFS
     * @param cycle       Conjunto temporal para almacenar las transacciones en el ciclo
     * @return Conjunto de transacciones en el ciclo o null si no se encontró ningún ciclo
     */
    private Set<Transaction> findCycle(Transaction transaction, Set<Transaction> visited, Set<Transaction> inProgress, Set<Transaction> cycle) {
        visited.add(transaction);
        inProgress.add(transaction);

        for (String waitingTransactionName : transaction.waitingFor) {
            Transaction waitingTransaction = transactions.get(waitingTransactionName);

            if (inProgress.contains(waitingTransaction)) {
                // Se ha encontrado un ciclo
                cycle.add(waitingTransaction);
                return cycle;
            }

            if (!visited.contains(waitingTransaction)) {
                Set<Transaction> subCycle = findCycle(waitingTransaction, visited, inProgress, cycle);
                if (subCycle != null) {
                    subCycle.add(waitingTransaction);
                    return subCycle;
                }
            }
        }

        inProgress.remove(transaction);
        return null;
    }

    /**
     * Obtiene la transacción más reciente en el ciclo.
     *
     * @param cycle Conjunto de transacciones en el ciclo
     * @return Transacción más reciente en el ciclo
     */
    private Transaction getLastTransactionInCycle(Set<Transaction> cycle) {
        Transaction lastTransaction = null;
        for (Transaction transaction : cycle) {
            if (lastTransaction == null || transaction.getName().compareTo(lastTransaction.getName()) > 0) {
                lastTransaction = transaction;
            }
        }
        return lastTransaction;
    }
}
