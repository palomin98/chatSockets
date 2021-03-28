package Cliente;

import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;


        
public class ThreadEnviaCliente implements Runnable {
    private final VistaCliente main; // Frame del cliente
    private DataOutputStream salida; // Flujo de salida de datos
    private String mensaje; // Mensaje para enviar
    private final Socket cliente; // Socket de Cliente
   
    /**
     * Constructor vacio
     */
    public ThreadEnviaCliente(){
        this.cliente = null;
        this.main = null;
    }
    
    /**
     * Constructor
     * @param cliente
     * @param main 
     */
    public ThreadEnviaCliente(Socket cliente, final VistaCliente main){
        this.cliente = cliente;
        this.main = main;
        
         /**
         * Evento que ocurre cuando se pulsa el boton enviar
         */
        VistaCliente.btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Cogemos el texto que hay dentro del txtField
                mensaje = VistaCliente.txtEnviar.getText();
                
                mensaje = encriptar(mensaje);
                
                // Se envia el mensaje 
                enviarDatos(mensaje); 
                // Borra el texto del TextView
                VistaCliente.txtEnviar.setText("");
            }
        });
    } 
   
    @Override
    public void run() {
        try {
            // Abrimos un flujo de salida de objetos hacia el servidor
            salida = new DataOutputStream(cliente.getOutputStream());
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }
    
    /**
     * Envia los datos al servidor
     * @param mensaje 
     */
    private void enviarDatos(String mensaje){
        try {
            // Escribe el mensaje que le pasan por parametro
            salida.writeUTF(mensaje);
            // Flush salida a servidor
            salida.flush();
        }catch (IOException e){ 
            System.out.println("Error escribiendo Mensaje");
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
