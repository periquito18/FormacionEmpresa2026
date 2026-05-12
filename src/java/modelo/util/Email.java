/*
 * Clase utilitaria para el envio de emails mediante SMTP de Gmail
 * Usa Jakarta Mail (anteriormente JavaMail)
 */
package modelo.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author SGame
 */
public class Email {

    // ---- Configuracion SMTP ----
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_REMITE = "mate.palacios.sergio@iescamas.es";
    private static final String EMAIL_PASSWORD = "xxxx xxxx xxxx xxxx"; // Contraseña de aplicación (Formacion Empresa 2026)

    /**
     * Envia un email con el asunto y cuerpo indicados al destinatario
     *
     * @param destinatario Email del destinatario
     * @param asunto Asunto del email
     * @param cuerpo Cuerpo del email (texto plano o HTML)
     * @param esHTML true si el cuerpo es HTML, false si es texto plano
     * @throws MessagingException Si ocurre algun error durante el envio
     */
    public static void enviar(String destinatario, String asunto, String cuerpo,
            boolean esHTML) throws MessagingException {

        // Configuracion de la conexion SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        // TLS: cifrado de la conexion (obligatorio en Gmail)
        props.put("mail.smtp.starttls.enable", "true");

        // Autenticador: proporciona las credenciales cuando el servidor las pide
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMITE, EMAIL_PASSWORD);
            }
        };

        // Creamos la sesión de correo con la configuración y autenticador
        Session sesion = Session.getInstance(props, auth);

        // Construimos el mensaje
        MimeMessage mensaje = new MimeMessage(sesion);
        mensaje.setFrom(new InternetAddress(EMAIL_REMITE));
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setSubject(asunto, "UTF-8");

        // Establecemos el cuerpo segun el tipo
        if (esHTML) {
            mensaje.setContent(cuerpo, "text/html; charset=UTF-8");
        } else {
            mensaje.setText(cuerpo, "UTF-8");
        }

        // Enviamos el mensaje
        Transport.send(mensaje);
    }

    public static void enviarNotificacionPractica(String emailAlumno, String nombreAlumno,
            String nombreEmpresa, String tutorNombre, String tutorEmail, String fechaInicio, String fechaFin) {
        String asunto = "Asignacion de prácticas - " + nombreEmpresa;

        // Cuerpo del email en HTML para mejor presentación
        String cuerpo = """
            <html>
            <body style="font-family: Arial, sans-serif; color: #333;">
                <h2 style="color: #0066cc;">Asignación de Prácticas en Empresa</h2>
                <p>Estimado/a <strong>%s</strong>,</p>
                <p>Te informamos de que has sido asignado/a a realizar tus 
                   prácticas en empresa. A continuación te indicamos los datos:</p>
                <table style="border-collapse: collapse; width: 100%%; 
                              margin: 20px 0;">
                    <tr style="background-color: #f2f2f2;">
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            <strong>Empresa</strong>
                        </td>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            %s
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            <strong>Tutor laboral</strong>
                        </td>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            %s
                        </td>
                    </tr>
                    <tr style="background-color: #f2f2f2;">
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            <strong>Email del tutor</strong>
                        </td>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            <a href="mailto:%s">%s</a>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            <strong>Fecha de inicio</strong>
                        </td>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            %s
                        </td>
                    </tr>
                    <tr style="background-color: #f2f2f2;">
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            <strong>Fecha de fin</strong>
                        </td>
                        <td style="padding: 10px; border: 1px solid #ddd;">
                            %s
                        </td>
                    </tr>
                </table>
                <p>Si tienes alguna duda, contacta con tu tutor del centro.</p>
                <p>Un saludo,<br>
                <strong>Equipo de Formación en Empresa</strong></p>
            </body>
            </html>
            """.formatted(nombreAlumno, nombreEmpresa, tutorNombre,
                tutorEmail, tutorEmail, fechaInicio, fechaFin);

        try {
            enviar(emailAlumno, asunto, cuerpo, true);
            // System.out.println("SIMULACIÓN: Se enviaría un correo a " + emailAlumno);
            // System.out.println("CUERPO GENERADO: " + cuerpo); // Así ves si el .formatted() funcionó
            System.out.println("Email enviado correctamente a: " + emailAlumno);
        } catch (MessagingException e) {
            // Logueamos el error pero no interrumpimos el flujo de la aplicación
            // La práctica se crea igualmente aunque el email falle
            System.err.println("Error al enviar email a " + emailAlumno + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
