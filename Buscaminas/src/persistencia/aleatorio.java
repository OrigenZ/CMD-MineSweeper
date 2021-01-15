package persistencia;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import Buscaminas.Jugador;
import static utils.lectura.leerMenu;
import static utils.strings.contieneIgnoreCase;

public class aleatorio {

    /**
     * Este metodo recibe un objeto tipo RandomAccessFile y escribe 15 veces las
     * posiciones en "Blank" si el archivo binario que contiene las puntuaciones
     * esta vacio
     *
     * @param raf Random Access File que apunta al binario que contiene las
     * puntuaciones
     * @param NOMLEN recibe el tamaño maximo del String que compondrá el nombre
     */
    public static void inizializaLeaderboard(RandomAccessFile raf, int NOMLEN) {

        try {

            for (int i = 0; i < 15; i++) {

                escribirString(raf, "Empty", NOMLEN);
                raf.writeInt(0);
                raf.writeLong(0L);
            }

            raf.close();
        } catch (IOException e) {

            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Recibe un objeto Jugador (record) que registra la puntuacion que ha
     * obtenido ese jugador en la partida, el registro sera escrito al final del
     * archivo binario, el archivo binario luego es ordenado de manera
     * descendente, dejando las mejores puntuaciones al principio, y eliminando
     * los ultimos registros si estos sobrepasan 15.
     *
     * @param record recibe un objeto Jugador con el registro de la partida
     * @param FILEPATH El String con el full PATH al archivo que guarda los
     * datos del jugador
     * @param REGLEN La longitud FIJA de memoria que puede ocupar un registro en
     * bytes
     * @param NOMLEN La longitud máxima del string que comprondra el nombre del
     * jugador.
     */
    public static void leaderboard(Jugador record, String FILEPATH, int REGLEN, int NOMLEN) {

        RandomAccessFile raf;
        int maxRecords = 15 * REGLEN;

        try {

            raf = new RandomAccessFile(new File(FILEPATH), "rw");

            //Escribimos la nueva entrada al final del archivo
            raf.seek(raf.length());
            escribirString(raf, record.nombre, NOMLEN);
            raf.writeInt(record.puntuacion);
            raf.writeLong(record.fecha);

            //Ordenamos puntuaciones descendentemente
            sortDescBinaryFile(FILEPATH, REGLEN, NOMLEN);

            //Eliminamos el ultimo registro.
            raf.setLength(maxRecords);

            raf.close();

        } catch (IOException e) {

            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Metodo void que lee el archivo binario donde se almacenan las
     * puntuaciones y lo printea con un determinado formato, permite visualizar
     * Jugadores por nombre de jugador, el nombre puede ser parcial o total.
     *
     * @param FILEPATH El String con el full PATH al archivo que guarda los
     * datos del jugador
     * @param REGLEN La longitud FIJA de memoria que puede ocupar un registro en
     * bytes
     * @param NOMLEN La longitud máxima del string que comprondra el nombre del
     * jugador.
     */
    public static void printLeaderboard(String FILEPATH, int REGLEN, int NOMLEN) {

        RandomAccessFile raf;
        int opcion;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date fecha;

        try {

            System.out.println(BLANCO_FONDO + "--------------------------------" + RESET
                    + VERDE + "\n|                              |\n"
                    + "| 1·MOSTRAR JUGADOR/ES" + RESET + "         |"
                    + AZUL_FUERTE + "\n|                              |\n"
                    + "| 2·VER TABLA DE PUNTUACIONES" + RESET + "  |"
                    + BLANCO_FONDO + "\n|                              |\n"
                    + "--------------------------------" + RESET);
            System.out.print("\nOPCIÓN: > ");

            opcion = leerMenu(1, 2, "\nIntroduce una opción válida.\n\nOPCIÓN: > ");

            if (opcion == 1) {
                lecturaRegistroAleatoria(FILEPATH, REGLEN, NOMLEN);
            } else {

                raf = new RandomAccessFile(new File(FILEPATH), "r");
                System.out.println("\n\n\n" + BLANCO_FONDO + "•-•-•-•-•-•-•-•-•-•-•-•-•LEADERBOARD•-•-•-•-•-•-•-•-•-•-•-•-•-•\n" + RESET);

                int i = 0;
                while (raf.getFilePointer() < raf.length()) {

                    Jugador record = new Jugador();
                    record.nombre = leerString(raf, NOMLEN);
                    record.puntuacion = raf.readInt();
                    record.fecha = raf.readLong();

                    fecha = new Date(record.fecha);

                    if (i < 9) {
                        System.out.printf("0%s· %-31s %-3s pts.   %-3s\n\n", i + 1, record.nombre, record.puntuacion, df.format(fecha));
                    } else {
                        System.out.printf("%s· %-31s %-3s pts.   %-3s\n\n", i + 1, record.nombre, record.puntuacion, df.format(fecha));
                    }

                    i++;
                }

                System.out.println(BLANCO_FONDO + "•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•-•\n" + RESET);

                System.out.println("");
                raf.close();
            }

        } catch (IOException e) {

            System.err.println("Error: " + e.getMessage());

        }
    }

    /**
     * Metodo para leer strings a través del RandomAccesFile, el método recibe
     * la longitud maxima que tendrá el String a leer, lee los carácteres uno
     * por uno, y si no son null los almacena en un String, luego devuelve el
     * String.
     *
     * @param raf Recibe un RandomAccessFile cargado con el File que contiene la
     * ruta al archivo del que se lee el String
     * @param maxChars Máximo numero de chars que serán leidos.
     * @return
     */
    public static String leerString(RandomAccessFile raf, int maxChars) {

        String cadena = "";
        char caracter;

        try {
            for (int i = 0; i < maxChars; i++) {

                caracter = raf.readChar();

                if (caracter != 0) {
                    cadena += caracter;
                }
            }
        } catch (Exception ex) {

            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();

        }
        return cadena;
    }

    /**
     * Método para escribir strings en un archivo Binario através del
     * RandomAccessFile que apunta a el archivo donde se escribiran los datos,
     * escribe los carácteres uno por uno, tomando como referencia la longitud
     * maxima que podrá ocupar de la variable "maxNomLength" en el binario, si
     * es mayor es truncada, y si es menor se rellena con 0s representando Null
     * values, de modo que el String siempre ocupe el mismo espacio en Bytes en
     * el archivo.
     *
     * @param raf RandomAccessFile apuntando al archivo donde necesitamos
     * realizar la escritura
     * @param nombre El String a ser escrito en el binario.
     * @param maxNomLength La longitud FIJA que ocupará el String en memoria.
     */
    public static void escribirString(RandomAccessFile raf, String nombre, int maxNomLength) {

        int longitudNombre;

        try {

            longitudNombre = nombre.length();
            if (longitudNombre > maxNomLength) {
                longitudNombre = maxNomLength;
            }

            for (int i = 0; i < longitudNombre; i++) {
                raf.writeChar(nombre.charAt(i));
            }

            char Null = 0;
            for (int i = 0; i < (maxNomLength - longitudNombre); i++) {
                raf.writeChar(Null);
            }

        } catch (Exception ex) {

            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para ordenar de mayor a menor los datos en un archivo binario,
     * toma como referencia los valores del atributo Puntuacion del Objeto
     * Jugador.
     *
     * @param FILEPATH String con el Full Path al archivo binario a ordenar.
     * @param REGLEN La longitud FIJA que ocupa cada registro en la memoria del
     * archivo binario
     * @param NOMLEN La longitud maxima del String a leer, como atributo Nombre
     * de el objeto Jugador.
     */
    public static void sortDescBinaryFile(String FILEPATH, int REGLEN, int NOMLEN) {

        RandomAccessFile raf;

        try {

            raf = new RandomAccessFile(new File(FILEPATH), "rw");

            Jugador leftRecord;
            Jugador rightRecord;

            for (int i = 0; i < raf.length() - REGLEN; i += REGLEN) {

                for (int j = 0; j < raf.length() - REGLEN; j += REGLEN) {

                    leftRecord = new Jugador();
                    rightRecord = new Jugador();

                    raf.seek(j); // pos 0

                    // de 0 a 72   
                    leftRecord.nombre = leerString(raf, NOMLEN);
                    leftRecord.puntuacion = raf.readInt();
                    leftRecord.fecha = raf.readLong();

                    // de 72 144
                    rightRecord.nombre = leerString(raf, NOMLEN);
                    rightRecord.puntuacion = raf.readInt();
                    rightRecord.fecha = raf.readLong();

                    if (leftRecord.puntuacion < rightRecord.puntuacion) {

                        raf.seek(raf.getFilePointer() - (REGLEN * 2));// de 144 a 0

                        escribirString(raf, rightRecord.nombre, NOMLEN);//de 0 a 72
                        raf.writeInt(rightRecord.puntuacion);
                        raf.writeLong(rightRecord.fecha);

                        escribirString(raf, leftRecord.nombre, NOMLEN);// de 72 a 144
                        raf.writeInt(leftRecord.puntuacion);
                        raf.writeLong(leftRecord.fecha);
                    }
                }
            }
            raf.close();
        } catch (Exception ex) {

            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para leer un registro de medida fija en un archivo binario de
     * manera no secuencial, toma como referencia el atributo Nombre del objeto
     * jugador, mostrara todas las coincidencias totales o parciales ignorando
     * Mayúsculas, para esto hace uso de el método "contieneIgnoreCase".
     *
     *
     * @param FILEPATH String con La ruta absoluta al archivo a ser leido.
     * @param REGLEN La longitud FIJA que tiene cada registro en el archivo
     * binario.
     * @param NOMLEN La longitud máxima que podrá tener el String leido, si es
     * superada el string será truncado.
     */
    public static void lecturaRegistroAleatoria(String FILEPATH, int REGLEN, int NOMLEN) {

        Jugador registro;
        boolean encontrado = false;
        String nombre;
        int cantidadJugadores;
        Scanner input = new Scanner(System.in);

        try {

            RandomAccessFile raf = new RandomAccessFile(new File(FILEPATH), "r");

            cantidadJugadores = (int) (raf.length() / REGLEN);
            System.out.print("\nNombre del Jugador: ");
            nombre = input.nextLine();

            raf.seek(0);

            for (int i = 0; i < cantidadJugadores; i++) {
                registro = new Jugador();

                registro.nombre = leerString(raf, NOMLEN);
                registro.puntuacion = raf.readInt();
                registro.fecha = raf.readLong();

                if (contieneIgnoreCase(registro.nombre, nombre) == true) {
                    mostrarRegistro(registro);
                    encontrado = true;
                }
            }
            if (encontrado == false) {

                System.err.printf("\n\nEl jugador %s no existe.\n\n", nombre);
            }
            raf.close();
        } catch (Exception ex) {

            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     * Metodo void que recibe un objeto de tipo Jugador y printea sus
     * caracteristicas en un formato dado.
     *
     * @param registro Objeto tipo Jugador con los atributos:
     * @(String)nombre , (int)puncuacion, (String)fecha
     */
    public static void mostrarRegistro(Jugador registro) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date fecha = new Date(registro.fecha);

        System.out.println("\n\n════════════════════════════════\n");
        System.out.printf("Nombre: %s\n", registro.nombre);
        System.out.printf("Puntuación: %s \n", registro.puntuacion);
        System.out.printf("Fecha: %s", df.format(fecha));
        System.out.println("\n\n════════════════════════════════");
    }
    /*
     * Declaracion de constantes globales con los valores de color escapados.
     */
    // Reset
    static final String RESET = "\033[0m";  // Reset de texto

    // Colores normales
    static final String NEGRO = "\033[0;30m";   // NEGRO
    static final String ROJO = "\033[0;31m";     // ROJO
    static final String VERDE = "\033[0;32m";   // VERDE
    static final String AMARILLO = "\033[0;33m";  // AMARILLO
    static final String AZUL = "\033[0;34m";    // AZUL
    static final String LILA = "\033[0;35m";  // LILA
    static final String CYAN = "\033[0;36m";    // CYAN
    static final String BLANCO = "\033[0;37m";   // BLANCO

    // Fuerte
    static final String NEGRO_FUERTE = "\033[1;37m";  // NEGRO_FUERTE
    static final String ROJO_FUERTE = "\033[1;31m";    // ROJO_FUERTE
    static final String VERDE_FUERTE = "\033[1;32m";  // VERDE_FUERTE
    static final String AMARILLO_FUERTE = "\033[1;33m"; // AMARILLO_FUERTE
    static final String AZUL_FUERTE = "\033[1;34m";   // AZUL_FUERTE
    static final String LILA_FUERTE = "\033[1;35m"; // LILA_FUERTE
    static final String CYAN_FUERTE = "\033[1;36m";   // CYAN_FUERTE
    static final String BLANCO_FUERTE = "\033[1;30m";  // BLANCO_FUERTE

    // Fondo
    //static final String NEGRO_FONDO = "\033[40m";  // NEGRO_FONDO
    //static final String ROJO_FONDO = "\033[41m";    // ROJO_FONDO 
    static final String VERDE_FONDO = "\033[42m";  // VERDE_FONDO
    //static final String AMARILLO_FONDO = "\033[43m"; // AMARILLO_FONDO
    //pstatic final String AZUL_FONDO = "\033[44m";   // AZUL_FONDO
    //static final String LILA_FONDO = "\033[45m"; // LILA_FONDO
    static final String CYAN_FONDO = "\033[46m";   // CYAN_FONDO
    static final String BLANCO_FONDO = "\033[47m";  // BLANCO_FONDO
}
