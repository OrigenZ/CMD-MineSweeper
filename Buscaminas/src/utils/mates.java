package utils;

/**
 *
 * @author sherab
 */
public class mates {

    /**
     * Calcula la raiz cuadrada de un numero dado, puede ser entero o decimal,
     * controla que el numero no sea menor a 0.
     *
     * @param enunciado recibe un string
     */
    public static void RaizCuadradaDe(String enunciado) {

        double numero;

        System.out.print(enunciado);
        numero = lectura.leerDouble();

        if (numero >= 0) {
            System.out.printf("\nLa raiz cuadrada de %.2f es %.2f", numero, Math.sqrt(numero));
        } else {
            System.out.printf("No se puede hacer la raiz cuadrada de %.2f", numero);
        }
    }

    /**
     * Calcula el factorial de un numero dado.
     *
     * @param numero recibe un numero
     * @return devuelve un double
     */
    public static double FactorialDe(double numero) {

        if (numero <= 1) {
            return 1;
        } else {
            return numero * FactorialDe(numero - 1);
        }
    }

    /**
     * Printea todos los numeros primos existentes entre el numero recibido y 0.
     *
     *
     * @param num Recibe un integer
     */
    public static void intervaloNprimos(int num) {

        boolean esPrimo;

        for (int i = 2; i <= num; i++) {
            esPrimo = true;

            for (int j = 2; j < i; j++) {

                if (i % j == 0) {
                    esPrimo = false;
                }
            }
            if (esPrimo) {
                System.out.println("Numero primo: " + i);
            }
        }
    }


    /**
     * Devuelve la serie Fibonacci hasta el numero n.
     *
     * @param n recibe un integer
     * @return
     */
    public static long Fibo(int n) {

        if (n <= 1) {
            return n;
        } else {
            return Fibo(n - 1) + Fibo(n - 2);

        }
    }
}
