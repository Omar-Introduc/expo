/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uni.aed.bloqueomortal;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa una transacción en un sistema de bases de datos concurrente.
 */
class Transaction {
    private String name;
    Set<String> locks;  // Registros bloqueados por la transacción
    Set<String> waitingFor;  // Transacciones a las que la transacción actual está esperando

    /**
     * Constructor de la clase Transaction.
     *
     * @param name Nombre único de la transacción.
     */
    public Transaction(String name) {
        this.name = name;
        locks = new HashSet<>();        // Conjunto de registros bloqueados por la transacción
        waitingFor = new HashSet<>();   // Conjunto de transacciones a las que la transacción actual está esperando
    }

    /**
     * Obtiene el nombre de la transacción.
     *
     * @return Nombre de la transacción.
     */
    public String getName() {
        return name;
    }

    /**
     * Bloquea un registro para esta transacción.
     *
     * @param record Registro que se va a bloquear.
     */
    public void lockRecord(String record) {
        locks.add(record);
    }

    /**
     * Desbloquea un registro previamente bloqueado por esta transacción.
     *
     * @param record Registro que se va a desbloquear.
     */
    public void unlockRecord(String record) {
        locks.remove(record);
    }

    /**
     * Agrega una transacción a la lista de transacciones a las que la transacción actual está esperando.
     *
     * @param transaction Nombre de la transacción a la que se espera.
     */
    public void addWaitingTransaction(String transaction) {
        waitingFor.add(transaction);
    }

    /**
     * Elimina una transacción de la lista de transacciones a las que la transacción actual está esperando.
     *
     * @param transaction Nombre de la transacción que ya no se espera.
     */
    public void removeWaitingTransaction(String transaction) {
        waitingFor.remove(transaction);
    }
}

