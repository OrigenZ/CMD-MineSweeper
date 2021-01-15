/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author sherab
 */
public class ordenacion {

    /**
     * Metodo que reordena los digitos de un numero entero en orden descendente,
     * formando el numero mas alto posible..
     *
     * @param numero Recibe un numero entero
     * @return devuelve el numero entero reordenado descendentemente.
     */
    public static int ordenaIntDesc(int numero) {

        String num = Integer.toString(numero);
        String numOrdenado = "";

        int[] vectorNum = new int[num.length()];

        for (int i = 0; i < vectorNum.length; i++) {

            vectorNum[i] = (num.charAt(i) - '0');

        }

        int aux;

        bubbleDescWhile(vectorNum);

        for (int i = 0; i < vectorNum.length; i++) {
            numOrdenado += vectorNum[i];
        }

        numero = Integer.parseInt(numOrdenado);

        return numero;
    }

    /**
     * Metodo para revertir un integer
     *
     * @param numero recibe un integer
     * @return devuelte el integer revertido
     */
    public static int revertirNum(int numero) {
        int numRevert = 0;

        while (numero != 0) {
            int digito = numero % 10;
            numRevert = numRevert * 10 + digito;
            numero /= 10;
        }
        return numRevert;
    }

    /**
     * Ordena un vector en orden descendente, version optimizada, cuando la
     * condicion if no se cumple, el boolean retorna false, ysale de la
     * iteracion automaticamente
     *
     * @param numsVector recibe un vector de numeros
     * @return Devuelve un vector de integers ordenado descendentemente.
     */
    public static double[] bubbleDescFor(double[] numsVector) {

        boolean cambiado = true;
        double temp;
        for (int i = numsVector.length - 1; cambiado && i >= 0; i--) {
            cambiado = false;
            for (int j = 0; j < i; j++) {
                if (numsVector[j] < numsVector[j + 1]) {
                    temp = numsVector[j];
                    numsVector[j] = numsVector[j + 1];
                    numsVector[j + 1] = temp;
                    cambiado = true;
                }
            }
        }
        return numsVector;
    }

    /**
     * Ordena un vector en orden ascendente, version optimizada, cuando la
     * condicion if no se cumple, el boolean retorna false, y sale de la
     * iteracion automaticamente
     *
     * @param numsVector
     * @return devuelve un vector de doubles.
     */
    public static double[] bubbleAscFor(double[] numsVector) {

        boolean cambiado = true;
        double temp;
        for (int i = numsVector.length - 1; cambiado && i >= 0; i--) {
            cambiado = false;
            for (int j = 0; j < i; j++) {
                if (numsVector[j] > numsVector[j + 1]) {
                    temp = numsVector[j];
                    numsVector[j] = numsVector[j + 1];
                    numsVector[j + 1] = temp;
                    cambiado = true;
                }
            }
        }
        return numsVector;
    }

    /**
     * Ordena un vector en orden descendente, version optimizada con WHILE,
     * cuando la condicion if no se cumple, el boolean retorna false, y sale de
     * la iteracion automaticamente
     *
     * @param numsVector recibe un vector de integers.
     * @return retorna un vector de integers.
     */
    public static int[] bubbleDescWhile(int[] numsVector) {

        int temp;
        boolean ordenado = false;

        int veces = numsVector.length;
        while (veces > 0 && ordenado == false) {
            ordenado = true;
            for (int j = 0; j < numsVector.length - 1; j++) {

                if (numsVector[j] < numsVector[j + 1]) {
                    temp = numsVector[j];
                    numsVector[j] = numsVector[j + 1];
                    numsVector[j + 1] = temp;
                    ordenado = false;
                }
            }
            veces--;
        }
        return numsVector;
    }

    public static int[] bubbleAscWhile(int[] numsVector) {

        int temp;
        boolean ordenado = false;

        int veces = numsVector.length;
        while (veces > 0 && ordenado == false) {
            ordenado = true;
            for (int j = 0; j < numsVector.length - 1; j++) {

                if (numsVector[j] > numsVector[j + 1]) {
                    temp = numsVector[j];
                    numsVector[j] = numsVector[j + 1];
                    numsVector[j + 1] = temp;
                    ordenado = false;
                }
            }
            veces--;
        }
        return numsVector;
    }
}
