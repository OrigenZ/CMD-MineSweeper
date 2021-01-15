/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import Buscaminas.Jugador;

/**
 *
 * @author voidxy
 */
public class secuencial {

    /**
     * Método para guardar los caracteres contenidos en la array de chars que
     * conforman los tableros de juego, tanto el backend como el frontend, el
     * método hace uso de PrintWriter en lugar de Scanner ya que el tablero de
     * juego frontend contiene caracteres en blanco ' ', el metodo escribe en el
     * archivo de texto primero: la puntuacion obtenida durante la partida y
     * seguidamente un espacio, segundo: la cantidad de filas y un espacio y la
     * cantidad de columnas y un espacio, tercero: los caracteres que conforman
     * los tableros, Los dos arrays serán guardados en el mismo archivo de texto
     * de manera alterna, es decir, B/F/B/F/B/F.
     *
     *
     * @param FILEPATH
     * @param backend
     * @param frontend
     * @param puntuacionTotal
     */
    public static void guardarTablero(String FILEPATH, char[][] backend, char[][] frontend, int puntuacionTotal) {

        PrintWriter pw = null;
        File partida;

        try {

            //Declaramos la ruta al archivo txt y el printer.
            partida = new File(FILEPATH);
            pw = new PrintWriter(partida);

            //obtenemos las medidas del array
            int numFilas = backend.length;
            int numColumnas = backend[0].length;

            //Printeamos la puntuacion total al momento de guardar
            pw.print(puntuacionTotal);
            pw.print(' ');

            //Printeamos en documento las medidas del array
            pw.print(numFilas);
            pw.print(' ');
            pw.print(numColumnas);
            pw.print(' ');

            //Printeamos el contenido del array backend y frontend
            //de manera alterna b/f/b/f/b/f ...
            for (int i = 0; i < backend.length; i++) {
                for (int j = 0; j < backend[i].length; j++) {
                    pw.write(backend[i][j]);
                    pw.write(frontend[i][j]);
                }
            }

        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
            ex.printStackTrace();

        } finally {

            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * Este metodo recupera los datos escritos en un archivo de texto y los
     * escribe en una array de chars,lee los caracteres de la puntuacion (esta
     * se lee atraves de un metodo diferente) tansolo para poder acceder a los
     * siguientes datos, que son el numero de filas y el numero de columnas, el
     * FileReader lee el primer dato y comprueba si el siguiente es un espacio
     * en blanco o lo que es lo mismo un -1, si no es un espacio en blanco
     * significa que son las decenas del numero que representa las
     * filas/columnas, por lo que multiplica las unidades por 10 y le suma las
     * decenas, dependiendo del valor boolean que le pasemos imprimira solo los
     * valores B o F en el patron B/F/B/F/B/F y omitira el resto
     *
     *
     *
     * @param FILEPATH String con la ruta absoluta a el archivo a leer
     * @param opcion Valor booleano que definira que seccion de los datos será
     * impresa, si es TRUE sera el Backend, si es FALSE sera el Frontend.
     * @return Devuelve un array de chars que contendra los datos leidos y
     * tendra el tamaño que sea especificado despues de la lectura de el
     * numFilas y numColumnas
     */
    public static char[][] recuperarTablero(String FILEPATH, boolean opcion) {

        FileReader fr;
        File partida;
        char[][] tablero = new char[0][0];
        int auxiliar;

        try {

            partida = new File(FILEPATH);
            fr = new FileReader(partida);

            //Skip la primera lectura de caracteres ya que son
            //los valores de la puntuacion
            auxiliar = Character.getNumericValue(fr.read());
            auxiliar = Character.getNumericValue(fr.read());
            if (auxiliar != -1) {
                auxiliar = Character.getNumericValue(fr.read());
            }

            //obtenemos el numero de filas
            int numFilas = Character.getNumericValue(fr.read());
            int filaDecena = Character.getNumericValue(fr.read());
            if (filaDecena != -1) {
                numFilas = (numFilas * 10) + filaDecena;
                auxiliar = Character.getNumericValue(fr.read());
            }

            //obtenemos el numero de columnas
            int numColumnas = Character.getNumericValue(fr.read());
            int columnaDecena = Character.getNumericValue(fr.read());
            if (columnaDecena != -1) {
                numColumnas = (numColumnas * 10) + columnaDecena;
                auxiliar = Character.getNumericValue(fr.read());
            }

            tablero = new char[numFilas][numColumnas];
            //Recuperar el tablero backend
            if (opcion == true) {

                for (int i = 0; i < numFilas; i++) {
                    for (int j = 0; j < numColumnas; j++) {

                        tablero[i][j] = (char) fr.read();
                        fr.skip(1);
                    }
                }

                //Recuperar el tablero frontend
            } else if (opcion == false) {

                for (int i = 0; i < numFilas; i++) {
                    for (int j = 0; j < numColumnas; j++) {

                        fr.skip(1);
                        tablero[i][j] = (char) fr.read();
                    }
                }
            }

            fr.close();

        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
            ex.printStackTrace();

        }
        return tablero;
    }

    /**
     * Metodo para leer la puntuacion del archivo de texto que contiene los
     * datos del tablero de la partida guardada, estos son los primeros digitos
     * que contiene el archivo.
     *
     *
     * @param FILEPATH string con la ruta absoluta al archivo a leer.
     * @return devuelve un integer con la puntuacion obtenida
     */
    public static int recuperarPuntuacion(String FILEPATH) {
        Jugador record = new Jugador();
        int auxiliar;

        FileReader fr;

        try {
            fr = new FileReader(new File(FILEPATH));
            record.puntuacion = Character.getNumericValue(fr.read());
            auxiliar = Character.getNumericValue(fr.read());
            while (auxiliar != -1) {
                record.puntuacion = (record.puntuacion * 10) + auxiliar;
                System.out.println("Puntuacion: " + record.puntuacion);
                auxiliar = Character.getNumericValue(fr.read());
            }
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
            ex.printStackTrace();

        }

        return record.puntuacion;
    }
}

//㤷
