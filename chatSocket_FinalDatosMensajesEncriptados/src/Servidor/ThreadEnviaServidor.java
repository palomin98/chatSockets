package Servidor;

import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
        
public class ThreadEnviaServidor implements Runnable {
    private final VistaServidor main; 
    private DataOutputStream salida;
    private String mensaje;
    private final Socket conexion; 
   
    /**
     * Constructor vacio
     */
    public ThreadEnviaServidor(){
        this.conexion = null;
        this.main = null;
    }
    
    /**
     * Contructor
     * @param conexion
     * @param main 
     */
    public ThreadEnviaServidor(Socket conexion, final VistaServidor main){
        this.conexion = conexion;
        this.main = main;
        
        /**
         * Evento que ocurre cuando se pulsa el boton validar
         */
        VistaServidor.btnValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Crea el mensaje que va a enviar
                mensaje = "Validado: " + VistaServidor.textArea2.getSelectedValue();
                
                mensaje = encriptar(mensaje);
                // Se llama al metodo que envia el mensaje
                enviarDatos(mensaje);
            }
        });
        
        /**
         * Evento que ocurre cuando se pulsa el boton rechazar
         */
        VistaServidor.btnRechazar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Crea el mensaje que va a enviar
                mensaje = "Rechazado: " + VistaServidor.textArea2.getSelectedValue();
                
                mensaje = encriptar(mensaje);
                
                // Se llama al metodo que envia el mensaje
                enviarDatos(mensaje);
            }
        });
    } 

    @Override
    public void run() {
         try {
            // Abro un flujo de salida de datos hacia el cliente
            salida = new DataOutputStream(conexion.getOutputStream());
        } catch (SocketException | NullPointerException ex) {
            System.out.println("ERROR: " + ex);
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }   
   
    /**
    * Metodo que envia el mensaje
    * @param mensaje 
    */
    private void enviarDatos(String mensaje){
        try {
            // Escribe el mensaje
            salida.writeUTF(mensaje);
            
            // Salida de datos al cliente
            salida.flush(); 
        }catch (IOException e){ 
            System.out.println("Error escribiendo Mensaje: " + e);
        }
   }
    
    public  String encriptar(String encriptado) {
		String keySecret="ey esta es la clave";
		String base64EncryptedString = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digestOfPassword = md.digest(keySecret.getBytes("utf-8"));
			byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
			SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainTextBytes = encriptado.getBytes("utf-8");
			byte[] buf = cipher.doFinal(plainTextBytes);
			byte[] base64Bytes = Base64.encodeBase64(buf);
			base64EncryptedString = new String(base64Bytes);
		} catch (Exception ex) {
			
		}
                
                System.out.println(base64EncryptedString);
                
		return base64EncryptedString;
	}
} 
