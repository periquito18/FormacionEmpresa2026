/*
 * Clase utilitaria para importar alumnos desde ficheros CSV
 * Formato esperado del CSV:
 * nombre, apellidos, email, fecha_nacimiento
 * Ej: Sergio, Mate Palacios, mate.palacios.sergio@iescamas.es, 2005-05-18
 */
package modelo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import modelo.entidades.Alumno;
import modelo.entidades.Curso;
import modelo.entidades.dao.DaoAlumno;
import modelo.entidades.dao.exceptions.PreexistingEntityException;

/**
 *
 * @author SGame
 */
public class CSVImporter {
    
    /**
     * Clase interna que encapsula el resultado de una importación.
     * Contiene el número de alumnos importados, errores y mensajes de error.
     */
    public static class ResultadoImportacion {
        private int importados = 0;
        private int errores = 0;
        private final List <String> mensajesError = new ArrayList<>();
        
        public void incrementarImportados() { importados++; }
        public void incrementarErrores() { errores++; } 
        public void addError(String msg) { mensajesError.add(msg); }
        
        public int getImportados() { return importados; }
        public int getErrores() { return errores; }
        public List<String> getMensajesError() { return mensajesError; }
    }
    
    /**
     * Lee un InputStream de un CSV e importa los alumnos al curso indicado.
     * Detecta automáticamente si la primera fila es una cabecera
     * 
     * @param inputStream El stream del fichero CSV subido
     * @param curso El curso al que se asignarán los alumnos
     * @param daoAlumno El DAO para persistir los alumnos
     * @return Un ResultadoImportacion con el resumen
     * @throws IOException 
     */
    public static ResultadoImportacion importarAlumnos(InputStream inputStream, 
            Curso curso, DaoAlumno daoAlumno) throws IOException {
        
        ResultadoImportacion resultado = new ResultadoImportacion();
        
        // BufferedReader para leer el fichero linea a linea
        BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        
        String linea;
        int numeroLinea = 0;
        boolean primeraLinea = true;
        
        while ((linea = lector.readLine()) != null) {
            numeroLinea++;
            
            // Ignoramos lineas vacias
            if (linea.trim().isEmpty()) { continue; }
            
            // Detectamos si la primera linea es una cabecera
            // Si empieza por "nombre" o "Nombre" la saltamos
            if (primeraLinea) {
                primeraLinea = false;
                if (linea.toLowerCase().startsWith("nombre")) {
                    continue; // Es una cabecera, la saltamos
                }
            }
            
            // Separamos los campos por coma
            String[] campos = linea.split(",");
            
            // Validamos que tenga exactamente 4 campos
            if (campos.length != 4) {
                resultado.incrementarErrores();
                resultado.addError("Linea " + numeroLinea + ": formato "
                        + "(se esperan 4 campos, hay " + campos.length + ")");
                continue;
            }
            
            // Extraemos y limpiamos cada campo
            String nombre = campos[0].trim();
            String apellidos = campos[1].trim();
            String email = campos[2].trim();
            String fechaStr = campos[3].trim();
            
            // Validamos que ningún campo esté vacío
            if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || fechaStr.isEmpty()) {
                resultado.incrementarErrores();
                resultado.addError("Linea " + numeroLinea + ": hay campos vacios.");
                continue;
            }
            
            // Parseamos la fecha
            LocalDate fechaNacimiento;
            try {
                fechaNacimiento = LocalDate.parse(fechaStr);
            } catch (DateTimeParseException e) {
                resultado.incrementarErrores();
                resultado.addError("Linea " + numeroLinea + ": fecha incorrecta '" 
                        + fechaStr + "' (formato esperado: yyyy-MM-dd)");
                continue;
            }
            
            // Intentamos crear el alumno en la BD
            try {
                Alumno alumno = new Alumno(nombre, apellidos, email, fechaNacimiento, curso);
                daoAlumno.create(alumno);
                resultado.incrementarImportados();
            } catch (PreexistingEntityException e) {
                resultado.incrementarErrores();
                resultado.addError("Linea " + numeroLinea + ": ya existe un alumno con email '" + email + "'");
            } catch (Exception e) {
                resultado.incrementarErrores();
                resultado.addError("Linea " + numeroLinea + ": error inesperado - " + e.getMessage());
            }
        }
        
        return resultado;
    }
}
