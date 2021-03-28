package Servidor;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class ThreadRecibeServidor implements Runnable {
    private final VistaServidor main; // Frame del servidor
    private String mensaje; // Mensaje que recibe
    private DataInputStream entrada; // Flujo de entrada de objetos
    private final Socket cliente; // Socket del cliente
   
    /**
     * Constructor vacio
     */
    public ThreadRecibeServidor(){
        this.cliente = null;
        this.main = null;
    }
    
    /**
     * Constructor
     * @param cliente
     * @param main 
     */
    public ThreadRecibeServidor(Socket cliente, VistaServidor main){
        this.cliente = cliente;
        this.main = main;
    }  
   
    @Override
    public void run() {
        try {
            // Se abre un flujo de entrada de datos
            entrada = new DataInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecibeServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Recorre hasta que se cierra el servidor
        do {
            // Intenta
            try {
                // Lee un nuevo mensaje
                mensaje = (String) entrada.readUTF();
                mensaje = desencriptar(mensaje);
                // Llama al metodo que te muestra el mensaje
                main.mostrarMensaje(mensaje);
            }catch (EOFException ex) {
                System.out.println("Fin de la conexion: " + ex);
            }catch (IOException ex) {
                Logger.getLogger(ThreadRecibeServidor.class.getName()).log(Level.SEVERE, null, ex);
            }         
        } while (true);
    } 
    
    /**
	 * Metodo para desencriptar
	 * @param secretKey
	 * @return
	 */
	public String desencriptar(String desencriptado) {
		String secretKey="ey esta es la clave";
		String base64EncryptedString = "";
		try {
			byte[] message = Base64.decodeBase64(desencriptado.getBytes("utf-8"));
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
			byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
			SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			Cipher decipher = Cipher.getInstance("DESede");
			decipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainText = decipher.doFinal(message);
			base64EncryptedString = new String(plainText, "UTF-8");
		} catch (Exception ex) {
			
		}
		
                System.out.println(base64EncryptedString);
                
		return base64EncryptedString;
	}
} 
