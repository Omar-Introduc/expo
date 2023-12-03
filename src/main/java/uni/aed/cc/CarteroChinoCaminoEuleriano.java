package uni.aed.cc;

import java.util.ArrayList;
import java.util.List;

public class CarteroChinoCaminoEuleriano {

    private static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        // Definir los nombres de los lugares
        String[] lugares = {"A", "B", "C", "D", "E"};
        
        // Definir la matriz de adyacencia del grafo
        int[][] grafo = {
                {0, 10, 0, 30, 0},
                {10, 0, 20, 5, 10},
                {0, 20, 0, 0, 15},
                {30, 5, 0, 0, 20},
                {0, 10, 15, 20, 0}
        };

        System.out.println("Grafo:");
        imprimirGrafo(grafo, lugares);
        // Encontrar la ruta óptima utilizando el algoritmo del Cartero Chino
        List<Integer> rutaOptima = encontrarRutaOptima(grafo);
         // Obtener las indicaciones de la ruta óptima
        List<String> indicaciones = obtenerIndicaciones(rutaOptima, lugares);

        System.out.println("\nRuta óptima:");
        for (String indicacion : indicaciones) {
            System.out.println(indicacion);
        }
        //Obtenemos la suma de pesos de caminos para obtner la longitud minima.
        int longitudMinima = calcularLongitudMinima(grafo, rutaOptima);
        System.out.println("Longitud mínima del recorrido: " + longitudMinima);

    }
/**Parece que hay un problema en la función encontrarRutaOptima que afecta al momento de tener un camino de longitudes minimas, es decir
  considera a todos como 0, entonces para corroborar agregare una funcion del calculo de la longitud minima.
  Me he dado cuenta de que en esa función, la matriz grafo se está modificando durante el proceso del algoritmo, 
  lo que afecta el cálculo posterior de la longitud mínima. Una solucion para esto es hacer una copia de la matriz antes de
  que entre a la funcion euleriano.
     * */
    public static List<Integer> encontrarRutaOptima(int[][] grafo) {
        int n = grafo.length;
        int[][] matrizRecorrido = new int[n][n];
        int[][] copiaGrafo = copiarMatriz(grafo);
        // Construir la matriz de recorrido
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grafo[i][j] != 0 && grafo[i][j] != INF) {
                    matrizRecorrido[i][j] = j;
                } else {
                    matrizRecorrido[i][j] = -1;
                }
            }
        }

        List<Integer> verticesImpares = obtenerVerticesImpares(grafo);

        // Completar las distancias entre los vértices impares
        for (int k = 0; k < verticesImpares.size(); k++) {
            for (int i = 0; i < verticesImpares.size(); i++) {
                for (int j = 0; j < verticesImpares.size(); j++) {
                    int origen = verticesImpares.get(i);
                    int destino = verticesImpares.get(j);
                    if (grafo[origen][destino] != 0 && grafo[origen][destino] != INF && matrizRecorrido[origen][destino] == -1) {
                        int intermedio = verticesImpares.get(k);
                        int distancia = grafo[origen][intermedio] + grafo[intermedio][destino];
                        if (distancia < grafo[origen][destino]) {
                            grafo[origen][destino] = distancia;
                            matrizRecorrido[origen][destino] = matrizRecorrido[origen][intermedio];
                        }
                    }
                }
            }
        }

        // Encontrar la ruta óptima, consideramos la copia de la matriz añadida.
        List<Integer> rutaOptima = new ArrayList<>();
        int inicio = verticesImpares.get(0);
        euleriano(copiaGrafo, matrizRecorrido, inicio, rutaOptima);

        return rutaOptima;
    }
    /**
      se ha añadido una función copiarMatriz para crear una copia de la matriz
      grafo antes de pasarla a la función euleriano. De esta manera, evitamos
      que los cambios en la matriz afecten el cálculo de la longitud mínima.
     */
    public static int[][] copiarMatriz(int[][] matriz) {
        int n = matriz.length;
        int[][] copia = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matriz[i], 0, copia[i], 0, n);
        }
        return copia;
    }
    /**
     * Realiza el recorrido euleriano del grafo.
     *
     * @param grafo           Grafo representado como una matriz de adyacencia.
     * @param matrizRecorrido Matriz de recorrido que guarda los nodos intermedios del camino.
     * @param actual          Nodo actual en el recorrido.
     * @param rutaOptima      Lista que guarda la ruta óptima.
     */
    private static void euleriano(int[][] grafo, int[][] matrizRecorrido, int actual, List<Integer> rutaOptima) {
        int n = grafo.length;
        for (int i = 0; i < n; i++) {
            if (grafo[actual][i] != 0 && grafo[actual][i] != INF) {
                grafo[actual][i] = 0;
                grafo[i][actual] = 0;
                euleriano(grafo, matrizRecorrido, i, rutaOptima);
            }
        }
        rutaOptima.add(actual);
    }
    
    /**
     * Obtiene los vértices impares del grafo.
     *
     * @param grafo Grafo representado como una matriz de adyacencia.
     * @return Lista de vértices impares.
     */
    private static List<Integer> obtenerVerticesImpares(int[][] grafo) {
        List<Integer> verticesImpares = new ArrayList<>();
        for (int i = 0; i < grafo.length; i++) {
            int grado = 0;
            for (int j = 0; j < grafo.length; j++) {
                if (grafo[i][j] != 0 && grafo[i][j] != INF) {
                    grado++;
                }
            }
            if (grado % 2 != 0) {
                verticesImpares.add(i);
            }
        }
        return verticesImpares;
    }
     /**
     * Obtiene las indicaciones para cada arista en la ruta óptima.
     *
     * @param rutaOptima Ruta óptima representada como una lista de índices de lugares.
     * @param lugares    Nombres de los lugares en el grafo.
     * @return Lista de indicaciones.
     */
    private static List<String> obtenerIndicaciones(List<Integer> rutaOptima, String[] lugares) {
        List<String> indicaciones = new ArrayList<>();
        int n = rutaOptima.size();

        for (int i = 0; i < n - 1; i++) {
            int origen = rutaOptima.get(i);
            int destino = rutaOptima.get(i + 1);
            String indicacion = "Ir de " + lugares[origen] + " a " + lugares[destino];
            indicaciones.add(indicacion);
        }

        return indicaciones;
    }
    //se grego esta funcion para visualizar la matriz y obtener visualmente un representacion de los caminos.
     /**
     * Imprime el grafo en la consola.
     *
     * @param grafo   Grafo representado como una matriz de adyacencia.
     * @param lugares Nombres de los lugares en el grafo.
     */
    
    private static void imprimirGrafo(int[][] grafo, String[] lugares) {
        int n = grafo.length;

        System.out.print("    ");
        for (String lugar : lugares) {
            System.out.print(lugar + " ");
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.print(lugares[i] + " | ");
            for (int j = 0; j < n; j++) {
                if (grafo[i][j] == INF) {
                    System.out.print("∞ ");
                } else {
                    System.out.print(grafo[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    //Agregando esta funcion podemos estar seguros que el recorrido que hace es el correcto y considera los pesos de cada camino.
    public static int calcularLongitudMinima(int[][] grafo, List<Integer> rutaOptima) {
        int longitudMinima = 0;
        int n = rutaOptima.size();
        for (int i = 0; i < n - 1; i++) {
            int origen = rutaOptima.get(i);
            int destino = rutaOptima.get(i + 1);
            longitudMinima += grafo[origen][destino];
        }
        return longitudMinima;
    }

}
