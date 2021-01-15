package utils;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author sherab
 */
public class lectura {

    /**
     * Recibe un double y comprueba que sea un double.
     *
     * @return devuelve un double.
     */
    public static double leerDouble() {

        Scanner input = new Scanner(System.in);
        double numero = 0;
        boolean leido = false;

        do {
            if (input.hasNextDouble()) {
                numero = input.nextDouble();
                leido = true;
            } else {
                System.out.print("Error, No es un número: ");
                input.nextLine();
            }

        } while (!leido);
        return numero;
    }

    /**
     * Recibe un entero y comprueba que sea un numero entero.
     * permite recibir un unico caracter que no sea integer,
     * este es el char 'x' , representado como 88 en ASCII
     * de modo que podamos usar la 'x' para salir del bucle si asi lo queremos.
     *
     * @return devuelve un entero
     */
    public static int leerInt() {

        Scanner input = new Scanner(System.in);
        int numero = 0;
        boolean leido = false;
        char x;

        do {
            if (input.hasNextInt()) {

                numero = input.nextInt();
                leido = true;

            } else if (input.hasNext()) {

                x = input.next().charAt(0);
                if (x == 120) {
                    return x;

                }
                leido = true;
            } else {
                System.out.print(ROJO_FUERTE + "\n\t     POR FAVOR, INTRODUCE UN NÚMERO ENTERO: " + RESET);
                input.nextLine();
            }

        } while (!leido);
        return numero;
    }

    /**
     * Redibe un Float y comprueba que sea un numero.
     *
     * @return devuelve un Float.
     */
    public static float leerFloat() {

        Scanner input = new Scanner(System.in);
        float numero = 0;
        boolean leido = false;

        do {
            if (input.hasNextFloat()) {
                numero = input.nextFloat();
                leido = true;
            } else {
                System.out.print("Error, Introduce un número: ");
                input.nextLine();
            }
        } while (!leido);
        return numero;
    }

    /**
     * Recibe un numero entero y lo establece como un maximo, comprueba que sea
     * un numero y que esté dentro del rango 0 a n;
     *
     *
     * @param max recibe un integer
     * @return devuelve un integer
     */
    public static int leerNumMax(int max) {
        int n = 0;
        boolean salir = false;

        do {
            n = leerNum();
            if (n >= 0 && n <= max) {
                salir = true;
            } else {
                System.out.println("Error: opción no válida.");

            }
        } while (!salir);
        return n;
    }

    /**
     * Recibe un numero y comprueba que sea un numero.
     *
     * @return devuelve un integer
     */
    public static int leerNum() {
        Scanner input = new Scanner(System.in);
        int n = 0;
        boolean salir = false;
        do {
            try {
                n = input.nextInt();
                salir = true;
            } catch (Exception e) {
                System.out.println("Error: entrada no válida.");
                input.next();
            }
        } while (!salir);
        return n;
    }

    /**
     * Scanner de integers optimizado.
     *
     * @return devuelve un integer.
     * @throws IOException
     */
    public static int ScannerInt() throws IOException {

        int num = 0, read;
        boolean digito = false;

        while ((read = System.in.read()) != -1) {

            if (read >= '0' && read <= '9') {
                digito = true;
                num = num * 10 + read - '0';
            } else if (digito) {
                break;
            }
        }
        return num;
    }

    /**
     * Recibe dos numeros enteros y los establece como: minimo y maximo, el
     * metodo comprueba que el numero entrado sea un integer y que quede dentro
     * del rango min y max, recibe un String con el mensaje a mostrar en caso de
     * no cumplir las condiciones.
     *
     *
     * @param min recibe un integer
     * @param max recibe un integer
     * @param mensaje Recibe un string con un mensaje personalizado
     * @return devuelve un integer
     */
    public static int leerMenu(int min, int max, String mensaje) {

        int n = 0;
        Scanner input = new Scanner(System.in);
        boolean leido = false;

        do {
            if (input.hasNextInt()) {
                n = input.nextInt();
                if (n >= min && n <= max) {
                    leido = true;
                } else {
                    System.out.print(mensaje);
                }
            } else {
                System.out.print(mensaje);
                input.next();
            }
        } while (!leido);

        return n;
    }

    /*
     * Declaracion de constantes globales con los valores de color escapados.
     */
    // Reset
    public static final String RESET = "\033[0m";  // Reset de texto

    // Colores normales
    public static final String NEGRO = "\033[0;30m";   // NEGRO
    public static final String ROJO = "\033[0;31m";     // ROJO
    public static final String VERDE = "\033[0;32m";   // VERDE
    public static final String AMARILLO = "\033[0;33m";  // AMARILLO
    public static final String AZUL = "\033[0;34m";    // AZUL
    public static final String LILA = "\033[0;35m";  // LILA
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String BLANCO = "\033[0;37m";   // BLANCO

    // Fuerte
    public static final String NEGRO_FUERTE = "\033[1;37m";  // NEGRO_FUERTE
    public static final String ROJO_FUERTE = "\033[1;31m";    // ROJO_FUERTE
    public static final String VERDE_FUERTE = "\033[1;32m";  // VERDE_FUERTE
    public static final String AMARILLO_FUERTE = "\033[1;33m"; // AMARILLO_FUERTE
    public static final String AZUL_FUERTE = "\033[1;34m";   // AZUL_FUERTE
    public static final String LILA_FUERTE = "\033[1;35m"; // LILA_FUERTE
    public static final String CYAN_FUERTE = "\033[1;36m";   // CYAN_FUERTE
    public static final String BLANCO_FUERTE = "\033[1;30m";  // BLANCO_FUERTE

    // Fondo
    public static final String NEGRO_FONDO = "\033[40m";  // NEGRO_FONDO
    public static final String ROJO_FONDO = "\033[41m";    // ROJO_FONDO 
    public static final String VERDE_FONDO = "\033[42m";  // VERDE_FONDO
    public static final String AMARILLO_FONDO = "\033[43m"; // AMARILLO_FONDO
    public static final String AZUL_FONDO = "\033[44m";   // AZUL_FONDO
    public static final String LILA_FONDO = "\033[45m"; // LILA_FONDO
    public static final String CYAN_FONDO = "\033[46m";   // CYAN_FONDO
    public static final String BLANCO_FONDO = "\033[47m";  // BLANCO_FONDO
}
