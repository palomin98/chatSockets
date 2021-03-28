package Cliente;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class ThreadRecibeCliente implements Runnable {
    private final VistaCliente main; // Frame del cliente
    private String mensaje; // Mensaje que recibe el cliente
    private DataInputStream entrada; // Flujo de entrada de objetos
    private final Socket cliente; // Socket del cliente
   
    /**
     * Constructor vacio
     */
    public ThreadRecibeCliente(){
        this.cliente = null;
        this.main = null;
    }
    
    /**
     * Constructor
     * @param cliente
     * @param main 
     */
    public ThreadRecibeCliente(Socket cliente, VistaCliente main){
        this.cliente = cliente;
        this.main = main;
    }  
   
    @Override
    public void run() {
        try {
            // Abrimos un flujo de entrada de objetos
            entrada = new DataInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecibeCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Recorre hasta que se cierra el cliente
        do { 
            // Intenta
            try {
                // Leer un nuevo mensaje
                mensaje = (String) entrada.readUTF();
                mensaje = desencriptar(mensaje);
                
                // Llama al metodo que lo muestra y se lo pasa por parametro
                main.mostrarMensaje(mensaje);
            }catch(SocketException | EOFException ex){
                try {
                    // Cerramos la ventana
                    System.exit(0);
                    // Cerramos el cliente
                    cliente.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ThreadRecibeCliente.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }catch (IOException ex) {
                Logger.getLogger(ThreadRecibeCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true); //Ejecuta hasta que el server escriba TERMINATE
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
