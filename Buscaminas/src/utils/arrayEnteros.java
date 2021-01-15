package utils;

public class arrayEnteros {

    /**
     * Introduce un valor en una posición de un array de enteros, desplazando el
     * valor existente y todos los que hay acontinuacion a la derecha, no se
     * borra nungún valor.
     *
     * @param array Array de enteros original
     * @param pos Posicion donde hacer la insercion
     * @param v Valor a inertar
     * @return Nuevo array de enteros con el valor insertado, ahora su medida se
     * ha incrementado en uno, Si hay un error se evalua a null
     */
    public int[] insertar(int[] array, int pos, int v) {

        if ((pos >= 0) && (pos < array.length)) {
            int[] nuevoArray = new int[array.length + 1];
            copiarNPosiciones(array, nuevoArray, pos);
            nuevoArray[pos] = v;
            for (int i = pos; i < array.length; i++) {
                nuevoArray[i + 1] = array[i];
            }
            return nuevoArray;
        } else {
            return null;
        }
    }

    /**
     * Borra un valor en una posición de un array de enteros, desplazando los
     * valores, sobre la posición eliminada, hacia la izquierda.
     *
     * @param array Array de enteros original
     * @param pos Posición a borrar
     * @return Nuevo array de enteros con el valor eliminado. Ahora su medida se
     * ha reducido en uno. Si hay un error se evalua a null.
     */
    public int[] borrar(int[] array, int pos) {

        if ((pos >= 0) && (pos < array.length)) {
            int[] nouArray = new int[array.length - 1];
            copiarNPosiciones(array, nouArray, pos);
            for (int i = pos + 1; i < array.length; i++) {
                nouArray[i - 1] = array[i];
            }
            return nouArray;
        } else {
            return null;
        }
    }

    /**
     * Dados dos vectores de enteros, copia los N primeros valores de el origen, a
     * la destinacion, comenzando por la posicion 0.
     *
     * @param origen Array de origen
     * @param desti Array de destino
     * @param n Numero de valors a copiar
     */
    public void copiarNPosiciones(int[] origen, int[] desti, int n) {
        for (int i = 0; i < n; i++) {
            desti[i] = origen[i];
        }
    }

    
    /**
     * Recibe un array y printea su contenido formateado.
     * 
     * @param array recibe un array 
     */
    public void mostrarArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.printf("%3d", array[i]);
        }
    }
}
