/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author voidxy
 */
public class strings {

    
    /**
     * Damos formato a la fecha que queremos obtener, obtenemos la fecha actual,
     * con LocalDateTime, y luego creamos un string con la fecha formateada.
     * 
     * 
     * @return devuelve un String con la fecha actual en formato:
     * @"dd/MM/yyyy HH:mm"
     */
    public static String fechaNowString() {

        String fecha;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        fecha = dtf.format(now);
        return fecha;

    }

    /**
     * Se busca el stringAbuscar comparandolo con el stringInicial, empezando
     * desde la posicion 0 del stringInicial y avanzando una posicion en cada
     * loop del for buscando coincidencias totales o parciales con el
     * stringAbuscar, esto se hace ignorando mayusculas .
     *
     * @param stringInicial String en el que buscamos coincidencias
     * @param stringAbuscar El string que buscamos.
     * @return Devuelve true si los strings coinciden false si es lo contrario.
     */
    public static boolean contieneIgnoreCase(String stringInicial, String stringAbuscar) {

        if (stringInicial == null || stringAbuscar == null) {
            return false;
        }

        //Seteamos la maxima longitud a recorer, restandole el string a buscar
        // al string base.
        int caracteres = stringAbuscar.length(); 
        int maxCaracteres = stringInicial.length() - caracteres; 

        //Se busca el sub string avanzando una posicion cada loop 
        for (int i = 0; i <= maxCaracteres; i++) {
            if (stringInicial.regionMatches(true, i, stringAbuscar, 0, caracteres)) {
                return true;
            }
        }
        return false;
    }
}
