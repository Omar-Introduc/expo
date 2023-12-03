/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package uni.aed.bloqueomortal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author omar
 */
public class Main {

    /**
     * Método principal que demuestra el uso de la detección de puntos muertos con un conjunto de comandos de transacción.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        DeadlockDetection deadlockDetection = new DeadlockDetection();
        List<String> commands = new ArrayList<>();
        commands.add("read,T1,A");
        commands.add("read,T2,A");
        commands.add("write,T2,B");
        commands.add("write,T1,B");
        commands.add("end,T1");
        commands.add("end,T2");
        deadlockDetection.executeTransactions(commands);
        deadlockDetection.detectDeadlock();
    }
    
}
