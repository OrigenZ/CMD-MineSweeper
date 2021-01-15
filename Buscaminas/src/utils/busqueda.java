package utils;

/**
 *
 * @author sherab
 */
public class busqueda {

    /**
     * Hace la busqueda con el método binario de un numero en una array ordenada,
     * devuelve un print con el numero buscado y la posición en la que se ha
     * encontrado.
     *
     * @param vector recibe un vector (array unidimensional)
     * @param numABuscar recibe el numero a buscar en el vector.
     */
    public static void busquedaBinariaDe(double[] vector, int numABuscar) {

        int izquierda, derecha, centro;
        izquierda = 0;
        derecha = vector.length - 1;
        centro = (derecha + izquierda) / 2;

        while (izquierda < derecha && vector[centro] != numABuscar) {

            if (vector[centro] > numABuscar) {
                derecha = centro - 1;
            } else {
                izquierda = centro + 1;
            }

            centro = (izquierda + derecha) / 2;
        }

        if (vector[centro] == numABuscar) {
            System.out.printf("Se ha encontrado el %d en la posición %d.", numABuscar, centro);
        }
    }

    /**
     * Busca el numero maximo dentro de una array unidimensional.
     *
     * @param array Recibe un vector
     * @return Devuelve el numero maximo dentro del vector
     */
    public static int maxNumDe(int[] array) {

        int max;
        max = array[0];

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;

    }

    /**
     * Busca el numero minimo dentro de una array unidimensional.
     *
     * @param array Recibe un vector
     * @return Devuelve el numero minimo dentro del vector
     */
    public static int minNumDe(int[] array) {

        int min;
        min = array[0];

        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;

    }
}
