package Cliente;

import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;


        
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
} 
