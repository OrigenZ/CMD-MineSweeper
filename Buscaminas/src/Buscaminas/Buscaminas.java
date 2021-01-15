package Buscaminas;

import java.io.*;
import java.util.*;
import static persistencia.aleatorio.*;
import static persistencia.secuencial.guardarTablero;
import static persistencia.secuencial.recuperarPuntuacion;
import static persistencia.secuencial.recuperarTablero;
import static utils.lectura.leerInt;
import static utils.lectura.leerMenu;

public class Buscaminas {

    static final int NOMLEN = 30;
    static final int REGLEN = NOMLEN * 2 + 4 + 8; // 30*2 + 10*2 + 4 = 84
    static final String BINPATH = "src" + File.separator + "Leaderboard" + File.separator + "mejoresPuntuaciones.bin";
    static final String FILEPATH = "src" + File.separator + "PartidasGuardadas" + File.separator + "partida.txt";

    public static void main(String[] args) {

        try {
            Jugador record;
            boolean exit;
            int menu;

            //Inicializamos el Leaderboard en caso de estar vacio. Max posiciones == 15
            RandomAccessFile raf = new RandomAccessFile(new File(BINPATH), "rw");
            if (raf.length() == 0) {
                inizializaLeaderboard(raf, NOMLEN);
            }
            raf.close();

            //menu de juego
            do {
                //Instanciamos objeto Jugador
                exit = false;
                record = new Jugador();

                //MENU DE OPCIONES
                System.out.println("\n\n    -B·U·S·C·A·M·I·N·A·S-   \n\n");
                System.out.println(BLANCO_FONDO + "------------MENÚ------------" + RESET
                        + VERDE + "\n|                          |\n"
                        + "| 1·MODO FÁCIL" + RESET + "             |"
                        + AMARILLO + "\n|                          |\n"
                        + "| 2·MODO DIFÍCIL" + RESET + "           |"
                        + AZUL_FUERTE + "\n|                          |\n"
                        + "| 3·MODO PERSONALIZADO" + RESET + "     |"
                        + LILA + "\n|                          |\n"
                        + "| 4·LEADERBOARD" + RESET + "            |"
                        + CYAN + "\n|                          |\n"
                        + "| 5·CARGAR PARTIDA" + RESET + "         |"
                        + ROJO + "\n|                          |\n"
                        + "| 6·SALIR" + RESET + "                  |"
                        + BLANCO_FONDO + "\n|                          |\n"
                        + "----------------------------" + RESET);
                System.out.print("\nOPCIÓN: > ");

                //Comprobamos que la opcion de menu sea valida atraves del metodo leerMenu
                menu = leerMenu(1, 6, ROJO_FUERTE + "\nPOR FAVOR, INTRODUCE UNA OPCIÓN DE MENÚ VÁLIDA: " + RESET);

                //Printeamos saltos de linea
                System.out.println("\n\n\n\n");

                //Switch menu principal
                switch (menu) {
                    case 1:
                        if (menu1(record) == false) {
                            leaderboard(record, BINPATH, REGLEN, NOMLEN);
                        }
                        break;
                    case 2:
                        if (menu2(record) == false) {
                            leaderboard(record, BINPATH, REGLEN, NOMLEN);
                        }
                        break;
                    case 3:
                        if (menu3(record) == false) {
                            leaderboard(record, BINPATH, REGLEN, NOMLEN);
                        }
                        break;
                    case 4:
                        printLeaderboard(BINPATH, REGLEN, NOMLEN);
                        break;
                    case 5:
                        if (menu4(record) == false) {
                            leaderboard(record, BINPATH, REGLEN, NOMLEN);
                        }
                        break;
                    case 6:
                        exit = true;
                        System.out.println("\n\n     HASTA LA PRÓXIMA!\n\n\n\n\n\n");
                        break;
                }

            } while (!exit);
            System.out.println();

        } catch (Exception ex) {

            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Metodo para distribuir minas en el tablero de juego de manera random,
     * recibe como parametros el array del tablero, el tamaño del
     * tablero(cantFilas y cantColumnas), la cantidad de minas a generar y la
     * fila y la columna del primer input del usuario, representando las
     * coordenadas de la casilla apartir de la cual se generaran las bombas
     * aleatoriamente, respetando un radio de 3x3.
     *
     * @param tableroBackend Recibe un array de chars
     * @param cantidadFilas Recibe un int representando la altura del grid
     * (cantidad de filas)
     * @param cantidadColumnas Recibe un int representando la anchura del grid
     * (cantidad de columnas)
     * @param cantMinas Recibe un int representando la cantidad de minas a
     * distribuir
     * @param fila Recibe un int representando la fila del primer input del
     * usuario
     * @param columna Recibe un int representando la columna del primer input
     * del usuario
     * @return Devuelve el tablero con minas colocadas aleatoriamente.
     */
    public static char[][] plantarMinasEn(char[][] tableroBackend, int cantidadFilas, int cantidadColumnas, int cantMinas, int fila, int columna) {

        Random rand = new Random();
        int fil;
        int col;
        //Llenamos el tablero con posiciones vacias ('/')
        tableroBackend = marcarPosisVacias(tableroBackend);

        //Marcamos la posicion base y sus 8 casillas adyacentes apartir de la cual se genera el tablero. Representadas por el caracter '*'
        //Posicion de origen
        tableroBackend[fila][columna] = '*';

        //Iter Norte
        if (fila > 0) {
            tableroBackend[fila - 1][columna] = '*';
        }

        //Iter Nor-este
        if (fila > 0 && columna < tableroBackend[fila].length - 1) {
            tableroBackend[fila - 1][columna + 1] = '*';
        }

        //Iter Este
        if (columna < tableroBackend[fila].length - 1) {
            tableroBackend[fila][columna + 1] = '*';
        }

        //Iter Sur-este
        if (columna < tableroBackend[fila].length - 1 && fila < tableroBackend.length - 1) {
            tableroBackend[fila + 1][columna + 1] = '*';
        }

        //Iter Sur
        if (fila < tableroBackend.length - 1) {
            tableroBackend[fila + 1][columna] = '*';
        }

        //Iter Sur-oeste
        if (fila < tableroBackend.length - 1 && columna > 0) {
            tableroBackend[fila + 1][columna - 1] = '*';
        }

        //Iter Oeste
        if (columna > 0) {
            tableroBackend[fila][columna - 1] = '*';
        }

        //Iter Nor-oeste
        if (fila > 0 && columna > 0) {
            tableroBackend[fila - 1][columna - 1] = '*';
        }

        //asignamos posicion de minas de manera aleatoria
        do {
            fil = rand.nextInt(cantidadFilas);
            col = rand.nextInt(cantidadColumnas);

            if (tableroBackend[fil][col] == '/') {
                tableroBackend[fil][col] = '■';
                cantMinas--;
            }
        } while (cantMinas > 0);

        //limpiamos los asteriscos y los devolvemos a posiciones vacias '/'.
        for (char[] tablero1 : tableroBackend) {
            for (int j = 0; j < tablero1.length; j++) {
                if (tablero1[j] == '*') {
                    tablero1[j] = '/';
                }
            }
        }

        return tableroBackend;
    }

    /**
     * Recibe un Array(tablero) de chars y lo llena con posiciones "en Blanco"
     * representadas por '/'.
     *
     * @param tablero Recibe un array de chars
     * @return Devuelve el tablero con todas las posiciones llenas con '/'
     */
    public static char[][] marcarPosisVacias(char[][] tablero) {

        for (char[] tablero1 : tablero) {
            for (int j = 0; j < tablero1.length; j++) {
                tablero1[j] = '/';
            }
        }

        return tablero;
    }

    /**
     * Recibe el tablero de juego(Back-end), itera sobre el y busca y almacena
     * las posiciones de las minas en otro array.
     *
     * @param tableroBackend recibe un Array de chars
     * @return devuelve un Array de integers
     */
    public static int[][] getPosicionMinas(char[][] tableroBackend) {

        int[][] posicionMinas;
        int cantidadMinas = 0;
        int aux = 0;

        //Contamos las minas dentro del Array para simplificar el metodo y evitar tener que pasarle un segundo parametro
        for (char[] tablero1 : tableroBackend) {
            for (int j = 0; j < tablero1.length; j++) {
                if (tablero1[j] == '■') {
                    cantidadMinas++;
                }
            }
        }

        //Inicializamos el Array con la cantidad de posiciones de minas a almacenar
        posicionMinas = new int[cantidadMinas][2];

        //Llenamos el array con las posiciones
        for (int j = 0; j < tableroBackend.length; j++) {
            for (int k = 0; k < tableroBackend[j].length; k++) {

                if (tableroBackend[j][k] == '■') {

                    posicionMinas[aux][0] = j;
                    posicionMinas[aux][1] = k;
                    aux++;

                }
            }
        }
        return posicionMinas;
    }

    /**
     * Recibe el tablero de juego (Array) con las minas ya distribuidas, recibe
     * tambien un array con las posiciones de las minas, establece la numeracion
     * alrededor de las minas segun la cantidad de minas adyacentes encontrdas
     * alrededor de dichas casillas en un area de 3x3.
     *
     * @param tableroBackend Recibe un array bidimensional de chars
     * @param posicionMinas Recibe un Array bidimensional de integers
     * @return devuelve un array bidimensional de chars
     */
    public static char[][] numerarCasillas(char[][] tableroBackend, int[][] posicionMinas) {

        int fila;
        int columna;
        int auxiliar;

        for (int i = 0; i < posicionMinas.length; i++) {

            //transformamos las coordenadas de posicionMinas a las coordenadas del tablero
            fila = posicionMinas[i][0];
            columna = posicionMinas[i][1];

            //Iter Norte
            if (fila > 0) {

                if (tableroBackend[fila - 1][columna] == '/') {
                    tableroBackend[fila - 1][columna] = '1';
                } else if (tableroBackend[fila - 1][columna] != '/' && tableroBackend[fila - 1][columna] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila - 1][columna]) + 1;
                    tableroBackend[fila - 1][columna] = (char) (auxiliar + '0');
                }
            }

            //Iter Nor-este
            if (fila > 0 && columna != tableroBackend[fila].length - 1) {

                if (tableroBackend[fila - 1][columna + 1] == '/') {
                    tableroBackend[fila - 1][columna + 1] = '1';
                } else if (tableroBackend[fila - 1][columna + 1] != '/' && tableroBackend[fila - 1][columna + 1] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila - 1][columna + 1]) + 1;
                    tableroBackend[fila - 1][columna + 1] = (char) (auxiliar + '0');
                }
            }

            //Iter Este
            if (columna < tableroBackend[fila].length - 1) {

                if (tableroBackend[fila][columna + 1] == '/') {
                    tableroBackend[fila][columna + 1] = '1';
                } else if (tableroBackend[fila][columna + 1] != '/' && tableroBackend[fila][columna + 1] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila][columna + 1]) + 1;
                    tableroBackend[fila][columna + 1] = (char) (auxiliar + '0');
                }
            }

            //Iter Sur-este
            if (columna < tableroBackend[fila].length - 1 && fila < tableroBackend.length - 1) {

                if (tableroBackend[fila + 1][columna + 1] == '/') {
                    tableroBackend[fila + 1][columna + 1] = '1';
                } else if (tableroBackend[fila + 1][columna + 1] != '/' && tableroBackend[fila + 1][columna + 1] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila + 1][columna + 1]) + 1;
                    tableroBackend[fila + 1][columna + 1] = (char) (auxiliar + '0');
                }
            }

            //Iter Sur
            if (fila < tableroBackend.length - 1) {

                if (tableroBackend[fila + 1][columna] == '/') {
                    tableroBackend[fila + 1][columna] = '1';
                } else if (tableroBackend[fila + 1][columna] != '/' && tableroBackend[fila + 1][columna] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila + 1][columna]) + 1;
                    tableroBackend[fila + 1][columna] = (char) (auxiliar + '0');
                }
            }

            //Iter Sur-oeste
            if (fila < tableroBackend.length - 1 && columna > 0) {

                if (tableroBackend[fila + 1][columna - 1] == '/') {
                    tableroBackend[fila + 1][columna - 1] = '1';
                } else if (tableroBackend[fila + 1][columna - 1] != '/' && tableroBackend[fila + 1][columna - 1] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila + 1][columna - 1]) + 1;
                    tableroBackend[fila + 1][columna - 1] = (char) (auxiliar + '0');
                }
            }

            //Iter Oeste
            if (columna > 0) {

                if (tableroBackend[fila][columna - 1] == '/') {
                    tableroBackend[fila][columna - 1] = '1';
                } else if (tableroBackend[fila][columna - 1] != '/' && tableroBackend[fila][columna - 1] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila][columna - 1]) + 1;
                    tableroBackend[fila][columna - 1] = (char) (auxiliar + '0');
                }
            }

            //Iter Nor-oeste
            if (fila > 0 && columna > 0) {

                if (tableroBackend[fila - 1][columna - 1] == '/') {
                    tableroBackend[fila - 1][columna - 1] = '1';
                } else if (tableroBackend[fila - 1][columna - 1] != '/' && tableroBackend[fila - 1][columna - 1] != '■') {

                    auxiliar = Character.getNumericValue(tableroBackend[fila - 1][columna - 1]) + 1;
                    tableroBackend[fila - 1][columna - 1] = (char) (auxiliar + '0');
                }
            }
        }
        System.out.println("");

        return tableroBackend;

    }

    /**
     * Recibe una Array de chars (Tablero de juego) y lo printea de manera
     * responsiva, con un formato y colores concretos representando las casillas
     * del tablero de juego.
     *
     * @param array Recibe una array de Chars.
     */
    public static void printTablero(char[][] array) {

        //Printeamos parte superior
        System.out.print(BLANCO_FUERTE + "      ╦" + RESET);
        for (int j = 0; j < array[0].length; j++) {
            System.out.print(BLANCO_FUERTE + "═════╦" + RESET);
        }
        System.out.println();

        System.out.print(NEGRO_FUERTE + "      •" + RESET);
        for (int i = 0; i < array[0].length; i++) {

            //Printeamos numeros guia superiores
            if (i <= 8) {
                System.out.print(" ");
                System.out.print(CYAN_FONDO + (i + 1) + (" ") + RESET);
                System.out.print(NEGRO_FUERTE + "  •" + RESET);
            } else {
                System.out.print(" ");
                System.out.print(CYAN_FONDO + (i + 1) + RESET);
                System.out.print(NEGRO_FUERTE + "  •" + RESET);
            }
        }
        System.out.println();

        //Printeamos linea divisoria de celdas
        for (int i = 0; i < array.length; i++) {
            System.out.print(BLANCO_FUERTE + "══════╬" + RESET);
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(BLANCO_FUERTE + "═════╬" + RESET);
            }

            //Printeamos numeros de guia
            for (int j = 0; j < array[0].length; j++) {

                if (j == 0) {

                    if (i <= 8) {

                        System.out.print("\n  " + VERDE_FONDO + " " + (i + 1) + RESET + NEGRO_FUERTE + "  •" + RESET);
                    } else {
                        System.out.print("\n  " + VERDE_FONDO + (i + 1) + RESET + NEGRO_FUERTE + "  •" + RESET);
                    }
                }

                //Printeamos contenido de las casillas con color dependiendo del caracter
                switch (array[i][j]) {
                    case '1':
                        System.out.print(AZUL + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '2':
                        System.out.print(VERDE + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '3':
                        System.out.print(ROJO_FUERTE + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '4':
                        System.out.print(LILA + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '5':
                        System.out.print(CYAN + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '6':
                        System.out.print(AMARILLO + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '7':
                        System.out.print(ROJO + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '8':
                        System.out.print(AZUL_FUERTE + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '/':
                        System.out.print(NEGRO_FUERTE + "  " + array[i][j] + "  " + RESET);
                        break;
                    case '■':
                        System.out.print("  " + ROJO + array[i][j] + "  " + RESET);
                        break;
                    case ' ':
                        System.out.print("  " + array[i][j] + "  ");
                        break;
                    default:
                        break;
                }

                System.out.print(BLANCO_FUERTE + "║" + RESET);
            }
            System.out.println("");
        }

        //Printeamos parte inferior
        System.out.print(BLANCO_FUERTE + "══════╩" + RESET);
        for (int j = 0; j < array[0].length; j++) {
            System.out.print(BLANCO_FUERTE + "═════╩" + RESET);
        }
        System.out.println();
    }

    /**
     * Recibe dos Arrays, una representando el tablero Lleno(Back-end) y otro el
     * tablero para display(Front-end) que se muestra cubierto para el jugador,
     * recibe tambien las coordenadas como numeros enteros representando la fila
     * y la columna, compara las coordenadas recibidas en el tablero lleno, y
     * las extrapola al tablero vacio para display.
     *
     * @param tableroBackend Array de chars con las posiciones llenas
     * @param tableroFrontend Array de chars con las posiciones vacias
     * @param fila Recibe un int
     * @param columna Recibe un int
     * @return Devuelve el tablero para display(Front-end) con la casilla
     * seleccionada descubierta.
     */
    public static char[][] comparaDisplay(char[][] tableroBackend, char[][] tableroFrontend, int fila, int columna) {

        // Contrastamos las coordenadas con los 3 posibles escenarios, bomba, espacio vacio, o numero.
        if (tableroBackend[fila][columna] == '■') {

            tableroFrontend[fila][columna] = tableroBackend[fila][columna];

        } else if (tableroBackend[fila][columna] == '/') {

            mostrarCasillasVacias(tableroBackend, tableroFrontend, fila, columna);

        } else if (tableroBackend[fila][columna] != '■' && tableroBackend[fila][columna] != '/') {

            tableroFrontend[fila][columna] = tableroBackend[fila][columna];
        }

        return tableroFrontend;
    }

    /**
     * Comprueba que los integers(fila,columna) queden dentro de la longitud del
     * ArrayLleno (Back-end), ya que seran usados como punto de partida concreto
     * dentro del ArrayLleno(Back-end) para iterar sobre el, tambien comprueba
     * que las coordenadas resultantes no coincidan con los caracteres '■' o '
     * ', En caso de coincidir con el caracter '/' en el ArrayLleno(Back-end),
     * itera recursivamente con un for en las 8 posiciones inmediatamente
     * adyacentes a esa posicion y en todas las posiciones que lo encuentre
     * printeara un ' ' en su lugar en el ArrayVacio(Front-end) a modo de espejo
     * , que luego sera el que mostrara a el usuario.
     *
     * @param tableroBackend Recibe un Array de chars
     * @param tableroFontend Recibe un Array de chars
     * @param fila Recibe un integer
     * @param columna Recibe un integer
     * @return Devuelve el Array TableroVacio(Back-end) con las posiciones
     * pertinentes sobreescritas con el caracter ' '
     */
    public static char[][] mostrarCasillasVacias(char[][] tableroBackend, char[][] tableroFontend, int fila, int columna) {

        //Comprobacion: coordenadas de fila y columna quedan dentro de los limites del tablero(Array)
        if (fila < 0 || columna < 0 || fila > tableroBackend.length - 1 || columna > tableroBackend[fila].length - 1) {

            return tableroFontend;

        } //Caso 1: Si las coordenadas que recibe el metodo ya son "casilla destapada" en el tablero FRONT end, no sigue recuriendo y termina.
        else if (tableroFontend[fila][columna] == ' ') {

            return tableroFontend;

        } //Caso 2: Si el metodo encuentra una bomba ('■') en el tablero BACK end, no sigue recurriendo y termina
        else if (tableroBackend[fila][columna] == '■') {

            return tableroFontend;

        } //Si el caracter encontrado en el tablero BACK end es "casilla cubierta" ('/')...
        //entonces cambia la casilla homologa del tablero FRONT end ('/') a "casilla descubierta"(' ')...
        //y sigue iterando recursivamente sobre el tablero BACK end...
        //hasta que todas las casillas homologas de el tablero FRONT end han sido cambiadas a "casilla descubierta"(' ')
        //o sean Bombas o Numeros en el BACK end, cumpliendo asi los casos 1, 2 y 3.
        else if (tableroBackend[fila][columna] == '/') {

            tableroFontend[fila][columna] = ' ';

            //For recursivo donde i es igual a fila -1 y el maximo valor es i+1, lo mismo para j.
            for (int i = fila - 1; i < fila + 2; i++) {
                for (int j = columna - 1; j < columna + 2; j++) {
                    mostrarCasillasVacias(tableroBackend, tableroFontend, i, j);
                }
            }

            return tableroFontend;

        } //Caso 3: En caso de no encontrar un espacio en el FRONT end (' ') o un a bomba en el BACK end('■'), o un espacio en el BACK end ('/')
        //es decir, en caso de que encuentre un numero, copia el numero encontrado en el BACK end al FRONT end y termina el metodo.
        else {

            tableroFontend[fila][columna] = tableroBackend[fila][columna];

            return tableroFontend;
        }
    }

    /**
     * Itera sobre la Matriz de chars y comprueba si hay un caracter
     * determinado, de haberlo retorna true, de lo contrario false.
     *
     * @param tableroFrontend Recibe una matriz de chars
     * @param fila Recibe un integer
     * @param columna Recibe un integer
     * @return Devuelve un valor booleano
     */
    public static boolean bombaDestapada(char[][] tableroFrontend, int fila, int columna) {

        //Caso directo para evitar la iteracion bruta
        return tableroFrontend[fila][columna] == '■';
    }

    /**
     * Recibe un Array de chars (tablero de juego Back-end), y las coordenadas
     * de la posicion (fila, columna), comprueba que no sea '■' (cuadrado), '
     * '(espacio) o '/'(barra) , esto es, comprueba que los caracteres sean
     * numeros los pasa a integer y devuelve el integer. total.
     *
     *
     *
     * @param tableroBackend Recibe un array de chars
     * @param fila recibe un integer
     * @param columna recibe un integer
     * @return Devuelve in integer con la puntuacion total
     */
    public static int getPuntuacionCasilla(char[][] tableroBackend, int fila, int columna) {

        int puntuacionCasilla = 0;

        if (tableroBackend[fila][columna] != '■' && tableroBackend[fila][columna] != ' ' && tableroBackend[fila][columna] != '/') {

            puntuacionCasilla = Character.getNumericValue(tableroBackend[fila][columna]);

        }

        return puntuacionCasilla;

    }

    /**
     * Recibe un Array de chars (tablero de juego Back-End ) e itera sobre el
     * buscando todos los caracteres que no sean '■'(cuadrado) , numero , o
     * espacio esto es, busca los caracteres que sean '/' los cuenta y los suma
     * en una variable auxiliar, luego devuelve la suma total, Esto devuelve el
     * total de casillas restantes todavia tapadas.
     *
     * ha conseguido durante la misma.
     *
     * @param tableroFrontend
     * @return devuelve un integer con la cantidad de casillas '/' encontradas.
     */
    public static int getCasillasRestantes(char[][] tableroFrontend) {

        int casillasRestantes = 0;

        for (char[] tableroVacio1 : tableroFrontend) {
            for (int j = 0; j < tableroVacio1.length; j++) {
                if (tableroVacio1[j] == '/') {
                    casillasRestantes++;
                }
            }
        }

        return casillasRestantes;
    }

    /**
     * Metodo Opción de menu 1 del juego Buscaminas, limita la cantidad de minas
     * a 10 y el grid a 8x8, no ofrece opciones de personalización, Recibe un
     * objeto de tipo Jugador que alamcena el nobre, puntuacion y tiempo
     * conseguidos por el mismo, es decir, dispone de un ranking de mejores
     * puntuaciones.
     *
     * Hace uso de los metodos:
     *
     * - marcarPosisVacias(); - plantarMinasEn(); - getPosicionMinas(); -
     * numerarCasillas(); - getCasillasRestantes(); - getPuntuacionCasilla();
     * -printTablero(); - comparaDisplay(); - bombaDestapada();
     *
     * @param jugador
     * @return devuelve un objeto de clase Jugador
     */
    public static boolean menu1(Jugador jugador) {
        Scanner input = new Scanner(System.in);

        int cantMinas = 10;
        final int filas = 8;
        final int columnas = 8;
        char[][] tableroBackend = new char[filas][columnas];
        char[][] tableroFrontend = new char[filas][columnas];
        int[][] posicionMinas;
        int fila;
        int columna;
        boolean tableroGenerado = false;
        boolean hacked = false;
        boolean bombaEncontrada = false;
        boolean posYaDescubierta;
        int puntuacionTotal = 0;
        int casillasRestantes;
        boolean victoria = false;
        long Inicio;
        long Final;
        float segundos = 0f;
        boolean saveAndExit = false;

        //Creamos un tablero vacio para el display
        marcarPosisVacias(tableroFrontend);

        //Printeamos tablero vacio con timer y puntuaciones
        System.out.println(BLANCO_FONDO + "\nTIEMPO: 0s" + RESET);
        System.out.println();
        System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);
        printTablero(tableroFrontend);

        //calculamos el comienzo del timer
        Inicio = System.currentTimeMillis();

        //Creamos el loop para el juego
        do {

            posYaDescubierta = false;

            //Coordenadas
            System.out.println("\nINTRODUCE LAS COORDENADAS.");
            System.out.print("FILA: ");

            //Tenemos un metodo que lee ints, pero esta modificado para que solo acepte
            //como no int al char 'x' si el caracter es x fin de juego, si no continua.
            fila = leerInt();
            if (fila == 'x') {

                saveAndExit = true;
                break;
            }
            //restamos 1 a la fila para que no se salga de la array
            fila -= 1;
            // Introducimos modo hacked!
            if (fila == -2 && hacked == false && tableroGenerado == true) {

                hacked = true;

            } else {

                System.out.print("COLUMNA: ");
                columna = leerInt() - 1;

                //Controlamos que las coordenadas esten dentro del rango del tablero
                while (fila < 0 || columna < 0 || fila > tableroFrontend.length - 1 || columna > tableroFrontend[fila].length - 1) {

                    System.out.println(ROJO_FUERTE + "\nFUERA DEL LÍMITE, REINTRODUCE POSICIÓN" + RESET);
                    System.out.print("FILA: ");
                    fila = leerInt() - 1;
                    System.out.print("COLUMNA: ");
                    columna = leerInt() - 1;

                }

                //Llenamos el tablero apartir del primer input 
                if (tableroGenerado == false) {

                    //Llenamos tablero principal con minas de modo aleatorio.
                    plantarMinasEn(tableroBackend, filas, columnas, cantMinas, fila, columna);

                    //Averiguamos la posicion de las minas
                    posicionMinas = getPosicionMinas(tableroBackend);

                    //Numeramos las casillas alrededor de las minas
                    numerarCasillas(tableroBackend, posicionMinas);

                    tableroGenerado = true;
                }

                //Comprobamos que la posicion no haya sido descubierta con anterioridad  
                if (tableroFrontend[fila][columna] != '/') {

                    posYaDescubierta = true;

                } else {

                    //Comprobamos la puntuacion de la casilla seleccionada
                    puntuacionTotal += getPuntuacionCasilla(tableroBackend, fila, columna);

                    //Introducimos las coordenadas en el tablero vacio y las extrapolamos al tablero lleno
                    comparaDisplay(tableroBackend, tableroFrontend, fila, columna);
                    bombaEncontrada = bombaDestapada(tableroFrontend, fila, columna);
                }

            }

            //Si modo hacked on mostramos tablero Lleno
            if (hacked == true) {
                System.out.println("\n");
                printTablero(tableroBackend);
                System.out.println(ROJO + "MODO HACKED!" + RESET);
            }

            //Calculamos el final del timer
            Final = System.currentTimeMillis();

            //Calculamos la diferencia y printeamos tiempo y puntuacion
            segundos = ((Inicio - Final) / 1000) * -1;
            System.out.println("\n\n" + BLANCO_FONDO + "\nTIEMPO: " + (int) segundos + "s" + RESET);
            System.out.println();
            System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);

            //Printeamos tablero vacio
            printTablero(tableroFrontend);
            System.out.println("['X'] Salir y Guardar.");

            //Mensaje de posicion descubierta
            if (posYaDescubierta == true) {
                System.out.print(ROJO_FUERTE + "\nESA POSICION YA HA SIDO DESCUBIERTA, " + RESET);
                System.out.println(NEGRO_FUERTE + "\nELIGE OTRA POSICION." + RESET);
            }

            //Comprobamos cuantas casillas tapadas quedan
            casillasRestantes = getCasillasRestantes(tableroFrontend);

            //Comprobamos que las casillas restantes no sean = a las bombas totales, si es asi, juego terminado.
            if (casillasRestantes == cantMinas) {
                victoria = true;
            }

        } while (bombaEncontrada == false && victoria == false);

        //Contamos la puntuacion total en caso de victoria o derrota y la printeamos
        if (victoria == true) {
            System.out.println("\n"+ VERDE + "ENHORABUENA, HAS GANADO!" + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        } else if (bombaEncontrada == true) {
            System.out.println("\n"+ ROJO + "LO SENTIMOS, OTRA VEZ SERÁ." + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        }

        //Pedimos el nombre del jugador y registramos su puntuacion y tiempo para el Leaderboard y penalizamos el modo hacked.
        if (saveAndExit == true) {

            guardarTablero(FILEPATH, tableroBackend, tableroFrontend, puntuacionTotal);

        } else {
            if (hacked == true) {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == false) {

                    jugador.nombre += " Cheater";

                } else {

                    jugador.nombre += "Desconocido Cheater";
                }

                jugador.puntuacion = 1;
                jugador.fecha = System.currentTimeMillis();

            } else {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == true) {
                    jugador.nombre = "Desconocido";
                }

                jugador.puntuacion = puntuacionTotal;
                jugador.fecha = System.currentTimeMillis();


            }
            //Printeamos saltos de linea
            for (int i = 0; i < 15; i++) {
                System.out.println("\n\n");
            }
        }

        return saveAndExit;
    }

    /**
     * Metodo Opción de menu 2 del juego Buscaminas, limita la cantidad de minas
     * a 50 y el grid a 16x20, no ofrece opciones de personalización, Recibe un
     * objeto de tipo Jugador que alamcena el nobre, puntuacion y tiempo
     * conseguidos por el mismo, es decir, dispone de un ranking de mejores
     * puntuaciones.
     *
     * Hace uso de los metodos:
     *
     * - marcarPosisVacias(); - plantarMinasEn(); - getPosicionMinas(); -
     * numerarCasillas(); - getCasillasRestantes(); - getPuntuacionCasilla(); -
     * printTablero(); - comparaDisplay(); - bombaDestapada();
     *
     * @param jugador Recibe un objeto tipo Jugador
     * @return devuelve un objeto tipo Jugador con (nombre,tiempo y puntuacion)
     */
    public static boolean menu2(Jugador jugador) {
        Scanner input = new Scanner(System.in);

        final int cantMinas = 50;
        final int filas = 16;
        final int columnas = 20;
        char[][] tableroBackend = new char[filas][columnas];
        char[][] tableroFrontend = new char[filas][columnas];
        int[][] posicionMinas;
        int fila;
        int columna;
        boolean tableroGenerado = false;
        boolean hacked = false;
        boolean bombaEncontrada = false;
        boolean posYaDescubierta;
        int puntuacionTotal = 0;
        int casillasRestantes;
        boolean victoria = false;
        long Inicio;
        long Final;
        float segundos = 0f;
        boolean saveAndExit = false;

        //Creamos un tablero vacio para el display
        marcarPosisVacias(tableroFrontend);

        System.out.println(BLANCO_FONDO + "\nTIEMPO: 0s" + RESET);
        System.out.println();
        System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);
        printTablero(tableroFrontend);

        //calculamos el comienzo del timer
        Inicio = System.currentTimeMillis();

        //Creamos el loop para el juego
        do {

            posYaDescubierta = false;

            //Coordenadas
            System.out.println("\nINTRODUCE LAS COORDENADAS.");

            System.out.print("FILA: ");
            fila = leerInt();
            if (fila == 'x') {

                saveAndExit = true;
                break;
            }
            //restamos 1 a la fila para que no se salga de la array
            fila -= 1;

            // Introducimos modo hacked!
            if (fila == -2 && hacked == false && tableroGenerado == true) {

                hacked = true;

            } else {

                System.out.print("COLUMNA: ");
                columna = leerInt() - 1;

                //Controlamos que las coordenadas esten dentro del rango del tablero
                while (fila < 0 || columna < 0 || fila > tableroFrontend.length - 1 || columna > tableroFrontend[fila].length - 1) {

                    System.out.println(ROJO_FUERTE + "\nFUERA DEL LÍMITE, REINTRODUCE POSICIÓN" + RESET);
                    System.out.print("FILA: ");
                    fila = leerInt() - 1;
                    System.out.print("COLUMNA: ");
                    columna = leerInt() - 1;

                }

                //Llenamos el tablero apartir del primer input 
                if (tableroGenerado == false) {

                    //Llenamos tablero principal con minas de modo aleatorio.
                    plantarMinasEn(tableroBackend, filas, columnas, cantMinas, fila, columna);

                    //Averiguamos la posicion de las minas
                    posicionMinas = getPosicionMinas(tableroBackend);

                    //Numeramos las casillas alrededor de las minas
                    numerarCasillas(tableroBackend, posicionMinas);

                    tableroGenerado = true;
                }

                //Comprobamos que la posicion no haya sido descubierta con anterioridad  
                if (tableroFrontend[fila][columna] != '/') {

                    posYaDescubierta = true;

                } else {

                    //Comprobamos la puntuacion de la casilla seleccionada
                    puntuacionTotal += getPuntuacionCasilla(tableroBackend, fila, columna);

                    //Introducimos las coordenadas en el tablero vacio y las extrapolamos al tablero lleno
                    comparaDisplay(tableroBackend, tableroFrontend, fila, columna);
                    bombaEncontrada = bombaDestapada(tableroFrontend, fila, columna);
                }

            }

            //Si modo hacked on mostramos tablero Lleno
            if (hacked == true) {
                System.out.println("\n");
                printTablero(tableroBackend);
                System.out.println(ROJO + "MODO HACKED!" + RESET);
            }

            //Calculamos el final del timer
            Final = System.currentTimeMillis();

            //Calculamos la diferencia y printeamos tiempo y puntuacion
            segundos = ((Inicio - Final) / 1000) * -1;
            System.out.println("\n\n" + BLANCO_FONDO + "\nTIEMPO: " + (int) segundos + "s" + RESET);
            System.out.println();
            System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);

            //Printeamos tablero vacio
            printTablero(tableroFrontend);
            System.out.println("['X'] Salir y Guardar.");

            //Mensaje de posicion descubierta
            if (posYaDescubierta == true) {
                System.out.print(ROJO_FUERTE + "\nESA POSICION YA HA SIDO DESCUBIERTA, " + RESET);
                System.out.println(NEGRO_FUERTE + "\nELIGE OTRA POSICION." + RESET);
            }

            //Comprobamos cuantas casillas tapadas quedan
            casillasRestantes = getCasillasRestantes(tableroFrontend);

            //Comprobamos que las casillas restantes no sean = a las bombas totales, si es asi, juego terminado.
            if (casillasRestantes == cantMinas && bombaEncontrada == false) {
                victoria = true;
            }

        } while (bombaEncontrada == false && victoria == false);

        //Contamos la puntuacion total en caso de victoria o derrota y la printeamos
        if (victoria == true) {
            System.out.println("\n"+ VERDE + "ENHORABUENA, HAS GANADO!" + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        } else if (bombaEncontrada == true) {
            System.out.println("\n" + ROJO + "LO SENTIMOS, OTRA VEZ SERÁ." + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        }

        if (saveAndExit == true) {

            guardarTablero(FILEPATH, tableroBackend, tableroFrontend, puntuacionTotal);

        } else {
            //Pedimos el nombre del jugador y registramos su puntuacion y tiempo para el Leaderboard y penalizamos el modo hacked.
            if (hacked == true) {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == false) {
                    jugador.nombre += " Cheater";

                } else {

                    jugador.nombre += "Desconocido Cheater";
                }
                jugador.puntuacion = 1;
                jugador.fecha = System.currentTimeMillis();

            } else {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == true) {
                    jugador.nombre = "Desconocido";
                }

                jugador.puntuacion = puntuacionTotal;
                jugador.fecha = System.currentTimeMillis();
            }

            //Printeamos saltos de linea
            for (int i = 0; i < 15; i++) {
                System.out.println("\n\n");
            }
        }

        return saveAndExit;

    }

    /**
     * Metodo Opción de menu 3 del juego Buscaminas, el usuario define el tamaño
     * del tablero introduciento la cantidad de filas y columnas del grid, el
     * usuario tambien define la cantidad de bombas a introducir en el tablero
     * de juego, recibe un objeto de tipo Jugador que alamcena el nobre,
     * puntuacion y tiempo conseguidos por el mismo, es decir, dispone de un
     * ranking de mejores puntuaciones.
     *
     *
     * Hace uso de los metodos:
     *
     * - marcarPosisVacias(); - plantarMinasEn(); - getPosicionMinas(); -
     * numerarCasillas(); - getCasillasRestantes(); - getPuntuacionCasilla(); -
     * printTablero(); - comparaDisplay(); - bombaDestapada();
     *
     * @param jugador Recibe un objeto Jugador con (nombre, puntuacion y tiempo)
     * @return Devuelve un objeto tipo jugador con los resultados de la partida.
     */
    public static boolean menu3(Jugador jugador) {
        Scanner input = new Scanner(System.in);

        int cantMinas;
        int filas;
        int columnas;
        char[][] tableroBackend;
        char[][] tableroFrontend;
        int[][] posicionMinas;
        int fila;
        int columna;
        boolean tableroGenerado = false;
        boolean hacked = false;
        boolean bombaEncontrada = false;
        boolean posYaDescubierta;
        int puntuacionTotal = 0;
        int casillasRestantes;
        boolean victoria = false;
        long Inicio;
        long Final;
        float segundos = 0f;
        boolean saveAndExit = false;

        //Introducimos el tamaño del tablero personalizado
        System.out.println("\n  " + AZUL_FUERTE + "-MODO DE JUEGO PERSONALIZADO-" + RESET + "\n\n");
        System.out.println("\n " + BLANCO_FONDO + "INTRODUCE EL TAMAÑO DEL TABLERO:" + RESET + "\n");
        System.out.print("      CANTIDAD FILAS: ");
        filas = leerInt();
        System.out.print("      CANTIDAD COLUMNAS: ");
        columnas = leerInt();

        //Controlamos que el tablero sea jugable
        while (filas < 3 || columnas < 3 || filas > 99 || columnas > 99) {
            System.out.println(ROJO_FUERTE + "\nTABLERO DEMASIADO PEQUEÑO/GRANDE, " + RESET);
            System.out.println(NEGRO_FUERTE + "REINTRODUCE EL TAMAÑO DEL TABLERO: \n" + RESET);
            System.out.print("      CANTIDAD FILAS: ");
            filas = leerInt();
            System.out.print("      CANTIDAD COLUMNAS: ");
            columnas = leerInt();
        }

        tableroBackend = new char[filas][columnas];
        tableroFrontend = new char[filas][columnas];

        //Introducimos la cantidad de minas personalizadas
        System.out.print("      CANTIDAD DE MINAS: ");
        cantMinas = leerInt();

        //Controlamos que la cantidad de minas no sea superior a la mitad del tamaño del tablero
        //para darle una cierta jugabilidad
        while (cantMinas < 1 || cantMinas > filas * columnas / 2) {
            System.out.println(ROJO_FUERTE + "\nDEMASIADAS O DEMASIADO POCAS MINAS, " + RESET);
            System.out.println(NEGRO_FUERTE + "REINTRODUCE LA CANTIDAD DE MINAS. " + RESET);
            System.out.print("\n      CANTIDAD DE MINAS: ");
            cantMinas = leerInt();
        }

        //Espacios para perder de vista el menu anterior.
        for (int i = 0; i < 15; i++) {
            System.out.println("\n\n");
        }

        //Creamos un tablero vacio para el display
        marcarPosisVacias(tableroFrontend);

        System.out.println(BLANCO_FONDO + "\nTIEMPO: 0s" + RESET);
        System.out.println();
        System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);
        printTablero(tableroFrontend);

        //calculamos el comienzo del timer
        Inicio = System.currentTimeMillis();

        //Creamos el loop para el juego
        do {

            posYaDescubierta = false;

            //Coordenadas
            System.out.println("\nINTRODUCE LAS COORDENADAS: \n");

            System.out.print("FILA: ");
            fila = leerInt();
            if (fila == 'x') {

                saveAndExit = true;
                break;
            }
            //restamos 1 a la fila para que no se salga de la array
            fila -= 1;

            // Introducimos modo hacked!
            if (fila == -2 && hacked == false && tableroGenerado == true) {

                hacked = true;

            } else {

                System.out.print("COLUMNA: ");
                columna = leerInt() - 1;

                //Controlamos que las coordenadas esten dentro del rango del tablero
                while (fila < 0 || columna < 0 || fila > tableroFrontend.length - 1 || columna > tableroFrontend[fila].length - 1) {

                    System.out.println(ROJO_FUERTE + "\nFUERA DEL LÍMITE, REINTRODUCE POSICIÓN" + RESET);
                    System.out.print("FILA: ");
                    fila = leerInt() - 1;
                    System.out.print("COLUMNA: ");
                    columna = leerInt() - 1;

                }

                //Llenamos el tablero apartir del primer input 
                if (tableroGenerado == false) {

                    //Llenamos tablero principal con minas de modo aleatorio.
                    plantarMinasEn(tableroBackend, filas, columnas, cantMinas, fila, columna);

                    //Averiguamos la posicion de las minas
                    posicionMinas = getPosicionMinas(tableroBackend);

                    //Numeramos las casillas alrededor de las minas
                    numerarCasillas(tableroBackend, posicionMinas);

                    tableroGenerado = true;
                }

                //Comprobamos que la posicion no haya sido descubierta con anterioridad  
                if (tableroFrontend[fila][columna] != '/') {

                    posYaDescubierta = true;

                } else {

                    //Comprobamos la puntuacion de la casilla seleccionada
                    puntuacionTotal += getPuntuacionCasilla(tableroBackend, fila, columna);

                    //Introducimos las coordenadas en el tablero vacio y las extrapolamos al tablero lleno
                    comparaDisplay(tableroBackend, tableroFrontend, fila, columna);
                    bombaEncontrada = bombaDestapada(tableroFrontend, fila, columna);
                }

            }

            //Si modo hacked on mostramos tablero Lleno
            if (hacked == true) {
                System.out.println("\n");
                printTablero(tableroBackend);
                System.out.println(ROJO + "MODO HACKED!" + RESET);
            }

            //Calculamos el final del timer
            Final = System.currentTimeMillis();

            //Calculamos la diferencia y printeamos tiempo y puntuacion
            segundos = ((Inicio - Final) / 1000) * -1;
            System.out.println("\n\n" + BLANCO_FONDO + "\nTIEMPO: " + (int) segundos + "s" + RESET);
            System.out.println();
            System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);

            //Printeamos tablero vacio
            printTablero(tableroFrontend);
            System.out.println("['X'] Salir y Guardar.");

            //Mensaje de posicion descubierta
            if (posYaDescubierta == true) {
                System.out.print(ROJO_FUERTE + "\nESA POSICION YA HA SIDO DESCUBIERTA, " + RESET);
                System.out.println(NEGRO_FUERTE + "\nELIGE OTRA POSICION." + RESET);
            }

            //Comprobamos cuantas casillas tapadas quedan
            casillasRestantes = getCasillasRestantes(tableroFrontend);

            //Comprobamos que las casillas restantes no sean = a las bombas totales, si es asi, juego terminado.
            if (casillasRestantes == cantMinas && bombaEncontrada == false) {
                victoria = true;
            }

        } while (bombaEncontrada == false && victoria == false);

        //Contamos la puntuacion total en caso de victoria o derrota y la printeamos
        if (victoria == true) {
            System.out.println("\n"+ VERDE + "ENHORABUENA, HAS GANADO!" + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL:  " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        } else if (bombaEncontrada == true) {
            System.out.println("\n"+ ROJO + "LO SENTIMOS, OTRA VEZ SERÁ." + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        }

        if (saveAndExit == true) {

            guardarTablero(FILEPATH, tableroBackend, tableroFrontend, puntuacionTotal);

        } else {

            //Pedimos el nombre del jugador y registramos su puntuacion y tiempo para el Leaderboard y penalizamos  el modo hacked.
            if (hacked == true) {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == false) {
                    jugador.nombre += " Cheater";
                } else {
                    jugador.nombre += "Desconocido Cheater";
                }

                jugador.puntuacion = 1;
                jugador.fecha = System.currentTimeMillis();

            } else {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == true) {
                    jugador.nombre = "Desconocido";
                }

                jugador.puntuacion = puntuacionTotal;
                jugador.fecha = System.currentTimeMillis();
            }

            //Printeamos saltos de linea
            for (int i = 0; i < 15; i++) {
                System.out.println("\n\n");
            }
        }

        return saveAndExit;

    }

    /**
     * Metodo Opción de menu 4, Esta destinado a recuperar la ultima partida
     * guardada del juego Buscaminas, Recibe un objeto de tipo Jugador que
     * alamcena el nobre, puntuacion y tiempo conseguidos por el mismo, es
     * decir, dispone de un ranking de mejores puntuaciones.
     *
     * Hace uso de los metodos:
     *
     * - marcarPosisVacias(); - plantarMinasEn(); - getPosicionMinas(); -
     * numerarCasillas(); - getCasillasRestantes(); - getPuntuacionCasilla();
     * -printTablero(); - comparaDisplay(); - bombaDestapada(); -
     * recuperarTablero();
     *
     * @param jugador
     * @return devuelve un objeto de clase Jugador
     */
    public static boolean menu4(Jugador jugador) {
        Scanner input = new Scanner(System.in);

        int cantMinas = 10;
        char[][] tableroBackend = recuperarTablero(FILEPATH, true);
        char[][] tableroFrontend = recuperarTablero(FILEPATH, false);
        int fila;
        int columna;
        boolean hacked = false;
        boolean bombaEncontrada = false;
        boolean posYaDescubierta;
        int puntuacionTotal = recuperarPuntuacion(FILEPATH);
        int casillasRestantes;
        boolean victoria = false;
        long Inicio;
        long Final;
        float segundos = 0f;
        boolean saveAndExit = false;

        //Creamos un tablero vacio para el display
        //marcarPosisVacias(tableroFrontend);
        //Printeamos tablero vacio con timer y puntuaciones
        System.out.println(BLANCO_FONDO + "\nTIEMPO: 0s" + RESET);
        System.out.println();
        System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);
        printTablero(tableroFrontend);

        //calculamos el comienzo del timer
        Inicio = System.currentTimeMillis();

        //Creamos el loop para el juego
        do {

            posYaDescubierta = false;

            //Coordenadas
            System.out.println("\nINTRODUCE LAS COORDENADAS.");
            System.out.print("FILA: ");

            //Tenemos un metodo que lee ints, pero esta modificado para que solo acepte
            //como no int al char 'x' si el caracter es x fin de juego, si no continua.
            fila = leerInt();
            if (fila == 'x') {

                saveAndExit = true;
                break;
            }
            //restamos 1 a la fila para que no se salga de la array
            fila -= 1;
            // Introducimos modo hacked!
            if (fila == -2 && hacked == false) {

                hacked = true;

            } else {

                System.out.print("COLUMNA: ");
                columna = leerInt() - 1;

                //Controlamos que las coordenadas esten dentro del rango del tablero
                while (fila < 0 || columna < 0 || fila > tableroFrontend.length - 1 || columna > tableroFrontend[fila].length - 1) {

                    System.out.println(ROJO_FUERTE + "\nFUERA DEL LÍMITE, REINTRODUCE POSICIÓN" + RESET);
                    System.out.print("FILA: ");
                    fila = leerInt() - 1;
                    System.out.print("COLUMNA: ");
                    columna = leerInt() - 1;

                }

                //Comprobamos que la posicion no haya sido descubierta con anterioridad  
                if (tableroFrontend[fila][columna] != '/') {

                    posYaDescubierta = true;

                } else {

                    //Comprobamos la puntuacion de la casilla seleccionada
                    puntuacionTotal += getPuntuacionCasilla(tableroBackend, fila, columna);

                    //Introducimos las coordenadas en el tablero vacio y las extrapolamos al tablero lleno
                    comparaDisplay(tableroBackend, tableroFrontend, fila, columna);
                    bombaEncontrada = bombaDestapada(tableroFrontend, fila, columna);
                }

            }

            //Si modo hacked on mostramos tablero Lleno
            if (hacked == true) {
                System.out.println("\n");
                printTablero(tableroBackend);
                System.out.println(ROJO + "MODO HACKED!" + RESET);
            }

            //Calculamos el final del timer
            Final = System.currentTimeMillis();

            //Calculamos la diferencia y printeamos tiempo y puntuacion
            segundos = ((Inicio - Final) / 1000) * -1;
            System.out.println("\n\n" + BLANCO_FONDO + "\nTIEMPO: " + (int) segundos + "s" + RESET);
            System.out.println();
            System.out.println(BLANCO_FONDO + "PUNTUACION: " + puntuacionTotal + RESET);

            //Printeamos tablero vacio
            printTablero(tableroFrontend);
            
            System.out.println("['X'] Salir y Guardar.");

            //Mensaje de posicion descubierta
            if (posYaDescubierta == true) {
                System.out.print(ROJO_FUERTE + "\nESA POSICION YA HA SIDO DESCUBIERTA, " + RESET);
                System.out.println(NEGRO_FUERTE + "\nELIGE OTRA POSICION." + RESET);
            }

            //Comprobamos cuantas casillas tapadas quedan
            casillasRestantes = getCasillasRestantes(tableroFrontend);

            //Comprobamos que las casillas restantes no sean = a las bombas totales, si es asi, juego terminado.
            if (casillasRestantes == cantMinas && bombaEncontrada == false) {
                victoria = true;
            }

        } while (bombaEncontrada == false && victoria == false);

        //Contamos la puntuacion total en caso de victoria o derrota y la printeamos
        if (victoria == true) {
            System.out.println("\n"+ VERDE + "ENHORABUENA, HAS GANADO!" + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        } else if (bombaEncontrada == true) {
            System.out.println("\n"+ ROJO + "LO SENTIMOS, OTRA VEZ SERÁ." + RESET);
            System.out.println(AZUL_FUERTE + "\nPUNTUACION TOTAL: " + RESET + puntuacionTotal);
            System.out.println(AZUL_FUERTE + "TIEMPO TOTAL: " + RESET + (int) segundos + "s");
        }

        //Pedimos el nombre del jugador y registramos su puntuacion y tiempo para el Leaderboard y penalizamos el modo hacked.
        if (saveAndExit == true) {

            guardarTablero(FILEPATH, tableroBackend, tableroFrontend,  puntuacionTotal);

        } else {
            if (hacked == true) {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == false) {

                    jugador.nombre += " Cheater";

                } else {

                    jugador.nombre += "Desconocido Cheater";
                }

                jugador.puntuacion = 1;
                jugador.fecha = System.currentTimeMillis();

            } else {

                System.out.print("\nINTRODUCE TU NOMBRE: ");
                jugador.nombre = input.nextLine().trim();

                if (jugador.nombre.isEmpty() == true) {
                    jugador.nombre = "Desconocido";
                }

                jugador.puntuacion = puntuacionTotal;
                jugador.fecha = System.currentTimeMillis();

            }
            //Printeamos saltos de linea
            for (int i = 0; i < 15; i++) {
                System.out.println("\n\n");
            }
        }

        return saveAndExit;
    }

    /*
     * valores de color escapados.
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

