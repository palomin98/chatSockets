package Cliente;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
} 
